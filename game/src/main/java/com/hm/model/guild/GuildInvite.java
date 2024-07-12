package com.hm.model.guild;

import com.google.common.collect.Maps;
import com.hm.libcore.msg.JsonMsg;
import com.hm.model.guild.bean.GuildInviteBean;

import java.util.Date;
import java.util.Map;

//部落邀请用户加入信息
public class GuildInvite extends GuildComponent{
	private Map<Long, GuildInviteBean> inviteGuild = Maps.newConcurrentMap();
	
	//添加部落邀请
	public void addInvite(long playerId, long reqPlayerId) {
		inviteGuild.put(playerId, new GuildInviteBean(new Date(), reqPlayerId));
		SetChanged();
	}
	//删除部落邀请
	public void removeInvite(long playerId) {
		inviteGuild.remove(playerId);
		SetChanged();
	}
	public boolean containsInvite(long playerId) {
		return this.inviteGuild.containsKey(playerId);
	}
	public Map<Long, GuildInviteBean> getInviteGuild() {
		return inviteGuild;
	}
	
	public void check() {
		
	}
	
	@Override
	protected void fillMsg(JsonMsg msg) {
		msg.addProperty("guildInvite", this);
	}
}
