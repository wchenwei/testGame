package com.hm.model.serverpublic;

import com.hm.handler.GamePlayerMsgForwardProxy;
import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.libcore.db.mongo.DBEntity;
import com.hm.libcore.msg.JsonMsg;
import com.hm.container.PlayerContainer;
import com.hm.message.MessageComm;
import com.hm.message.ServerMsgType;
import com.hm.server.GameServerType;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 单服务器的公共数据
 * @author siyunlong  
 * @date 2018年10月15日 下午5:29:03 
 * @version V1.0
 */
public class ServerData extends DBEntity<String>{
	private ServerStatistics serverStatistics;
	private ServerOpenData serverOpenData;
	//服务器功能列表
	private ServerFunction serverFunction;

	private ServerTrumpArenaData serverTrumpArenaData;
	//消除
	private ServerEliminate serverEliminate;

	private TitleData titleData;
	private ServerKfData serverKfData;
	private ServerMergeData serverMergeData;
	//总统特权
	private ServerPresidentPower serverPresidentPower;
	
	private ServerWorldLvData serverWorldLvData = new ServerWorldLvData();
	private ServerAllianceData serverAllianceData = new ServerAllianceData();
	private ServerGuildTaskData serverGuildTaskData = new ServerGuildTaskData();
	private ServerWorldBuildData serverWorldBuildData = new ServerWorldBuildData();
	private ServerKefuData serverKefuData = new ServerKefuData();
	private ServerActData serverActData = new ServerActData();//服务器活动数据

	private GameServerType gameServerType;//服务器游戏类型

	private ServerCampConvertData serverCampConvertData;
	private ServerCampData serverCampData = new ServerCampData();

	public ServerData() {
	}
	public ServerData(int serverId) {
		setId(ServerDataDbUtils.ID);
		setServerId(serverId);
	}
	
	public ServerStatistics getServerStatistics() {
		if(this.serverStatistics == null) this.serverStatistics = new ServerStatistics();
		this.serverStatistics.setContext(this);
		return serverStatistics;
	}
	public ServerOpenData getServerOpenData() {
		if(this.serverOpenData == null) this.serverOpenData = new ServerOpenData();
		this.serverOpenData.setContext(this);
		return serverOpenData;
	}

	public ServerTrumpArenaData getServerTrumpArenaData() {
		if(this.serverTrumpArenaData == null) this.serverTrumpArenaData = new ServerTrumpArenaData();
		this.serverTrumpArenaData.setContext(this);
		return serverTrumpArenaData;
	}

	public ServerFunction getServerFunction() {
		if(this.serverFunction == null) this.serverFunction = new ServerFunction();
		this.serverFunction.setContext(this);
		return serverFunction;
	}
	public ServerKfData getServerKfData() {
		if(this.serverKfData == null) this.serverKfData = new ServerKfData();
		this.serverKfData.setContext(this);
		return serverKfData;
	}
	public TitleData getTitleData() {
		if(this.titleData == null) this.titleData = new TitleData();
		this.titleData.setContext(this);
		return titleData;
	}

	public ServerMergeData getServerMergeData() {
		if(this.serverMergeData == null) this.serverMergeData = new ServerMergeData();
		this.serverMergeData.setContext(this);
		return serverMergeData;
	}
	
	public ServerPresidentPower getServerPresidentPower() {
		if(this.serverPresidentPower == null) this.serverPresidentPower = new ServerPresidentPower();
		this.serverPresidentPower.setContext(this);
		return serverPresidentPower;
	}
	public ServerWorldLvData getServerWorldLvData() {
		if(this.serverWorldLvData == null) this.serverWorldLvData = new ServerWorldLvData();
		this.serverWorldLvData.setContext(this);
		return serverWorldLvData;
	}
	public ServerAllianceData getServerAllianceData() {
		this.serverAllianceData.setContext(this);
		return serverAllianceData;
	}
	public ServerGuildTaskData getServerGuildTaskData() {
		this.serverGuildTaskData.setContext(this);
		return serverGuildTaskData;
	}

	public ServerWorldBuildData getServerWorldBuildData() {
		this.serverWorldBuildData.setContext(this);
		return serverWorldBuildData;
	}

	public ServerCampData getServerCampData() {
		this.serverCampData.setContext(this);
		return serverCampData;
	}

	public ServerKefuData getServerKefuData() {
		return serverKefuData;
	}

	public ServerActData getServerActData() {
		return serverActData;
	}

	public ServerEliminate getServerEliminate() {
		if(this.serverEliminate == null) this.serverEliminate = new ServerEliminate();
		this.serverEliminate.setContext(this);
		return serverEliminate;
	}

	public void fillMsg(JsonMsg serverMsg) {
		serverMsg.addProperty("openDay", getServerOpenData().getOpenDay());
		serverMsg.addProperty("serverFunction",getServerFunction());
		serverMsg.addProperty("serverTrumpArenaData",getServerTrumpArenaData());
		serverMsg.addProperty("serverKfData",getServerKfData());
		serverMsg.addProperty("ServerPresidentPower",getServerPresidentPower());
		serverMsg.addProperty("serverWorldLvData",getServerWorldLvData());
		serverMsg.addProperty("serverAllianceData",getServerAllianceData());
		serverMsg.addProperty("serverGuildTaskData",getServerGuildTaskData());
		serverMsg.addProperty("serverWorldBuildData",getServerWorldBuildData());
		serverMsg.addProperty("serverKefuData",this.serverKefuData);
		serverMsg.addProperty("campConvertData",getCampConvertData());
		serverMsg.addProperty("serverActData", serverActData);
		serverMsg.addProperty("serverCampData", getServerCampData());
		serverMsg.addProperty("serverHFTimes", getServerMergeData().getMergeTimes());
		getServerStatistics().fillMsg(serverMsg);
	}
	
	public void loadServerData() {
		loadServerKfUrl();
		//加载服务器每日标识
		getServerStatistics().loadServerDayMark();
		//加载跨服数据
		getServerKfData().loadHourCheck();
		//阵营荣誉标识
		getServerCampData().loadFirst();
		//域名IP对应信息
		IpToDomain.loadData();
		//保存redis数据库
		ServerRedisData serverRedisData = ServerRedisDataUtils.getServerRedisDataByServerId(getServerId());
		serverRedisData.load(this);
		serverRedisData.saveDB();
		ServerRedisDataUtils.addServerData(getServerId(),serverRedisData);
	}
	
	public void loadServerKfUrl() {
		String[] mineInfo = GameServerType.getKfMineUrl(getServerId()).split("#");
		this.gameServerType = GameServerType.getGameServerTypeByServerId(getServerId());
		System.err.println("跨服资源战地址:"+GSONUtils.ToJSONString(mineInfo));
		getServerKfData().setKfMineIp(mineInfo[0]+":"+mineInfo[1]);
		getServerKfData().setKfMinehttpIp(mineInfo[0]+":"+mineInfo[2]);
		getServerKfData().setKfPkUrl(GameServerType.loadKfPkUrl(gameServerType));
		String kfextortUrl = GameServerType.loadExtortUrl(gameServerType);
		System.err.println("跨服征讨地址:"+kfextortUrl);
		getServerKfData().setKfExtortUrl(kfextortUrl);
		//加载客服信息
		this.serverKefuData.loadData(this);

		//注册
		GamePlayerMsgForwardProxy gameProxy = SpringUtil.getBean(GamePlayerMsgForwardProxy.class);
		gameProxy.register(ServerMsgType.KFRank,getServerId(),this.getServerKfData().getKfPkUrl());
	}
	
	/**
	 * 广播服务器数据库变化
	 */
	public void broadServerUpdate() {
		JsonMsg serverMsg = new JsonMsg(MessageComm.S2C_ServerDataChange);
		fillMsg(serverMsg);
		PlayerContainer.broadPlayer(getServerId(), serverMsg);
	}
	
	public GameServerType getGameServerType() {
		return gameServerType;
	}
	public void save() {
		ServerDataDbUtils.saveOrUpdate(this);
	}

	public ServerCampConvertData getServerCampConvertData() {
		if (this.serverCampConvertData == null) this.serverCampConvertData = new ServerCampConvertData();
		this.serverCampConvertData.setContext(this);
		if (this.serverCampConvertData.initStartTime(getServerId())) {
		    save();
		}
		return this.serverCampConvertData;
	}

	private Map getCampConvertData() {
		ServerCampConvertData convertData = getServerCampConvertData();
		Map<String, Object> map = new HashMap<>();
		map.put("campRank", convertData.getCampRank());
        map.put("openEndTime", convertData.getOpenEndTime());
		map.put("endTime", convertData.getEndTime());
		map.put("mask", convertData.getMask());
		return map;
	}
}
