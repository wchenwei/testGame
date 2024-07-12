package com.hm.model.guild;

import com.google.common.collect.Lists;
import com.hm.libcore.msg.JsonMsg;
import com.hm.db.PlayerUtils;
import com.hm.model.guild.bean.GuildReqPlayer;
import com.hm.model.player.Player;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class GuildReqs extends GuildComponent{

	protected transient ConcurrentHashMap<Long, GuildReqPlayer> playerMap = new ConcurrentHashMap<>();
	public void cleanReq() {
		this.playerMap.clear();
		SetChanged();
	}
	public void addPlayer(GuildReqPlayer player) {
		playerMap.put(player.getPlayerId(), player);
		SetChanged();
	}

	public void removePlayer(long playerId) {
		playerMap.remove(playerId);
		SetChanged();
	}

	public GuildReqPlayer guildPlayer(long playerId) {
		return playerMap.get(playerId);
	}

	public List<GuildReqPlayer> getGuildMembers() {
		return Lists.newArrayList(this.playerMap.values());
	}

	public List<Player> getPlayerList() {
		return this.playerMap.values().stream().map(e -> PlayerUtils.getPlayer(e.getPlayerId()))
				.filter(Objects::nonNull).collect(Collectors.toList());
	}

	public int getNum() {
		return playerMap.size();
	}

	@Override
	protected void fillMsg(JsonMsg msg) {
		msg.addProperty("GuildReqs", this);
	}
}
