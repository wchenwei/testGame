package com.hm.model.player;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.http.HttpUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.db.mongo.ClassChanged;
import com.hm.libcore.spring.SpringUtil;
import com.hm.action.guild.biz.GuildFactoryBiz;
import com.hm.action.login.biz.LoginBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.action.sys.SysFacade;
import com.hm.cache.PlayerCacheManager;
import com.hm.db.PlayerUtils;
import com.hm.enums.ActivityType;
import com.hm.enums.LeaveOnlineType;
import com.hm.enums.PlayerFunctionType;
import com.hm.model.activity.AbstractActivity;
import com.hm.model.activity.PlayerActivityValue;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.server.GameServerManager;
import com.hm.servercontainer.activity.ActivityServerContainer;
import com.hm.util.ServerUtils;
import com.mongodb.client.MongoCollection;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.bson.Document;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

@Slf4j
public class ClonePlayerUtils {
	
	public static boolean clonePlayer(long sid,long toid) {
		Player sourcePlayer = MongoUtils.getGameMongodDB(ServerUtils.getServerId(sid)).get(sid, Player.class);
		Player toPlayer = PlayerUtils.getPlayer(toid);
		
		return clonePlayer(sourcePlayer, toPlayer);
	}

    public static boolean clonePlayer(Player sourcePlayer, Player toPlayer) {
        return clonePlayer(sourcePlayer, toPlayer, true);
    }

    public static boolean clonePlayer(Player sourcePlayer, Player toPlayer, boolean clearAct) {
        LoginBiz loginBiz = SpringUtil.getBean(LoginBiz.class);
        SysFacade sysFacade = SpringUtil.getBean(SysFacade.class);
        if(sourcePlayer == null || toPlayer == null) {
            return false;
        }
        Player p1 = PlayerUtils.getOnlinePlayer(sourcePlayer.getId());
        if(p1 != null && p1.isOnline()) {
            sysFacade.sendLeavePlayer(p1, LeaveOnlineType.SERVER);
        }
        Player p2 = PlayerUtils.getOnlinePlayer(toPlayer.getId());
        if(p2 != null && p2.isOnline()) {
            sysFacade.sendLeavePlayer(p2, LeaveOnlineType.SERVER);
        }
        toPlayer.changeName(sourcePlayer.getName());
		List<String> filerIds = Lists.newArrayList("playerBaseInfo","id","channelId","serverId"
				,"uid","createServerId","playerGuild","playerFriend","playerTroops");
		for (Field field : ReflectUtil.getFields(Player.class)) {
			if (filerIds.contains(field.getName())) {
				continue;
			}
			Object obj = ReflectUtil.getFieldValue(sourcePlayer, field);
			//if (obj instanceof ClassChanged) {
			ReflectUtil.setFieldValue(toPlayer, field, obj);
			//}
		}

        sourcePlayer.getPlayerFunction().getFunctionIds().stream().filter(e -> e != PlayerFunctionType.TradeStock.getType())
                .forEach(e -> toPlayer.getPlayerFunction().addOpenFunction(e));

        //处理活动
        if (clearAct) {
            doPlayerActivity(toPlayer);
        }

        //清除玩家武器信息
        GuildFactoryBiz guildFactoryBiz = SpringUtil.getBean(GuildFactoryBiz.class);
        guildFactoryBiz.clearArms(toPlayer);

        PlayerCacheManager.getInstance().removePlayerCache(toPlayer.getId());
        PlayerCacheManager.getInstance().removePlayerCache(sourcePlayer.getId());
        setPlayerAllChange(toPlayer);
        toPlayer.saveNowDB();
        return true;
    }
	
	private static void setPlayerAllChange(Player player) {
		try {
			for (Field field : ReflectUtil.getFields(player.getClass())) {
	        	String fieldName = field.getName();
	        	if("id".equals(fieldName) 
	        			|| field.getAnnotation(Transient.class) != null) {
	        		continue;
	        	}
	        	field.setAccessible(true);
				Object obj = field.get(player);
				if(obj == null) {
					continue;
				}
				if(obj instanceof ClassChanged) {
					ClassChanged changeObj = (ClassChanged)obj;
					changeObj.SetChanged();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean copyPlayer(int fromId,int toId) {
		log.error("===============服务器玩家转移"+fromId+"->"+toId+"=======================================");
		MongoTemplate fromTemplate = MongoUtils.getGameMongodDB(fromId).getMongoTemplate();
		MongoCollection<Document> collPlayer = fromTemplate.getCollection("player");
		for (Document document : collPlayer.find()) {
			Player player = fromTemplate.getConverter().read(Player.class, document);
			//copy数据
			copyPlayerData(player, toId);
		}
		log.error("===============服务器玩家转移"+fromId+"->"+toId+"=======================================");
		return true;
	}
	
	public static boolean copyPlayerForPlayer(int fromPlayerId,int toServerId) {
		int fromServerId = ServerUtils.getCreateServerId(fromPlayerId);
		Player player = MongoUtils.getGameMongodDB(fromServerId).get(fromPlayerId, Player.class);
		if(player == null) {
			log.error(fromPlayerId+"转移失败！玩家不存在");
			return false;
		}
		return copyPlayerData(player, toServerId);
	}
	
	public static boolean copyPlayerData(Player player,int toId) {
		PlayerBiz playerBiz = SpringUtil.getBean(PlayerBiz.class);
         String imei = player.playerBaseInfo().getImei();
         int channelId = player.getChannelId();
         long uid = player.getUid();
         long oldPlayerId = PlayerUtils.getPlayerId(uid, toId,toId);
         if(oldPlayerId > 0) {
         	//此服务器已经有玩家数据了,转移失败
         	log.error(player.getId()+"转移失败！目标服务器有玩家数据,"+ oldPlayerId);
         	return false;
         }
         Player toPlayer = playerBiz.createPlayer(player.getUid(), toId, toId,channelId, imei, 0);

         clonePlayer(player, toPlayer);
         
         log.error(player.getId()+"->"+toPlayer.getId()+"  转移完成" );
         return true;
	}
	
	public static Player toKfPlayer(String playerId) {
		try {
			String url = "http://49.232.193.52:7180/GameHotWeb/getPlayerData.jsp?playerId="+playerId;
			System.err.println(url);
			String json = HttpUtil.get(url, Charset.forName("utf-8"));
			Document doc = Document.parse(json);
			List<Integer> serverIds = GameServerManager.getInstance().getServerIdList();
			MongoConverter mongoConverter = MongoUtils.getMongodDB(serverIds.get(0)).getMongoTemplate().getConverter();
			return mongoConverter.read(Player.class, doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<Integer> ActivityTypeList
			= Lists.newArrayList(ActivityType.LoginWelfare.getType());

    public static void doPlayerActivity(Player player) {
        Map<Integer, PlayerActivityValue> activityMap = player.getPlayerActivity().getActivityMap();
        for (int acId : Lists.newArrayList(activityMap.keySet())) {
			if (!ActivityTypeList.contains(acId)) {
                activityMap.remove(acId);
                continue;
            }
			ActivityType activityType = ActivityType.getActivityType(acId);
			AbstractActivity activity = ActivityServerContainer.of(player).getAbstractActivity(activityType);
            if (activity == null) {
                activityMap.remove(acId);
                continue;
            }
            activityMap.get(acId).setActivityId(activity.getId());
        }
        player.playerActivity.SetChanged();
    }
}
