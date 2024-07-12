package com.hm.action.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.action.http.biz.HttpBiz;
import com.hm.action.http.gm.LvDistribution;
import com.hm.action.http.gm.OnlineStatistics;
import com.hm.action.item.ItemBiz;
import com.hm.action.login.biz.LoginBiz;
import com.hm.action.sys.SysFacade;
import com.hm.config.ResourceReader;
import com.hm.config.excel.ExcleConfig;
import com.hm.container.PlayerContainer;
import com.hm.db.OperateStaticsUtils;
import com.hm.db.PlayerUtils;
import com.hm.enums.*;
import com.hm.leaderboards.HdLeaderboardsService;
import com.hm.leaderboards.LeaderboardInfo;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.httpserver.handler.HttpSession;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.MongodDB;
import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.util.TimeUtils;
import com.hm.libcore.util.date.DateUtil;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.libcore.util.string.StringUtil;
import com.hm.model.item.Items;
import com.hm.model.page.PageBean;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;
import com.hm.servercontainer.idcode.IdCodeContainer;
import com.hm.util.ItemUtils;
import com.hm.util.PackageUtil;
import com.mongodb.BasicDBObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service("player.do")
public class PlayerManagerHandlerAction implements IHttpAction{
	@Resource
	private SysFacade sysFacade;
	@Resource
	private LoginBiz loginBiz;
	@Resource
	private HttpBiz httpBiz;
	@Resource
	private ItemBiz itemBiz;


	/**
	 * 获取服务器信息和白名单
	 *
	 * @param session
	 */
	public void reloadIpWhite(HttpSession session) {
		try {
			int serverId = getServerId(session);
			httpBiz.getWhiteList(serverId);
			session.Write("1");
		} catch (Exception e) {
			e.printStackTrace();
			session.Write("0");
		}

	}
	/**
	 * 禁言玩家
	 *
	 * @param session
	 */
	public void gagPlayer(HttpSession session) {
		try {
			long playerId = Long.parseLong(session.getParams("playerId"));
			String startTime = URLDecoder.decode(session.getParams("startTime"),"utf-8");
			String endTime = URLDecoder.decode(session.getParams("endTime"),"utf-8");
			Player player = PlayerUtils.getPlayer(playerId);
			if(null == player) {
				session.Write("0");
				return;
			}
			long closeStartTime =  TimeUtils.ParseTimeStampSafe(startTime).getTime();
			long closeEndTime =  TimeUtils.ParseTimeStampSafe(endTime).getTime();
			player.playerStatus().updateStatus(PlayerStatusType.NotChat,closeStartTime,closeEndTime,"",1);
			player.notifyObservers(ObservableEnum.NotChat);
			player.sendUserUpdateMsg();
			session.Write("1");
		} catch (Exception e) {
			e.printStackTrace();
			session.Write("0");
		}

	}

	/**
	 * 拉小黑屋
	 *
	 * @param session
	 */
	public void blackHome(HttpSession session) {
		try {
			long playerId = Long.parseLong(session.getParams("playerId"));
			String startTime = URLDecoder.decode(session.getParams("startTime"),"utf-8");
			String endTime = URLDecoder.decode(session.getParams("endTime"),"utf-8");
			Player player = PlayerUtils.getPlayer(playerId);
			if(null == player) {
				session.Write("0");
				return;
			}
			long closeStartTime =  TimeUtils.ParseTimeStampSafe(startTime).getTime();
			long closeEndTime =  TimeUtils.ParseTimeStampSafe(endTime).getTime();
			player.playerStatus().updateStatus(PlayerStatusType.BlackHome,closeStartTime,closeEndTime,"",1);
			//通知聊天服删除玩家聊天数据，同禁言一样处理
			player.notifyObservers(ObservableEnum.NotChat);
			player.sendUserUpdateMsg();
			session.Write("1");
		} catch (Exception e) {
			e.printStackTrace();
			session.Write("0");
		}

	}

	/**
	 * 解除禁言玩家
	 *
	 * @param session
	 */
	public void unGagPlayer(HttpSession session) {
		try {
			long playerId = Long.parseLong(session.getParams("playerId"));
			Player player = PlayerUtils.getPlayer(playerId);
			if(null == player) {
				session.Write("0");
				return;
			}
			player.playerStatus().updateStatus(PlayerStatusType.NotChat,0,0,"",0);
			player.sendUserUpdateMsg();
			session.Write("1");
		} catch (Exception e) {
			e.printStackTrace();
			session.Write("0");
		}
	}
	/**
	 * 解除小黑屋
	 *
	 * @param session
	 */
	public void unBlackHomes(HttpSession session) {
		try {
			String playerIds = URLDecoder.decode(session.getParams("ids"),"utf-8");
			for(String id:playerIds.split(",")){
				long playerId = Long.parseLong(id);
				Player player = PlayerUtils.getPlayer(playerId);
				if(player!=null){
					player.playerStatus().updateStatus(PlayerStatusType.BlackHome,0,0,"",0);
					player.notifyObservers(ObservableEnum.NotChat);
					player.sendUserUpdateMsg();
				}
			}
			session.Write("1");
		} catch (Exception e) {
			e.printStackTrace();
			session.Write("0");
		}
	}
	//解除多个玩家禁言
	public void unGagPlayers(HttpSession session){
		try {
			String playerIds = URLDecoder.decode(session.getParams("ids"),"utf-8");
			for(String id:playerIds.split(",")){
				long playerId = Long.parseLong(id);
				Player player = PlayerUtils.getPlayer(playerId);
				if(player!=null){
					player.playerStatus().updateStatus(PlayerStatusType.NotChat,0,0,"",0);
					player.notifyObservers(ObservableEnum.NotChat);
					player.sendUserUpdateMsg();
				}
			}
			session.Write("1");
		} catch (Exception e) {
			e.printStackTrace();
			session.Write("0");
		}
	}
	//根据id获取玩家
	public void getPlayerById(HttpSession session) {
		List<SimplePlayerInfo> results = Lists.newArrayList();
		try {
			Map<String, String> params = session.getParams();
			int id = Integer.parseInt(params.get("id"));
			int rank = Integer.parseInt(params.get("rank"));//0-不查询各榜单，1-查询榜单
			Player p = PlayerUtils.getPlayer(id);
			if(p != null) {
				SimplePlayerInfo simplePlayerInfo = new SimplePlayerInfo(p);
				if(rank==1){
				}
				results.add(simplePlayerInfo);
			}
			session.Write(GSONUtils.ToJSONString(results));
		} catch (Exception e) {
			session.Write(GSONUtils.ToJSONString(results));
		}
	}


	//根据id获取玩家
	public void getPlayerDetailById(HttpSession session) {
		Map<Integer, Object> resultMap = Maps.newHashMap();
		try {
			Map<String, String> params = session.getParams();
			int id = Integer.parseInt(params.get("id"));
			Player p = PlayerUtils.getPlayer(id);
			resultMap.put(-1, "success");
			for(PlayerDetailMsg temp :PlayerDetailMsg.values()) {
				resultMap.put(temp.getType(), temp.getData(p));
			}
			session.Write(GSONUtils.ToJSONString(resultMap));
		} catch (Exception e) {
			resultMap.put(-1, "error");
			session.Write(GSONUtils.ToJSONString(resultMap));
		}
	}
	//根据id获取玩家列表
	/*public void getPlayersByIds(HttpSession session) {
		try {
			Map<String, String> params = session.getParams();
			String idsStr = URLDecoder.decode(params.get("ids"),"utf-8");
			List<Integer> ids = StringUtil.splitStr2IntegerList(idsStr, ",");
			List<SimplePlayerInfo> results = Lists.newArrayList();
			Criteria criteria = new Criteria();
			criteria.andOperator(Criteria.where("_id").in(ids));
			Query query =new Query(criteria);
			query.with(Sort.by(new Order(Direction.DESC, "_id")));
			for (Player player : mongodDB.query(query, Player.class)) {
				results.add(new SimplePlayerInfo(player));
			}
			session.Write(GSONUtils.ToJSONString(results));
		} catch (Exception e) {
			session.Write("");
		}
	}*/
	/**
	 * kickPlayerOffLine:踢下线
	 * @author zqh
	 * @param session  使用说明
	 */
	public void kickPlayerOffLine(HttpSession session) {
		try {
			int pid = Integer.parseInt(session.getParams("playerId"));
			Player player = PlayerUtils.getPlayer(pid);
			if(null == player) {
				session.Write("0");
				return;
			}
			sysFacade.sendLeavePlayer(player, LeaveOnlineType.SERVER);
			session.Write("1");
		} catch (Exception e) {
			session.Write("0");
			log.error("踢玩家下线失败", e);
		}
	}
	/**
	 * kickPlayerOffLine:踢下线
	 * @author zqh
	 * @param session  使用说明
	 */
	public void kickPlayer(HttpSession session) {
		try {
			String idsStr = URLDecoder.decode(session.getParams("ids"),"utf-8");
			List<Integer> ids = StringUtil.splitStr2IntegerList(idsStr, ",");
			for(int palyerId:ids){
				Player player = PlayerUtils.getPlayer(palyerId);
				if(null != player&&player.isOnline()) {
					sysFacade.sendLeavePlayer(player, LeaveOnlineType.SERVER);
				}
			}
			session.Write("1");
		} catch (Exception e) {
			session.Write("0");
			log.error("踢玩家下线失败", e);
		}
	}

	/**
	 * 通过player id list 获取用户信息
	 *
	 * @param session
	 */
	public void getPlayerByIdList(HttpSession session) {
		List<SimplePlayerInfo> infos = Lists.newArrayList();
		try {
			int serverId = getServerId(session);
			MongodDB mongodDB = MongoUtils.getMongodDB(serverId);
			Map<String, String> params = session.getParams();
			String idsStr = URLDecoder.decode(session.getParams("ids"),"utf-8");
			int pageNo = Integer.parseInt(URLDecoder.decode(params.get("pageNo"), "utf-8"));
			int pageSize = Integer.parseInt(URLDecoder.decode(params.get("pageSize"), "utf-8"));
			List<Integer> ids = StringUtil.splitStr2IntegerList(idsStr, ",");
			Query query = Query.query(Criteria.where("_id").in(ids));
			long total = mongodDB.count(query, Player.class);
			List<Player> players = mongodDB.query(query.with(PageRequest.of(pageNo - 1, pageSize)), Player.class);
			for (Player player : players) {
				SimplePlayerInfo info = new SimplePlayerInfo(player);
				infos.add(info);
			}
			PageBean<SimplePlayerInfo> result = new PageBean<SimplePlayerInfo>(total, infos);
			session.Write(JSON.toJSONString(result));
		} catch (Exception e) {
			session.Write("0");
			e.printStackTrace();
		}
	}

	/**
	 * 获取 >= vip level 的用户信息
	 *
	 * @param session
	 */
	public void getPlayerByVipLv(HttpSession session) {
		List<SimplePlayerInfo> infos = Lists.newArrayList();
		try {
			int serverId = getServerId(session);
			MongodDB mongodDB = MongoUtils.getMongodDB(serverId);
			Map<String, String> params = session.getParams();
			int vipLv = Integer.parseInt(URLDecoder.decode(params.get("vipLv"), "utf-8"));
			int pageNo = Integer.parseInt(URLDecoder.decode(params.get("pageNo"), "utf-8"));
			int pageSize = Integer.parseInt(URLDecoder.decode(params.get("pageSize"), "utf-8"));
			Criteria criteria = new Criteria();
			criteria.andOperator(Criteria.where("playerVipInfo.vipLv").gte(vipLv));
			Query query = new Query(criteria);

			Sort sort = Sort.by(new Order(Direction.DESC, "playerVipInfo.vipLv"));
			long total = mongodDB.count(query, Player.class);
			List<Player> players = mongodDB.query(query.with(PageRequest.of(pageNo - 1, pageSize, sort)), Player.class);
			for (Player player : players) {
				SimplePlayerInfo info = new SimplePlayerInfo(player);
				infos.add(info);
			}
			PageBean<SimplePlayerInfo> result = new PageBean<SimplePlayerInfo>(total, infos);
			session.Write(JSON.toJSONString(result));
		} catch (Exception e) {
			session.Write("0");
			e.printStackTrace();
		}
	}

	/**
	 * 获取总充值金额 >= amount 的玩家列表
	 *
	 * @param session
	 */
	public void getPlayerByChargeAmount(HttpSession session) {
		List<SimplePlayerInfo> infos = Lists.newArrayList();
		try {
			int serverId = getServerId(session);
			MongodDB mongodDB = MongoUtils.getMongodDB(serverId);
			Map<String, String> params = session.getParams();
			int amount = Integer.parseInt(URLDecoder.decode(params.get("amount"), "utf-8"));
			int pageNo = Integer.parseInt(URLDecoder.decode(params.get("pageNo"), "utf-8"));
			int pageSize = Integer.parseInt(URLDecoder.decode(params.get("pageSize"), "utf-8"));
			Criteria criteria = new Criteria();
			String key = "playerStatistics.lifeStatistics." + StatisticsType.RECHARGE.getType();
			criteria.andOperator(Criteria.where(key).gte(amount));
			Query query = new Query(criteria);

			Sort sort = Sort.by(new Order(Direction.DESC, key));
			long total = mongodDB.count(query, Player.class);
			List<Player> players = mongodDB.query(query.with(PageRequest.of(pageNo - 1, pageSize, sort)), Player.class);
			for (Player player : players) {
				SimplePlayerInfo info = new SimplePlayerInfo(player);
				infos.add(info);
			}
			PageBean<SimplePlayerInfo> result = new PageBean<SimplePlayerInfo>(total, infos);
			session.Write(JSON.toJSONString(result));
		} catch (Exception e) {
			session.Write("0");
			e.printStackTrace();
		}
	}

	public void getPlayerByPlayerName(HttpSession session) {
		try {
			int serverId = getServerId(session);
			MongodDB mongodDB = MongoUtils.getMongodDB(serverId);
			Map<String, String> params = session.getParams();
			String name = URLDecoder.decode(params.get("name"), "utf-8");
			int searchBy = Integer.parseInt(URLDecoder.decode(params.get("searchBy"),"utf-8"));
			int pageNo = Integer.parseInt(params.get("pageNo"));
			int pageSize = Integer.parseInt(params.get("pageSize"));
			Criteria criteria = new Criteria();
			if(StringUtils.isNotBlank(name)){
				//criteria.orOperator(Criteria.where("name").is(name)).orOperator(Criteria.where("_id").is(name)).orOperator(Criteria.where("name").regex(name));
				switch (searchBy) {
					case 1:
						criteria.andOperator(Criteria.where("name").is(name));
						break;
					case 2:
						criteria.andOperator(Criteria.where("_id").is(Integer.parseInt(name)));
						break;
					case 3:
						criteria.andOperator(Criteria.where("name").regex(name));
						break;
					default:
				}
			}
			Query query =new Query(criteria);
			long total = mongodDB.count(query, Player.class);
			List<Player> players = mongodDB.query(query.with(PageRequest.of(pageNo-1, pageSize)), Player.class);
			List<SimplePlayerInfo> infos = players.stream().map(t -> new SimplePlayerInfo(t)).collect(Collectors.toList());
			PageBean<SimplePlayerInfo> result = new PageBean<SimplePlayerInfo>(total,infos);
			session.Write(JSONObject.toJSONString(result));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void getOnlinePlayer(HttpSession session) {
		List<SimplePlayerInfo> infos = Lists.newArrayList();
		try {
			int serverId = getServerId(session);
			Map<String, String> params = session.getParams();
			String name = URLDecoder.decode(params.get("name"), "utf-8");
			int pageSize = Integer.parseInt(params.get("pageSize"));
			int pageNum = Integer.parseInt(params.get("pageNo"));
			List<Player> onlineplayers = PlayerContainer.getOnlinePlayersByServerId(serverId);
			infos = onlineplayers.stream().filter(t -> StringUtils.isBlank(name)||name.equals(t.getName())||name.equals(t.getId().toString())).map(t -> new SimplePlayerInfo(t)).collect(Collectors.toList());
			PageBean<SimplePlayerInfo> pageBean = new PageBean<SimplePlayerInfo>(infos.size(),infos.subList(pageSize*(pageNum-1), Math.min(pageSize*pageNum,infos.size()))) ;
			session.Write(JSONObject.toJSONString(pageBean));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//获取排行榜信息(只能按全服和国家查，区域后续有需要补充)
	public void getRank(HttpSession session) {
		try {
			int serverId = getServerId(session);
			Map<String, String> params = session.getParams();
			int type = Integer.parseInt(params.get("rankType"));
			int pageNo = Integer.parseInt(params.get("pageNo"));
			int pageSize = Integer.parseInt(params.get("pageSize"));
			String name = URLDecoder.decode(params.get("name"), "utf-8");
			int searchBy = params.containsKey("searchBy")?Integer.parseInt(params.get("searchBy")):0;//查询方式 1-id,2-name,3-name模糊查询
			RankType rankType = RankType.getTypeByIndex(type);
			PageBean<SimplePlayerInfo> page = null;
			List<SimplePlayerInfo> infos = Lists.newArrayList();
			if(rankType!=null&&StringUtils.isBlank(name)){//排行榜查询
				page = getRank(serverId,rankType, pageNo, pageSize);
			}else{//数据库排行查询
				page = getFromDb(serverId,type, name, searchBy, pageNo,pageSize, params);
			}
			String json = JSONObject.toJSONString(page);
			session.Write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//排行榜查
	public PageBean<SimplePlayerInfo> getRank(int serverId,RankType rankType,int pageNo,int pageSize){
		//组装查询的排行榜名字
		String rankName = rankType.getRankName();
		long total  =  HdLeaderboardsService.getInstance().getRankTotal(serverId,rankName);//排行榜总条数
		pageNo = Math.max(1, pageNo);
		//最多50条
		List<LeaderboardInfo> rankList = HdLeaderboardsService.getInstance().getGameRankWithPage(serverId,rankName, pageNo, Math.min(50, pageSize));
		List<SimplePlayerInfo> infos = rankList.stream().filter(t->t!=null).map(t ->{
			long tempPlayerId = t.getIntId();
			if(tempPlayerId<=0) {
				SimplePlayerInfo simplePlayerInfo = new SimplePlayerInfo();
				simplePlayerInfo.setName("NPC:"+t.getId());
				return simplePlayerInfo;
			}else {
				Player player = PlayerUtils.getPlayer(tempPlayerId);
				if(null==player) {
					SimplePlayerInfo simplePlayerInfo = new SimplePlayerInfo();
					simplePlayerInfo.setName("ERROR:"+tempPlayerId);
					return simplePlayerInfo;
				}
				SimplePlayerInfo simplePlayerInfo = new SimplePlayerInfo(player);
				List<String> rankNameMap = Lists.newArrayList(RankType.Combat.getRankName(player),RankType.Arena.getRankName(player));
				Map<String,Integer> rankMap = HdLeaderboardsService.getInstance().getPlayerRankByList(serverId,player.getId(), rankNameMap);
				simplePlayerInfo.setServerRank(rankMap.get(RankType.Combat.getRankName(player)));
				simplePlayerInfo.setArenaRank(rankMap.get(RankType.Arena.getRankName(player)));
				return simplePlayerInfo;
			}
		}).collect(Collectors.toList());
		PageBean<SimplePlayerInfo> result = new PageBean<SimplePlayerInfo>(total,infos);
		return result;
	}
	//本地数据库榜查
	public PageBean<SimplePlayerInfo> getFromDb(int serverId,int rankType,String name,int searchBy,int pageNo,int pageSize, Map<String, String> params){
		MongodDB mongo = MongoUtils.getMongodDB(serverId);
		Criteria criteria = new Criteria();
		if(StringUtils.isNotBlank(name)){
			switch (searchBy) {
				case 1:
					criteria.andOperator(Criteria.where("name").is(name));
					break;
				case 2:
					int id = Integer.parseInt(name.trim());
					criteria.andOperator(Criteria.where("_id").is(id));
					break;
				case 3:
					criteria.andOperator(Criteria.where("name").regex(name));
					break;
				default:
			}
		}
		Query query =new Query();
		Sort sort = Sort.unsorted();
		switch(rankType){
			case 1000001://玩家等级排名  这个数字，是跟gm，playerlist.html对应的，防止游戏中排行榜编号重复
				sort=Sort.by(new Order(Direction.DESC, "playerCommander.militaryLv"));
				break;
			case 1000002://玩家vip等级  这个数字，是跟gm，playerlist.html对应的，防止游戏中排行榜编号重复
				sort=Sort.by(new Order(Direction.DESC, "playerVipInfo.vipLv"));
				break;
			default://默认按id倒叙排列
				sort = Sort.by(new Order(Direction.DESC, "playerBaseInfo.createDate"));
				break;
		}
		if(!"-1".equals(params.get("channelIds"))) {
			try {
				List<Integer> tmepChannel = null;
				tmepChannel = StringUtil.splitStr2IntegerList(URLDecoder.decode(params.get("channelIds"),"utf-8"),",");
				criteria.and("channelId").in(tmepChannel);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		query.addCriteria(criteria);
		long total = mongo.count(query, Player.class);
		//这分页居然是从0页开始的
		List<Player> players = mongo.query(query.with(PageRequest.of(pageNo-1, pageSize,sort)), Player.class);
		List<SimplePlayerInfo> infos = players.stream().filter(t -> t!=null).map(t -> new SimplePlayerInfo(PlayerUtils.getOnlinePlayer(t.getId())==null?t:PlayerUtils.getOnlinePlayer(t.getId()))).collect(Collectors.toList());
		PageBean<SimplePlayerInfo> result = new PageBean<SimplePlayerInfo>(total,infos);
		return result;
	}

	public void getOnlineNum(HttpSession session){
		try {
			int serverId = getServerId(session);
			String json = JSONObject.toJSONString(PlayerContainer.getOnlinePlayersByServerId(serverId).size());
			session.Write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//获取所有玩家基本信息
	public void getAllPlayers(HttpSession session){
		try {
			int serverId = getServerId(session);
			List<Player> players = PlayerUtils.getPlayerSimple(serverId,new Criteria(),Sort.unsorted(),0,0);
			List<SimplePlayerInfo> simplePlayers = players.stream().filter(t -> t!=null).map(t -> new SimplePlayerInfo(t)).collect(Collectors.toList());
			session.Write(JSON.toJSONString(simplePlayers));
		} catch (Exception e) {
			session.Write("0");
			e.printStackTrace();
		}


	}
	private List<String> getAllExcelConfig(String name) {
		Set<String> set = new HashSet<String>();
		List<String> classNames = PackageUtil.getClassName("com.hm.config.excel.temlate");
		if (classNames != null) {
			try {
				for (String className : classNames) {
					Class<?> temp = Class.forName(className);
					if (temp.isAnnotationPresent(FileConfig.class)) {
						FileConfig fileConfig = temp.getAnnotation(FileConfig.class);
						String excelName = fileConfig.value();
						if(StringUtils.isBlank(name)||name.equals(excelName)){
							set.add(fileConfig.value());
						}
					}
				}
			} catch (Exception ex) {
				System.exit(0);
			}
		}
		return Lists.newArrayList(set);
	}
	//获取配置文件
	public void getExcelConfig(HttpSession session){
		try {
			Map<String, String> params = session.getParams();
			int pageSize = Integer.parseInt(params.get("pageSize"));
			int pageNum = Integer.parseInt(params.get("pageNo"));
			String name = params.get("name");
			List<String> excelConfigStrings = getAllExcelConfig(name);
			Collections.sort(excelConfigStrings);
			PageBean<String> pageBean = new PageBean<String>(excelConfigStrings.size(),excelConfigStrings.subList(pageSize*(pageNum-1), Math.min(pageSize*pageNum,excelConfigStrings.size()))) ;
			String json = JSONObject.toJSONString(pageBean);
			session.Write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//重新加载指定配置文件
	public void reloadExcelConfig(HttpSession session){
		try {
			Map<String, String> params = session.getParams();
			String names = URLDecoder.decode(params.get("excelNames"),"utf-8");
			List<String> excelNames = StringUtil.splitStr2StrList(names, ",");

			List<String> fails = Lists.newArrayList(excelNames);
			//重新下载配置文件
			ResourceReader.getInstance().downloadAllProp();
			Map<String, ExcleConfig> beanMap = SpringUtil.getBeanMap(ExcleConfig.class);

			for (String excelName : excelNames) {
				try {
					for (ExcleConfig excleConfig : beanMap.values()) {
						if (excleConfig.getFileList().contains(excelName)) {
							excleConfig.loadConfig();
							log.error("加载配置文件:"+ excelName);
							fails.remove(excelName);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.error("加载配置文件失败: "+ excelName);
				}
			}
			String json = JSONObject.toJSONString(fails);
			session.Write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//获取等级分布
	public void getLvDistribution(HttpSession session) {
		try {
			int serverId = getServerId(session);
			Map<String, String> params = session.getParams();
			String time = params.get("time");
			LvDistribution lvDistributions = OperateStaticsUtils.getLvDistribution(serverId,time);
			//PageBean<LvDistribution> pageBean = new PageBean<LvDistribution>(lvDistributions.size(),lvDistributions.subList(pageSize*(pageNum-1), Math.min(pageSize*pageNum,lvDistributions.size()))) ;
			session.Write(JSONObject.toJSONString(lvDistributions.getLvMap()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void getOnlineDistribution(HttpSession session){
		try {
			int serverId = getServerId(session);
			MongodDB mongodDB = MongoUtils.getMongodDB(serverId);
			Map<String, String> params = session.getParams();
			int pageSize = Integer.parseInt(params.get("pageSize"));
			int pageNum = Integer.parseInt(params.get("pageNo"));
			Sort.by(new Order(Direction.DESC, "_id"));
			Query query = new Query();
			long total = mongodDB.count(query, LvDistribution.class);
			List<LvDistribution> lvDistributions = mongodDB.query(query.with(PageRequest.of(pageNum-1, pageSize,Sort.by(new Order(Direction.DESC, "_id")))), LvDistribution.class);
			PageBean<LvDistribution> result = new PageBean<LvDistribution>(total,lvDistributions);
			session.Write(JSONObject.toJSONString(result));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void getOnlineStatistics(HttpSession session){
		try {
			int serverId = getServerId(session);
			Map<String, String> params = session.getParams();
			String time = params.get("time");
			OnlineStatistics onlineStatistics = OperateStaticsUtils.getOnlineNumStatics(serverId,time);
			session.Write(JSONObject.toJSONString(onlineStatistics.getOnlineInfo()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getTankStarData(HttpSession session){
		try {
			int serverId = getServerId(session);
			Map<String, String> params = session.getParams();
			String tankId = params.get("tankId");
			//查询条件
			Criteria criteria = Criteria.where("playerTank.tankMap."+tankId).exists(true)
					.andOperator(Criteria.where("playerBaseInfo.lastLoginDate")
							.gte(new Date(DateUtil.getProDays(new Date(), 7))));
			//分组条件
			Field vip = Fields.field("vipLv","playerVipInfo.vipLv");
			Field tankStar = Fields.field("star","playerTank.tankMap."+tankId+".star");
			Fields fields = Fields.from(vip,tankStar);

			Map<String, Object> map = Maps.newHashMap();
			map.put("totalCount", getPlayerCount(serverId));
			map.put("data", aggregatePlayer(serverId, criteria, fields));
			session.Write(JSONObject.toJSONString(map));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void getTankDriverData(HttpSession session){
		try {
			int serverId = getServerId(session);
			Map<String, String> params = session.getParams();
			String tankId = params.get("tankId");

			Criteria criteria = Criteria.where("playerTank.tankMap."+tankId).exists(true)
					.andOperator(Criteria.where("playerBaseInfo.lastLoginDate")
							.gte(new Date(DateUtil.getProDays(new Date(), 7))));
			Field vip = Fields.field("vipLv","playerVipInfo.vipLv");
			Field tankDriver = Fields.field("driverLv","playerTank.tankMap."+tankId+".driver.lv");
			Fields fields = Fields.from(vip,tankDriver);

			Map<String, Object> map = Maps.newHashMap();
			map.put("totalCount", getPlayerCount(serverId));
			map.put("data", aggregatePlayer(serverId, criteria, fields));
			session.Write(JSONObject.toJSONString(map));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 聚合查询
	 *
	 * @author yanpeng
	 * @param serverId
	 * @param query
	 * @param fields
	 * @return
	 *
	 */
	private List<BasicDBObject> aggregatePlayer(int serverId, Criteria query, Fields fields){
		MongoTemplate template = MongoUtils.getServerMongoTemplate(serverId);
		List<AggregationOperation> operations = Lists.newArrayList();
		operations.add(Aggregation.match(query));
		operations.add(Aggregation.group(fields).count().as("count"));
		Aggregation aggregation = Aggregation.newAggregation(operations);
		AggregationResults<BasicDBObject> results = template.aggregate(aggregation, "player", BasicDBObject.class);
		return results.getMappedResults();
	}

	/**
	 * 查询活跃用户数(7日内登陆)
	 *
	 * @author yanpeng
	 * @param serverId
	 * @return
	 *
	 */
	private int getPlayerCount(int serverId){
		MongoTemplate template = MongoUtils.getServerMongoTemplate(serverId);
		Query query = Query.query(Criteria.where("playerBaseInfo.lastLoginDate").gte(new Date(DateUtil.getProDays(new Date(), 7))));
		return template.find(query,Player.class).size();
	}

	/**
	 * 直播消费
	 *
	 * @author xjt
	 * @return
	 */
	public void spendItems(HttpSession session){
		try {
			long playerId = Long.parseLong(session.getParams("playerId"));
			Player player = PlayerUtils.getPlayer(playerId);
			String orderId = URLDecoder.decode(session.getParams("orderId"), "utf-8");
			String itemStr = URLDecoder.decode(session.getParams("itemStr"), "utf-8");
			String goodsInfo = URLDecoder.decode(session.getParams("goodsInfo"), "utf-8");
			synchronized (player.playerVideoOrder()) {
				if(player.playerVideoOrder().isContainsOrder(orderId)) {
					//重复消费
					session.Write("-1");
					return;
				}
				List<Items> costs = ItemUtils.str2ItemList(itemStr, ",", ":");
				if (!itemBiz.checkItemEnoughAndSpend(player, costs, LogType.Viedo.value(goodsInfo))) {
					//货币不足
					session.Write("0");
					return;
				}
				player.playerVideoOrder().addOrderId(orderId);
				player.sendUserUpdateMsg();
			}
			//消费成功
			session.Write("1");
		} catch (Exception e) {
			//请求出错
			session.Write("-2");
			e.printStackTrace();
		}

	}

	/**
	 * 解除用户唯一码
	 *
	 * @author yanpeng
	 * @return
	 */
	public void unBindIdCode(HttpSession session){
		try {
			long playerId = Long.parseLong(session.getParams("playerId"));
			Player player = PlayerUtils.getPlayer(playerId);
			//解绑唯一码
			IdCodeContainer.of(player).unBindIdCode(player);
			//清空玩家身上的唯一码
			player.unBindIdCode();
			player.saveDB();
			player.notifyObservers(ObservableEnum.BindIdCode, player.getIdCode());
			session.Write("1");
		} catch (Exception e) {
			session.Write("0");
			e.printStackTrace();
		}

	}


}
