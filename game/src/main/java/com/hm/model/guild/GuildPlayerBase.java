package com.hm.model.guild;

import com.google.common.collect.Lists;
import com.hm.db.PlayerUtils;
import com.hm.model.guild.bean.GuildPlayer;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import com.hm.redis.PlayerRedisData;
import com.hm.redis.util.RedisUtil;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @Description: 部落成员管理基类
 * @author siyunlong  
 * @date 2019年1月28日 下午5:25:23 
 * @version V1.0
 */
public class GuildPlayerBase extends GuildComponent{
	protected transient ConcurrentHashMap<Long, GuildPlayer> playerMap = new ConcurrentHashMap<>();
	
	public int getNum() {
		return playerMap.size();
	}
	
	public boolean addPlayer(GuildPlayer player) {
		playerMap.put(player.getPlayerId(), player);
		SetChanged();
		return true;
	}

	public GuildPlayer guildPlayer(long playerId) {
		return playerMap.get(playerId);
	}
	//获取部落成员
	public List<GuildPlayer> getGuildMembers() {
		return Lists.newArrayList(this.playerMap.values());
	}

	public Set<Long> getGuildMemberIds(){
		return playerMap.keySet();
	}

	public void removePlayer(long playerId) {
		playerMap.remove(playerId);
		SetChanged();
	}
	
	//获取玩家玩家列表实体
	public List<Player> getPlayerList() {
		return this.playerMap.values().stream().map(e -> PlayerUtils.getPlayer(e.getPlayerId()))
				.filter(Objects::nonNull).collect(Collectors.toList());
	}
	public List<PlayerRedisData> getRedisPlayerList() {
		return RedisUtil.getListPlayer(this.playerMap.values().stream().map(GuildPlayer::getPlayerId).collect(Collectors.toList()));
	}
	
	//增加贡献调此方法，不要调其他方法
	public void addContr(BasePlayer player, long num) {
		GuildPlayer guildPlayer = playerMap.get(player.getId());
		if(null!=guildPlayer) {
			guildPlayer.contrAdd(num);
			SetChanged();
		}
	}
	//增加当日贡献信息（只计算部落捐献的增加）
	public void addDayContr(BasePlayer player, long num) {
		GuildPlayer guildPlayer = playerMap.get(player.getId());
		if(null!=guildPlayer) {
			guildPlayer.dayContrAdd(num);
			SetChanged();
		}
	}

	public boolean isLastMember(){
		return getNum() <= 1;
	}

}


