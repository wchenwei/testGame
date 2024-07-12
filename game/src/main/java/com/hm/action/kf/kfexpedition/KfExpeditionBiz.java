package com.hm.action.kf.kfexpedition;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.util.date.DateUtil;
import com.hm.action.activity.ActivityBiz;
import com.hm.action.kf.KfObserverBiz;
import com.hm.action.kf.vo.KfExpeditionServerVo;
import com.hm.action.player.PlayerBiz;
import com.hm.config.GameConstants;
import com.hm.config.excel.CommValueConfig;
import com.hm.enums.ActivityType;
import com.hm.enums.CommonValueType;
import com.hm.enums.LogType;
import com.hm.message.MessageComm;
import com.hm.model.activity.kfactivity.KfExpeditionActivity;
import com.hm.model.kf.KfExpeditionServer;
import com.hm.model.player.Player;
import com.hm.model.player.CurrencyKind;
import com.hm.libcore.mongodb.ServerInfo;
import com.hm.redis.PlayerRedisData;
import com.hm.redis.util.RedisUtil;
import com.hm.server.GameServerManager;
import com.hm.server.KfUrlCount;
import com.hm.servercontainer.activity.ActivityServerContainer;
import com.hm.sysConstant.SysConstant;
import com.hm.util.RandomUtils;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Biz
public class KfExpeditionBiz {
	
	@Resource
	private PlayerBiz playerBiz;
	@Resource
	private ActivityBiz activityBiz;
	@Resource
    private CommValueConfig commValueConfig;
    @Resource
    private KfObserverBiz kfObserverBiz;

	
	//找前后5的服务器
	public static final int RangeServer = 10;
	
	/**
	 * 判断是否可以宣战
	 * @param player
	 * @return
	 */
	public boolean isCanDeclare(Player player) {
		//只有周2才能宣战
		if(DateUtil.getCsWeek() != GameConstants.KfExpeditionWeek) {
			return false;
		}
		//只有大总统或大元帅有权限可进行宣战
//		if(!haveExpeditionPower(player.getId())) {
//			return false;
//		}
		return true;
	}
	
	/**
	 * 检查是否能对别人宣战
	 * @param serverId
	 * @param targetId
	 * @return
	 */
	public boolean isCanDeclareForServerId(int serverId,int targetId) {
		List<KfExpeditionServer> serverList = KfExpeditionScoreUtils.getAllKfExpeditionServer();
		KfExpeditionServer server1 = serverList.stream().filter(e -> e.getId() == serverId).findAny().orElse(null);
		KfExpeditionServer server2 = serverList.stream().filter(e -> e.getId() == targetId).findAny().orElse(null);
		if(server1 == null || server2 == null) {
			return false;
		}
		if(server1.getType() > 0 || server2.getType() > 0) {
			return false;
		}
		ServerInfo atkServerInfo = GameServerManager.getServerInfoFromDB(serverId);
		ServerInfo defServerInfo = GameServerManager.getServerInfoFromDB(targetId);
		if(atkServerInfo == null || defServerInfo == null
				|| atkServerInfo.getType() != defServerInfo.getType()) {
			return false;
		}
		return defServerInfo.getDb_id() == 0 || defServerInfo.getDb_id() == defServerInfo.getServer_id();
	}
	
	//只有大总统或大元帅有权限可进行宣战
	public static boolean haveExpeditionPower(long playerId) {
		PlayerRedisData playerRedisData = RedisUtil.getPlayerRedisData(playerId);
		return 
				playerRedisData.getTitleId() == 1 || playerRedisData.getTitleId() == 2;
	}
	
	/**
	 * 获取复仇宣战列表
	 * @param serverId
	 * @return
	 */
	public List<KfExpeditionServerVo> buildExpeditionServerVo(int serverId) {
		KfExpeditionActivity expeditionActivity = (KfExpeditionActivity)ActivityServerContainer.of(serverId).getAbstractActivity(ActivityType.KfExpeditionActivity);
		if(expeditionActivity == null) {
			return Lists.newArrayList();
		}
		List<Integer> failList = expeditionActivity.getFailList();
		if(CollUtil.isEmpty(failList)) {
			return Lists.newArrayList();
		}
		List<KfExpeditionServerVo> serverList = KfExpeditionScoreUtils.getAllKfExpeditionServer().stream()
				.filter(e -> failList.contains(e.getServerId()))
				.filter(e -> e.getId() != serverId)
				.map(e -> new KfExpeditionServerVo(e.getId(), e.getType()))
				.collect(Collectors.toList());
		return serverList;
	}
	
	/**
	 * 处理宣战数据
	 * @param player
	 * @param targetId
	 * @param declaration
	 */
	public boolean doDeclareForServer(Player player,int targetId,String declaration) {
		int serverId = player.getServerId();
		//随机跨服远征服务器
		KfUrlCount kfUrlCount = KfUrlCount.findLuckUrl();
		if(kfUrlCount == null) {
			player.sendErrorMsg(SysConstant.KfExpeditionDeclare_Fail);
			return false;
		}
		String url = kfUrlCount.getKfInfo().getUrl();
		boolean isSuc = sendHttpDeclareForServer(player.getId(),url, serverId, targetId, declaration);
		if(!isSuc) {
			player.sendErrorMsg(SysConstant.KfExpeditionDeclare_Fail);
			return false;
		}
		kfUrlCount.addCount();
		log.error("宣战成功"+serverId+"->"+targetId);
		saveExpeditionServer(serverId, targetId);
		
		KfExpeditionActivity expeditionActivity = (KfExpeditionActivity)ActivityServerContainer.of(player).getAbstractActivity(ActivityType.KfExpeditionActivity);
		expeditionActivity.declareServerForOther(player,targetId, url,declaration);
		expeditionActivity.saveDB();
		activityBiz.broadPlayerActivityUpdate(expeditionActivity);
		//宣战成功
		player.sendMsg(MessageComm.S2C_KfExpeditionDeclare);
		return true;
	}
	
	/**
	 * 对跨服服务器请求宣战
	 * @param serverId
	 * @param targetId
	 * @param declaration
	 */
	public boolean sendHttpDeclareForServer(long declareId,String url,int serverId,int targetId,String declaration) {
		try {
			String httpUrl = "http://"+url.split("#")[0]+":"+url.split("#")[2];
			Map<String , Object> paramMap = Maps.newConcurrentMap();
			paramMap.put("action","kfope.do");
			paramMap.put("m","declareForServer");
			paramMap.put("serverId",serverId);
			paramMap.put("targetId",targetId);
			paramMap.put("declareId",declareId);
			paramMap.put("declaration",declaration);
			paramMap.put("url",url);
			String result = HttpUtil.get(httpUrl, paramMap);
			return StrUtil.equals(result, "1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 保存跨服远征数据
	 * @param serverId
	 * @param targetId
	 */
	public void saveExpeditionServer(int serverId,int targetId) {
		List<KfExpeditionServer> serverList = KfExpeditionScoreUtils.getAllKfExpeditionServer();
		KfExpeditionServer server1 = serverList.stream().filter(e -> e.getId() == serverId).findAny().orElse(null);
		KfExpeditionServer server2 = serverList.stream().filter(e -> e.getId() == targetId).findAny().orElse(null);
		server1.setAtkId(targetId);
		server1.setLastId(targetId);
		server1.setType(1);
		server1.saveDB();
		
		server2.setAtkId(serverId);
		server2.setLastId(serverId);
		server2.setType(2);
		server2.saveDB();
	}
	
	/**
	 * 对特定服务器进行宣战
	 * @param player
	 * @param targetId
	 * @return
	 */
	public void declareFixedServer(Player player,int targetId,String declaration) {
		int serverId = player.getServerId();
		//判断服务器
		if(!isCanDeclareForServerId(serverId, targetId)) {
			//是否可以对此服务器宣战
			player.sendErrorMsg(SysConstant.KfExpeditionDeclare_HaveFight);
			return;
		}
		int spend = commValueConfig.getCommValue(CommonValueType.KfExpeditionServerGold);
		if(!playerBiz.checkPlayerCurrency(player, CurrencyKind.Gold, spend)) {
			player.sendErrorMsg(SysConstant.PLAYER_DIAMOND_NOT);
			return;
		}
		//处理宣战
		if(doDeclareForServer(player, targetId, declaration)) {
			//扣费
			playerBiz.spendPlayerCurrency(player, CurrencyKind.Gold, spend, LogType.KfExpeditionDeclare);
			player.sendUserUpdateMsg();
		}
	}
	
	/**
	 * 随机服务器进行宣战
	 * @param player
	 * @param declaration
	 */
	public void declareRandomServer(Player player,String declaration) {
		int targetId = randomTargetServer(player.getServerId(), RangeServer);
		if(targetId <= 0) {
			player.sendErrorMsg(SysConstant.KfExpeditionDeclare_NotFind);
			return;
		}
		if(!isCanDeclareForServerId(player.getServerId(), targetId)) {
			//是否可以对此服务器宣战
			player.sendErrorMsg(SysConstant.KfExpeditionDeclare_HaveFight);
			return;
		}
		//处理宣战
		doDeclareForServer(player, targetId, declaration);
	}
	
	/**
	 * 随机可宣战服务器
	 * @param serverId
	 * @return
	 */
//	public int randomTargetServer(int serverId,int rangeNum) {
//		//按照积分进行排序
//		List<KfExpeditionServer> randomList = findRangeKfServer(serverId, rangeNum)
//				.stream().filter(e -> e.getType() == 0)
//				.filter(e -> e.getServerId() != serverId)
//				.collect(Collectors.toList());
//		if(randomList.isEmpty()) {
//			return 0;
//		}
//		return randomList.get(0).getId();
////		return RandomUtils.randomEle(randomList).getId();
//	}
	
	public int randomTargetServer(int serverId,int rangeNum) {
		KfExpeditionServer luckServer = findRangeKfServer(serverId, rangeNum);
		if(luckServer != null && luckServer.getId() != serverId && luckServer.getType() == 0) {
			return luckServer.getId();
		}
		return 0;
	}
	
	public KfExpeditionServer findRangeKfServer(int serverId,int rangeNum) {
		//按照积分进行排序
		ServerInfo atkServerInfo = GameServerManager.getServerInfoFromDB(serverId);
		if(atkServerInfo == null || atkServerInfo.getType() == 0) {
			return null;
		}
		//所有同类型服务器
		List<Integer> typeList = GameServerManager.getServerInfoByType(atkServerInfo.getType()).stream()
				.map(e -> e.getServer_id())
				.collect(Collectors.toList());
		//找出所有同类型服务器按照积分排序
		List<KfExpeditionServer> serverList = KfExpeditionScoreUtils.getAllKfExpeditionServer().stream()
						.filter(e -> typeList.contains(e.getServerId()))
//						.filter(e -> e.getType() == 0)
						.sorted(Comparator.comparingLong(KfExpeditionServer::getCombat).reversed())
						.collect(Collectors.toList());
		return findLuckFightServer(serverList, serverId);
	}
	
	public static KfExpeditionServer findLuckFightServer(List<KfExpeditionServer> serverList,int serverId) {
		KfExpeditionServer myServer = serverList.stream().filter(e -> e.getId() == serverId).findFirst().orElse(null);
		if(myServer == null) {
			return null;
		}
		final int serverGroupId = myServer.getGroupId();
		if(serverGroupId > 0) {
			//有分组的从自己分组里匹配
			List<KfExpeditionServer> luckList = serverList.stream().filter(e -> e.getGroupId() == serverGroupId).
					collect(Collectors.toList());
			return randomKfExpeditionServerFromList(myServer, luckList);
		}else{
			List<KfExpeditionServer> luckList = serverList.stream().filter(e -> e.getGroupId() == 0)
					.collect(Collectors.toList());
			if(CollUtil.isEmpty(luckList)) {
				return null;
			}
			if(myServer.isCombatRandom()) {//战力排序
				luckList = luckList.stream().filter(e -> e.isCombatRandom())
						.sorted(Comparator.comparingLong(KfExpeditionServer::getCombat).reversed())
						.collect(Collectors.toList());
				return randomFoCombat(luckList, myServer);
			}else{
				//按照开服天数
				luckList = luckList.stream().filter(e -> !e.isCombatRandom())
						.sorted(Comparator.comparingLong(KfExpeditionServer::getOpenDay).reversed())
						.collect(Collectors.toList());
				return randomFoCombat(luckList, myServer);
			}
		}
	}
	
	public static KfExpeditionServer randomFoCombat(List<KfExpeditionServer> luckList,KfExpeditionServer myServer) {
		int range = 10;//按照排名 10个一组进行匹配
		int index = luckList.indexOf(myServer);
		if(index < 0) {
			return null;
		}
		int startIndex = Math.max((index/range)*range, 0);
		int endIndex = Math.min(startIndex+range, luckList.size());
		return randomKfExpeditionServerFromList(myServer, Lists.newArrayList(luckList.subList(startIndex, endIndex)));
	}
	
	public static KfExpeditionServer randomKfExpeditionServerFromList(KfExpeditionServer myServer,List<KfExpeditionServer> serverList) {
		serverList.remove(myServer);
		List<KfExpeditionServer> luckList = serverList.stream()
				.filter(e -> e.getType() == 0 && e.getId() != myServer.getId())
				.collect(Collectors.toList());
		if(CollUtil.isEmpty(luckList)) {
			return null;
		}
		List<KfExpeditionServer> filterLuckIds = luckList.stream()
				.filter(e -> myServer.getLastId() != e.getId())
				.collect(Collectors.toList());
		if(CollUtil.isNotEmpty(filterLuckIds)) {
			return RandomUtils.randomEle(filterLuckIds);
		}
		return RandomUtils.randomEle(luckList);
	}
	//按照战力去找
//	public static KfExpeditionServer findLuckFightServer(List<KfExpeditionServer> serverList,int serverId) {
//		KfExpeditionServer myServer = serverList.stream().filter(e -> e.getId() == serverId).findFirst().orElse(null);
//		if(myServer == null) {
//			return null;
//		}
//		serverList.remove(myServer);//删除自己的服务器
//		for (int i = 1; i <= 10; i++) {
//			//每次10%的递增
//			KfExpeditionServer luck = findLuckFightServer(serverList, myServer, 0.1*i);
//			if(luck != null) {
//				return luck;
//			}
//		}
//		return null;
//	}
	
	//按照战力区间随机
	public static KfExpeditionServer findLuckFightServer(List<KfExpeditionServer> serverList,KfExpeditionServer myServer,double rate) {
		final long minCombat = myServer.getCombat() - (long)(myServer.getCombat()*rate);
		final long maxCombat = myServer.getCombat() + (long)(myServer.getCombat()*rate);
		List<KfExpeditionServer> fitList = serverList.stream().filter(e -> {
			return e.getCombat() >= minCombat && e.getCombat() <= maxCombat;
		}).collect(Collectors.toList());
		if(fitList.isEmpty()) {
			return null;
		}
		return RandomUtils.randomEle(fitList);
	}
	
//	public static List<KfExpeditionServer> findRangeList(List<KfExpeditionServer> serverList,int serverId,int rangeNum) {
//		List<KfExpeditionServer> resultList = Lists.newArrayList();
//		int myRank = -1;
//		for (int i = 0; i < serverList.size(); i++) {
//			if(serverList.get(i).getId() == serverId) {
//				myRank = i;break;
//			}
//		}
//		if(myRank < 0) {
//			return resultList;
//		}
//		boolean randomSpace = RandomUtils.randomInt(100) < 50;
//		int preIndex = myRank-1;
//		int nextIndex = myRank + 1;
//		int maxSize = serverList.size();
//		while (resultList.size() < rangeNum) {
//			if(preIndex < 0 && nextIndex >= maxSize) {
//				return resultList;//前后都找不到了
//			}
//			if(randomSpace) {
//				if(nextIndex < maxSize) {
//					resultList.add(serverList.get(nextIndex));
//				}
//				if(preIndex >= 0) {
//					resultList.add(serverList.get(preIndex));
//				}
//			}else{
//				if(preIndex >= 0) {
//					resultList.add(serverList.get(preIndex));
//				}
//				if(nextIndex < maxSize) {
//					resultList.add(serverList.get(nextIndex));
//				}
//			}
//			preIndex --;
//			nextIndex ++;
//		}
//		return resultList;
//	}
	
	public void calOldServerKf(int serverId) {
		try {
			int targetId = commValueConfig.getCommValue(CommonValueType.KfExpeditionOpenCity);
            kfObserverBiz.checkKfExpedition(serverId, targetId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		int range = 20;
		List<Integer> luckList = Lists.newArrayList();
		for (int i = 1; i < 226; i++) {
			luckList.add(i);
		}
		for (int i = 1; i < 226; i++) {
			int index = luckList.indexOf(i);
			if(index <= 0) {
				
			}
			int startIndex = Math.max((index/range)*range, 0);
			int endIndex = Math.min(startIndex+range, luckList.size());
			List<Integer> subList = luckList.subList(startIndex, endIndex);
			System.err.println(subList.contains(i)+":"+i+"="+GSONUtils.ToJSONString(subList));
		}
	}
}
