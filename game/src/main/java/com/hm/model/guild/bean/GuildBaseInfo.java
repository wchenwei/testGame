package com.hm.model.guild.bean;

import com.hm.libcore.msg.JsonMsg;
import com.hm.model.guild.GuildComponent;
import com.hm.model.player.Player;
import lombok.Getter;
import org.springframework.data.annotation.Transient;

import java.util.Date;

@Getter
public class GuildBaseInfo extends GuildComponent{
	private String guildName;	//部落名字
	private long createUserId;	//创建人
	private Date createTime;	//创建时间
	private long money;			//部落资金
	private String notice;		//部落公告
	private long leaderId;		//部落长ID
	private int tecPoints;		//科技点数
	private long lastLoginTime; //最后登录时间（只有部落成员有登录，就会更新）
	private long lastLeaderLoginTime;// 首领最后登录时间
	private String leaderName;	//部落长名称
	

	public void initBaseInfo(Player player, String guildName) {
		this.createUserId = player.getId();
		this.createTime = new Date();
		this.money = 0;
		this.leaderId = player.getId();
		this.leaderName = player.getName();
		this.guildName = guildName;
		SetChanged();
	}
	public void setLeader(Player transPlayer) {
		leaderId = transPlayer.getId();		//部落长ID
		this.leaderName = transPlayer.getName();
		this.lastLeaderLoginTime = transPlayer.playerBaseInfo().getLastLoginDate().getTime();
		SetChanged();
	}

	public void setGuildName(String guildName) {
		this.guildName = guildName;
		SetChanged();
	}

	public void changeNotice(String notice) {
		this.notice = notice;
		SetChanged();
	}

	//增加科技点数
	public void addTecPoints(int tecPoints) {
		this.tecPoints += tecPoints;
		SetChanged();
	}
	//减科技点数
	public boolean reduceTecPoints(int pointsIn) {
		if(this.tecPoints-pointsIn>=0) {
			this.tecPoints = this.tecPoints-pointsIn;
			SetChanged();
			return true;
		}
		return false;
	}
	public void resetPoints(int points) {
		this.tecPoints=points;
		SetChanged();
	}

	public void updateLoginTime() {
		this.lastLoginTime = new Date().getTime();
		SetChanged();
	}

	public void updateLeaderLoginTime() {
		this.lastLeaderLoginTime = new Date().getTime();
		SetChanged();
	}

	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
		SetChanged();
	}

	@Override
	public void fillMsg(JsonMsg msg) {
		msg.addProperty("guildBaseInfo", this);
	}
}
