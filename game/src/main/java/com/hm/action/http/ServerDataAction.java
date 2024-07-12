package com.hm.action.http;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.hm.action.activity.ActivityBiz;
import com.hm.action.guild.biz.GuildBiz;
import com.hm.action.wx.WXBiz;
import com.hm.chat.InnerChatFacade;
import com.hm.db.PlayerUtils;
import com.hm.enums.RankType;
import com.hm.leaderboards.HdLeaderboardsService;
import com.hm.leaderboards.LeaderboardInfo;
import com.hm.libcore.httpserver.handler.HttpSession;
import com.hm.libcore.util.SensitiveWordUtil;
import com.hm.model.player.Player;
import com.hm.model.serverpublic.ServerData;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.observer.ObservableEnum;
import com.hm.redis.type.RedisTypeEnum;
import com.hm.redis.util.RedisUtil;
import com.hm.server.GameServerManager;
import com.hm.servercontainer.idcode.IdCodeContainer;
import com.hm.servercontainer.idcode.IdCodeInfo;
import com.hm.servercontainer.idcode.IdCodeItemContainer;
import com.hm.util.HFUtils;
import com.hm.util.NameUtils;
import com.hm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import com.hm.libcore.annotation.Action;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ClassName: ServerDataAction. <br/>  
 * Function: gm工具，获取服务器的title（称号）信息. <br/>  
 * date: 2019年5月28日 下午4:08:45 <br/>  
 *  
 * @author zxj  
 * @version
 */
@Slf4j
@Service("serverdata.do")
public class ServerDataAction {
	@Resource
    private ActivityBiz activityBiz;
	@Resource
	private InnerChatFacade innerChatFacade;

	@Resource
	private GuildBiz guildBiz;
	@Resource
	private WXBiz wXBiz;
	

	public void getServerTitle(HttpSession session) {
		if(!session.getParams().containsKey("serverid") || StringUtil.isNullOrEmpty(session.getParams("serverid"))) {
			log.error(String.format("出入参数异常：%s", JSON.toJSONString(session.getParams())));
			session.Write("error");
		}
		int serverId = Integer.parseInt(session.getParams("serverid"));
		ServerData serverData = ServerDataManager.getIntance().getServerData(serverId);
		if(null==serverData) {
			log.error(String.format("出入参数错误，没有找到服务器：%s", JSON.toJSONString(session.getParams())));
			session.Write("error");
		}
		Map<Integer, Long> allTitle = serverData.getTitleData().getAllTitle();
		session.Write(JSON.toJSONString(allTitle));
	}
	
	public void sendVipPlayer(HttpSession session) {
		long playerId = Long.parseLong(session.getParams("playId"));
		Player player = PlayerUtils.getPlayer(playerId);
		if(player != null) { 
			player.getPlayerVipInfo().setVipPlayer(true);
			player.sendUserUpdateMsg();
			activityBiz.sendActivityList(player);
			session.Write("1");
			return;
		}
		session.Write("0");
	}
	
	public void changePlayerName(HttpSession session) {
		long playerId = Long.parseLong(session.getParams("playerId"));
		String name = session.getParams("name");
		Player player = PlayerUtils.getPlayer(playerId);
		if(player != null) {
			name = NameUtils.nameClearBlank(name);
			if (SensitiveWordUtil.contains(name)) {
				session.Write("0");
				return;
			}
			name = HFUtils.checkName(player, name);// 合服服务器用户名字自动加上服务器前缀
			if (!PlayerUtils.checkName(name, player.getServerId())) {
				session.Write("0");
				return;
			}
			String oldName = player.getName();
			player.changeName(name);
			player.notifyObservers(ObservableEnum.ChangeName, oldName);
			player.sendUserUpdateMsg();
			session.Write("1");
			return;
		}
		session.Write("0");
	}
	
	public void sendSystemRedPacket(HttpSession session) {
		try {
			int serverId = Integer.parseInt(session.getParams("serverId"));
			int campId = Integer.parseInt(session.getParams("campId"));
			int total = Integer.parseInt(session.getParams("total"));
			int count = Integer.parseInt(session.getParams("count"));
			String title = URLDecoder.decode(session.getParams("title"), "utf-8");
			String content = URLDecoder.decode(session.getParams("content"), "utf-8");
			if(total <= 0 || count <= 0 || serverId <= 0) {
				session.Write("0");
				return;
			}
			innerChatFacade.saveAndSendSystemRedPacket(serverId, campId, total, count,title,content);
			session.Write("succ");
			return;
		} catch (Exception e) {
		}
		session.Write("fail");
		return;
	}
	//根据类型获取玩家填写的收货地址
	//http://192.144.238.206:6103/?action=serverdata.do&m=getRankRewardAddr&addrType=RechargeCarnivalAddr&rankType=61&start=1&end=1000&activityId=act_1387&extra=1_300,7001_7299,301_899,1100_1999;2001_2999,8001_8100,6001_6999,4001_4999,10001_10999,5001_5999,7301_7999
	public void getRankRewardAddr(HttpSession session) {
		try {
			String addrType = session.getParams("addrType");
			int rankType = Integer.parseInt(session.getParams("rankType"));
			int start = Integer.parseInt(session.getParams("start"));
			int end = Integer.parseInt(session.getParams("end"));
			String extra = StrUtil.isNotBlank(session.getParams("extra"))?session.getParams("extra"):"0";
			String activityId = session.getParams("activityId");
			RedisTypeEnum redisType = RedisTypeEnum.getRedisType(addrType);
			RankType type = RankType.getTypeByIndex(rankType);
			List<String> line = Lists.newArrayList();
			List<Integer> kfIds = Arrays.stream(extra.split(";")).map(t ->Integer.parseInt(t.split("_")[0])).collect(Collectors.toList());
			for(int kfId:kfIds){
				line.add("跨服分组id========="+kfId+"============对应的排行榜数据开始");
				List<LeaderboardInfo> list = HdLeaderboardsService.getInstance().getGameRank(kfId,type.getRankName()+"_"+activityId, start,end);
				for (LeaderboardInfo info : list) {
					long playerId = info.getIntId();
					int rank = info.getRank();
					String json = redisType.get(String.valueOf(playerId));
					String result = json != null?json:"未填写";
					line.add("playerId:"+playerId+",排名:"+rank+",地址:"+result);
				}
				line.add("跨服分组id========="+kfId+"============对应的排行榜数据结束");
				line.add("====================================================");
				line.add("====================================================");
				line.add("====================================================");
			}
			session.Write(line.toString());
			return;
		} catch (Exception e) {
		}
		return;
	}
	
	//更改唯一码过滤开关
	//http://192.168.1.246:8302/?action=serverdata.do&m=changeIdCodeFilterSwitch&idCodeSwitch=false&ipSwitch=true&loginSwitch=false
	//http://192.168.1.246:8180/GameHotWeb/showIdCodeSwitch.jsp
	//http://192.144.160.155:6101/?action=serverdata.do&m=changeIdCodeFilterSwitch&idCodeSwitch=true&ipSwitch=true&loginSwitch=true

	
	//清理唯一码信息
	public void clearIdCodeInfo(HttpSession session) {
		try {
			int serverId = Integer.parseInt(session.getParams("serverId"));
			IdCodeItemContainer  container =  IdCodeContainer.of(serverId);
			List<IdCodeInfo> idCodeInfos = container.getAllIdCodeInfos();
			for(IdCodeInfo idCodeInfo:idCodeInfos){
				List<Long> playerIds = Lists.newArrayList(idCodeInfo.getPlayerMap().keySet());
				if(CollUtil.isEmpty(playerIds)){
					//该唯一码信息中没有任何玩家信息则从container和数据库中删除
					container.delIdCodeInfo(idCodeInfo);
					continue;
				}
				//找到该唯一码中不存在的用户
				List<Long> delIds = playerIds.stream().filter(id -> !RedisUtil.isExitPlayer(id)).collect(Collectors.toList());
				if(!CollUtil.isEmpty(delIds)){
					//该唯一码信息中有玩家已被删除(合服清用户)
					idCodeInfo.del(delIds);
					idCodeInfo.saveDB();
					//删除后再次检查该码中是否还有用户
					if(idCodeInfo.getPlayerMap().size()<=0){
						//该唯一码信息中没有任何玩家信息则从container和数据库中删除
						container.delIdCodeInfo(idCodeInfo);
					}
				}
			}
			session.Write("清理唯一码成功,serverId----"+serverId);
			return;
		} catch (Exception e) {
			session.Write("fail");
		}
		return;
	}
	
	public void resetPlayerArmsState(HttpSession session){
		for(int serverId:GameServerManager.getInstance().getServerIdList()){
			int maxLv = guildBiz.getGuildLvMax(serverId);
			if(maxLv<5){
				//最大等级小于5级则将所有开启武器功能的玩家状态置为0
				List<Long> playerIds = PlayerUtils.getPlayerArmsError(serverId);
				if(playerIds.size()>0){
					for(long id:playerIds){
						Player player = PlayerUtils.getPlayer(id);
						player.playerArms().setState(0);
						player.sendUserUpdateMsg();
					}
				}
			}
		}
		session.Write("succ");
		
	}

//	public void versionChange(HttpSession session) {
//		for (Integer serverId : GameServerManager.getInstance().getServerIdList()) {
//			wXBiz.checkPlayerSub(serverId, WXSubsType.VersionChange);
//		}
//		session.Write("succ");
//	}
}
