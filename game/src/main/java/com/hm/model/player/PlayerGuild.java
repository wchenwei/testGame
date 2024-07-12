package com.hm.model.player;

import com.google.common.collect.Lists;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.util.TimeUtils;
import com.hm.enums.GuildJob;
import com.hm.model.guild.Guild;
import com.hm.servercontainer.guild.GuildContainer;
import org.springframework.data.annotation.Transient;

import java.util.Date;
import java.util.List;

public class PlayerGuild extends PlayerDataContext{
	private int guildId;
	private Date levelTime;//上一次离开部落时间
	//申请部落列表
	private List<Integer> reqGuild = Lists.newArrayList();
	@Transient
	private int guildJob=0;
	//首次加入部落奖励领取标示0:未加入部落；1：已加入过部落
	private int firstAddReward = 0;
	//领取到哪个阶段了
	private int guildTaskPos;
	
	private int guildProdeceCount;//今日部落生产次数(每日清空)
	private transient long lastCityTime;//上次领取城市奖励时间
	
	public List<Integer> getReqGuild() {
		return reqGuild;
	}
	public int getGuildId() {
		return guildId;
	}

	public boolean timeCheck(int hour) {
		if(null==levelTime || TimeUtils.getDifferMinutes(levelTime,new Date())>= hour*60) {
			return true;
		}
		return false;
	}
	
	public int getReqSize() {
		return this.reqGuild.size();
	}
	public void addReq(int guildId) {
		reqGuild.add(guildId);
		SetChanged();
	}
	public void removeReq(Integer guildId) {
		reqGuild.remove(guildId);
		SetChanged();
	}
	
	public boolean reReq(int guildId) {
		return this.reqGuild.contains(guildId);
	}

	public void setGuild(Guild guild) {
		this.guildId = guild.getId();
		this.changeFirstAddState();
		SetChanged();
	}
	@Override
	public void fillMsg(JsonMsg msg) {
		Guild guild = GuildContainer.of(this.Context().getServerId()).getGuild(this.Context().playerGuild().getGuildId());
		if(null!=guild) {
			GuildJob jobEnum = guild.getGuildMembers().getJob(this.Context().getId());
			guildJob = null==jobEnum?0:jobEnum.getType();
		}else {
			guildJob=0;
		}
		msg.addProperty("PlayerGuild", this);
	}
	public void quit() {
		this.guildId=0;
		this.levelTime = new Date();
		SetChanged();
	}
	public void setLevelTime(Date levelTime) {
		this.levelTime = levelTime;
		SetChanged();
	}
	public void clearReq() {
		reqGuild.clear();
		SetChanged();
	}
	public void removeGuild() {
		this.guildId = 0;
		SetChanged();
	}
	
	//是否可以领取奖励
	public boolean canFirstAddReward() {
		return firstAddReward <= 0;
	}

	//首次加入部落修改状态
	public void changeFirstAddState() {
		if(this.firstAddReward <= 0) {
			this.setRewardState(1);
		}
	}
	//修改部落状态
	private void setRewardState(int state) {
		this.firstAddReward = state;
		SetChanged();
	}
	public int getGuildTaskPos() {
		return guildTaskPos;
	}
	public void setGuildTaskPos(int guildTaskPos) {
		this.guildTaskPos = guildTaskPos;
		SetChanged();
	}
	public void doWeekReset() {
		if(this.guildTaskPos > 0) {
			this.guildTaskPos = 0;
			SetChanged();
		}
	}
	//部落生产次数每日清空
	public void doDayReset() {
		if(this.guildProdeceCount > 0) {
			this.guildProdeceCount = 0;
			SetChanged();
		}
	}
	
	public int getStrengthCount() {
		return guildProdeceCount;
	}
	public void produce() {
		this.guildProdeceCount++;
		SetChanged();
	}

	public long getLastCityTime() {
		return lastCityTime;
	}

	public void updateLastCityTime(long nextTime) {
		this.lastCityTime = nextTime;
		SetChanged();
	}
}


