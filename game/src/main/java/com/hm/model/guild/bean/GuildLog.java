package com.hm.model.guild.bean;

import java.util.Date;

public class GuildLog {
	private Date createTime;
	private String content;
	
	public GuildLog() {}
	public GuildLog(String content) {
		this.createTime = new Date();
		this.content = content;
	}
}
