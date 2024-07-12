package com.hm.main;

import cn.hutool.core.date.DateUtil;
import com.hg.mq.enums.MqGroupIdEnum;
import com.hm.action.guild.biz.GuildFlagManger;
import com.hm.action.http.gm.GmMailManager;
import com.hm.action.kf.kfworldwar.sum.KfWorldWarConf;
import com.hm.action.loginmsg.LoginMsgBiz;
import com.hm.action.player.IdCodeFilterBiz;
import com.hm.action.player.RemovePlayerBiz;
import com.hm.action.wx.WXUtils;
import com.hm.actor.ActorDispatcherType;
import com.hm.actor.OnceQueueTaskType;
import com.hm.chat.ChatRpcUtils;
import com.hm.chsdk.ChSDKConfUtils;
import com.hm.config.ConfigLoad;
import com.hm.config.GameConfig;
import com.hm.handler.GamePlayerMsgForwardProxy;
import com.hm.leaderboards.HdLeaderboardsService;
import com.hm.leaderboards.RankRedisConfig;
import com.hm.libcore.action.BaseAction;
import com.hm.libcore.handler.ServerStateCache;
import com.hm.libcore.handler.ServerStateCache.ServerState;
import com.hm.libcore.inner.InnerAction;
import com.hm.libcore.msg.Router;
import com.hm.libcore.serverConfig.GameServerMachine;
import com.hm.libcore.serverConfig.H5Config;
import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.springredis.factory.MapperFactory;
import com.hm.libcore.util.GameIdUtils;
import com.hm.log.ali.AliLogProducerUtil;
import com.hm.log.ali.IndexUtil;
import com.hm.message.MessageIDUtils;
import com.hm.model.activity.kfactivity.KFServerInfoUtil;
import com.hm.model.activity.kfseason.server.KfSeasonServerUtils;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.mq.msg.MqManager;
import com.hm.observer.IObserver;
import com.hm.redis.type.RedisConstants;
import com.hm.redis.util.RedisDbUtil;
import com.hm.rediscenter.CenterRedisUtils;
import com.hm.rpc.GameRpc;
import com.hm.server.GameHttpServer;
import com.hm.server.GameServer;
import com.hm.server.GameServerManager;
import com.hm.server.WebSocketServer;
import com.hm.timerjob.GuildWarUtils;
import com.hm.timerjob.server.GameTimerManager;
import com.hm.util.GameIpUtils;
import com.hm.war.sg.SettingManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerInit {

	public static void initServer() {
		GameIpUtils.registerGameIp();
		GameServerManager.getInstance().init();
		MessageIDUtils.init();
		//====================注册消息号 ========================================
		SpringUtil.getBeanMap(BaseAction.class).values().forEach(e -> e.registerMsg());
		SpringUtil.getBeanMap(InnerAction.class).values().forEach(e -> e.registerMsg());
		//添加消息转发
		Router.getInstance().setMsgForwardProxy(SpringUtil.getBean(GamePlayerMsgForwardProxy.class));
		//=====================加载世界配置文件=========================================
		ConfigLoad.loadAllConfig();
		//====================加载观察者 ========================================
		SpringUtil.getBeanMap(IObserver.class).values().forEach(e -> e.registObserverEnum());
		AliLogProducerUtil.init();
		KFServerInfoUtil.init();
		WXUtils.checkToken();//微信数据

		GameIdUtils.init(ServerConfig.getInstance().getMachineId());
		//注册redisDB
		MapperFactory.getFactory().registerDB();
		//=========================加载排行榜信息============================
		MqManager.getInstance().initMqProxy(MqGroupIdEnum.BIZ);
		HdLeaderboardsService.getInstance().init();
		ActorDispatcherType.startAll();
		OnceQueueTaskType.startAll();
		GameServerMachine.loadServerTcpPort();

		//按照顺序加载Container
		loadServerDB();
        KfWorldWarConf.init();//初始化跨服世界大战配置
		GameTimerManager.getInstance().startTimer();//启动定时器
		
		SettingManager.getInstance().initServer();
//		CmqUtil.getInstance().load(GameMqConfig.getInstance().getMqAccessKey(), GameMqConfig.getInstance().getMqSecretKey(), GameMqConfig.getInstance().getMqEndpoint(),GameMqConfig.getInstance().getDefaultTopic());
		RedisDbUtil.returnResource(RedisDbUtil.getJedis(RedisConstants.Player));//提前加载redis
		RedisDbUtil.returnResource(RedisDbUtil.getJedis(RedisConstants.Threshold));//提前加载redis
		RankRedisConfig.getInstance().returnResource(RankRedisConfig.getInstance().getRankPool().getResource());
		CenterRedisUtils.returnResource(CenterRedisUtils.getWorldCityPool().getResource());
		ChSDKConfUtils.init();//草花sdk初始化
		GuildFlagManger.init();
		KfSeasonServerUtils.reloadKFSeasonServerFromDB();
		GuildWarUtils.checkWarOpen();//检查部落战开启时间

		ServerStateCache.changeServerState(ServerState.Running);
		GameConfig.StartDate = DateUtil.now();
		//加载唯一码过滤白名单
		loadIpWhite();
		// 注销用户数据加载
		RemovePlayerBiz.init();
		LoginMsgBiz.init();
	}
	
	private static void loadIpWhite() {
		try {
			IdCodeFilterBiz idCodeFilterBiz = SpringUtil.getBean(IdCodeFilterBiz.class);
			idCodeFilterBiz.loadIpWhites();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void loadServerDB() {
		GmMailManager.getInstance().loadMail();
		//获取所有的继承BaseServerContainer的类，然后调用loadDataMap()方法初始化
		GameServerManager.getInstance().getServerIdList().forEach(serverId -> {
			CreateServerFactory.checkCreateFirstServer(serverId);//第一次启动检查
			ServerDataManager.getIntance().loadServerData(serverId);
			CreateServerFactory.createServerIndex(serverId);
			CreateServerFactory.createServerContainer(serverId);
		});
		ServerDataManager.getIntance().checkAllFirstBaseNpclv();
		IndexUtil.checkAndCreateIndex();
	}
	
	public static void startGame() {
		GameRpc.startRpc();//启动rpc
		ChatRpcUtils.startChatClientRpc();

		new Thread(() -> {
			GameServer gameServer = new GameServer();
			gameServer.start();
		}).start();

		new Thread(() -> {
			try {
				GameHttpServer httpServer = new GameHttpServer();
				httpServer.start();
			} catch (Exception e) {
				log.error("htpp游戏服务器启动：", e);
			}
		}).start();

		new Thread(() -> {
			if (H5Config.getInstance().isOpenH5()) {
				new WebSocketServer().start();
				log.error("h5 端口启动");
			}
		}).start();

		log.info("游戏服务器启动成功"+GameConfig.getGameVersion());
	}
	
}









