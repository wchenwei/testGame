
package com.hm.container;

import com.google.common.collect.Lists;
import com.hm.action.login.biz.LoginBiz;
import com.hm.config.GameConstants;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.util.function.GofFunction1;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Title: GamePlayerContainer.java
 * Description:游戏玩家容器
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 李飞
 * @date 2015年4月22日 上午10:52:23
 * @version 1.0
 */
@Slf4j
public class PlayerContainer{
	//所有在线玩家
	@Getter
	private static Map<Long, Player> gamePlayerMap = new ConcurrentHashMap<>();

    //被禁言的玩家
    private static List<Long> noChatIds = Lists.newArrayList();
	
	public static void addPlayer2Map(Player player){
		gamePlayerMap.put(player.getId(), player);
	}
	
	public static void removePlayer(long id){
		gamePlayerMap.remove(id);
	}
	
	public static Player getPlayer(long id){
		return gamePlayerMap.get(id);
	}
	
	public static boolean isOnline(long id) {
		Player player = gamePlayerMap.get(id);
		if(player != null && !player.playerTemp().isCloneLogin()) {
			return true;
		}
		return false;
	}
	
	public static List<Player> getOnlinePlayers(){
		return gamePlayerMap.values()
				.stream().filter(e -> !e.isKFPlayer())
				.collect(Collectors.toList());
	}
	
	public static List<Player> getOnlinePlayersByServerId(int serverId){
		return gamePlayerMap.values().stream().filter(t-> t.getServerId()==serverId).collect(Collectors.toList());
	}

	public static void broadPlayer(JsonMsg msg) {
		gamePlayerMap.values().stream()
				.forEach(e -> e.sendMsg(msg));
	}
	
	public static void broadPlayer(int serverId,JsonMsg msg) {
		gamePlayerMap.values().stream().filter(e -> e.getServerId() == serverId)
			.forEach(e -> e.sendMsg(msg));
	}
	public static void broadPlayerByGuild(int guildId,JsonMsg msg) {
		gamePlayerMap.values().stream().filter(e -> e.getGuildId() == guildId)
				.forEach(e -> e.sendMsg(msg));
	}

	public static void doOnlinePlayerForFunc(GofFunction1<Player> func) {
		gamePlayerMap.values().forEach(e -> func.apply(e));
	}


	/**
	 * 踢出超过5分钟没有发消息的玩家
	 */
	public static void checkOnlinePlayer() {
		long now = System.currentTimeMillis();
		long maxTime = 5*GameConstants.MINUTE;
		for (Player player : gamePlayerMap.values()) {
			if(player.isKFPlayer()) {
				continue;
			}
			if(now-player.playerTemp().getLastMsgTime() > maxTime) {
				log.error("踢出玩家:"+player.getName());
				SpringUtil.getBean(LoginBiz.class).doLoginOut(player);
			}else{
				player.notifyObservers(ObservableEnum.PlayerHeart);
			}
		}
	}

    public static void loadNoChatIds(List<Long> ids) {
        noChatIds = ids;
    }

    public static boolean isNoChat(long playerId) {
        return noChatIds.contains(playerId);
    }

    public static void addNoChat(long playerId) {
        noChatIds.add(playerId);
    }
}

