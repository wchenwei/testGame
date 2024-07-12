package com.hm.model.guild.bean;

import lombok.Data;

import java.util.Date;

//保存邀请信息
@Data
public class GuildInviteBean {
	public GuildInviteBean() {}
	public GuildInviteBean(Date date, long reqPlayerId) {
		this.inviteDate = date;
		this.reqPlayer = reqPlayerId;
	}
	private Date inviteDate;//邀请日期
	private long reqPlayer;//邀请人
}
