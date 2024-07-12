package com.hm.main;

import com.google.common.collect.Lists;
import com.hm.action.activity.ActivityServerConfBiz;
import com.hm.action.cmq.CmqBiz;
import com.hm.cache.PlayerCacheManager;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.MongodDB;
import com.hm.libcore.mongodb.PrimaryKeyWeb;
import com.hm.libcore.spring.SpringUtil;
import com.hm.model.guild.Guild;
import com.hm.model.mail.Mail;
import com.hm.model.player.Player;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.server.GameServerManager;
import com.hm.servercontainer.BaseServerContainer;
import com.hm.servercontainer.ContainerMap;
import com.hm.servercontainer.activity.ActivityServerContainer;
import com.hm.servercontainer.centreArms.CentreArmsContainer;
import com.hm.servercontainer.guild.GuildContainer;
import com.hm.servercontainer.idcode.IdCodeContainer;
import com.hm.servercontainer.mail.MailServerContainer;
import com.hm.servercontainer.notice.NoticeServerContainer;
import com.hm.servercontainer.player.PlayerDataServerContainer;
import com.hm.servercontainer.supplyTroop.SupplyTroopServerContainer;
import com.hm.servercontainer.troop.TroopServerContainer;
import com.hm.servercontainer.world.WorldServerContainer;
import com.hm.servercontainer.worldbuild.WorldBuildTroopServerContainer;
import com.hm.timerjob.server.GameTimerManager;
import com.hm.util.ServerUtils;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import org.bson.Document;

import java.util.List;

public class CreateServerFactory {
	
	public synchronized static void loadNewServer() {
		List<Integer> oldServerIds = GameServerManager.getInstance().getServerIdList();
		List<Integer> newServerIds = MongoUtils.getDbIdList();
		newServerIds.removeAll(oldServerIds);
		System.err.println("加载新服务器："+newServerIds);
		GameServerManager.getInstance().init();
		for (int serverId : newServerIds) {
			createServerNoCheck(serverId);
		}
	}
	public synchronized static boolean createServer(int serverId) {
		if(GameServerManager.getInstance().containServer(serverId)) {
			return true;
		}
		return createServerNoCheck(serverId);
	}
	/**
	 * 运行时初始化新的服务器
	 * @param serverId
	 */
	public synchronized static boolean createServerNoCheck(int serverId) {
		//判断是否在此服务器
		boolean isInServer = MongoUtils.getServerList().stream()
				.anyMatch(e -> e.getServer_id() == serverId);
		if(!isInServer) {
			return false;
		}
		//加载数据库
		MongoUtils.loadServerDbToCache(serverId);
		//加载服务器
		ServerDataManager.getIntance().loadServerData(serverId);
		//重新加载index
		createServerIndex(serverId);
		//加载container
		createServerContainer(serverId);
		//loadServer
		checkCreateFirstServer(serverId);//第一次启动检查
		//
		GameTimerManager.getInstance().startServerTimer(serverId);
		//重新加载服务器列表
		GameServerManager.getInstance().addServerId(serverId);
		//同步活动
		SpringUtil.getBean(ActivityServerConfBiz.class).syncActivity(serverId);
		
		CmqBiz cmqBiz =SpringUtil.getBean(CmqBiz.class);
		cmqBiz.statisServerMsg(serverId);
		
		return true;
	}
	
	/**
	 * 加载服务器的container
	 * ===================保证先后顺序===========================================
	 * @param serverId
	 */
	public static void createServerContainer(int serverId) {
		//加载容器
		List<ContainerMap> containerMapList = listSortContainer();
		containerMapList.forEach(e -> e.putAndNewServerContainer(serverId));
		//加载玩家私聊数据
        //SpringUtil.getBean(PersonalChatContainer.class).putAndNewServerContainer(serverId);
		//检查世界等级建筑
//		SpringUtil.getBean(WorldBuildBiz.class).checkWorldBuildOpen(serverId);
	}

	private static List<ContainerMap> listSortContainer() {
		List<ContainerMap> sortList = Lists.newArrayList();
		sortList.add(ActivityServerContainer.getServerMap());
		sortList.add(MailServerContainer.getServerMap());
		sortList.add(NoticeServerContainer.getServerMap());
		sortList.add(TroopServerContainer.getServerMap());
		sortList.add(WorldServerContainer.getServerMap());
		sortList.add(GuildContainer.getServerMap());
		sortList.add(SupplyTroopServerContainer.getServerMap());
		sortList.add(WorldBuildTroopServerContainer.getServerMap());
		sortList.add(IdCodeContainer.getServerMap());
		sortList.add(CentreArmsContainer.getServerMap());
		sortList.add(PlayerDataServerContainer.getServerMap());

		return sortList;
	}
	
	public static void createServerIndex(int serverId) {
		MongodDB mongo = MongoUtils.getMongodDB(serverId);
		mongo.createIndex(Player.class);
		mongo.createIndex(Mail.class.getSimpleName(),"sendType");
		//创建player复合索引
		checkPlayerIndex(mongo);
		//用户主键规则变化的时候，gm后台，邮件群发（邮件群发，根据用户id，自动寻找服务器），需要修改
		PrimaryKeyWeb.checkPrimaryKey(Player.class, ServerUtils.getMinPlayerId(serverId),serverId);
		PrimaryKeyWeb.checkPrimaryKey(Guild.class, ServerUtils.getMinPlayerId(serverId),serverId);
	}
	
	public static void checkPlayerIndex(MongodDB mongo) {
		String collName = "player";
		List<String> indexList = mongo.getCollectionIndex(collName);
		if(indexList.contains("uid")) {
			return;
		}
		IndexOptions indexOptions = new IndexOptions();
		indexOptions.background(true);
		Document keys = new Document();
		keys.put("uid", 1);
		keys.put("createServerId", 1);
		MongoCollection<Document> collection = mongo.getCollection(collName);
		System.err.println("创建player索引:"+collection.createIndex(keys, indexOptions));
	}
	
	/**
	 * 第一次启动服务器处理
	 * @param serverId
	 */
	public static void checkCreateFirstServer(int serverId) {
		if(!ServerDataManager.getIntance().isFirstStartServer(serverId)) {
			return;
		}
		
	}
	
	
	public static void stopServer(int serverId) {
		GameServerManager.getInstance().removeServer(serverId);
		//停止定时器
		GameTimerManager.getInstance().stopServerJob(serverId);
		//删除数据库
		ServerDataManager.getIntance().removeServerData(serverId);
		WorldBuildTroopServerContainer.saveAll(serverId);

		SpringUtil.getBeanMap(BaseServerContainer.class)
			.values().forEach(e -> e.removeItem(serverId));
        //把玩家的内存信息清除
        try {
            for (long playerId : Lists.newArrayList(PlayerCacheManager.getInstance().getCache().asMap().keySet())) {
                if (serverId == ServerUtils.getServerId(playerId)) {
                    PlayerCacheManager.getInstance().removePlayerCache(playerId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
