package com.hm.model.guild;

import com.google.common.collect.Lists;
import com.hm.libcore.msg.JsonMsg;

import java.util.List;

public class GuildFlag extends GuildComponent{
	private String flagName;//军团旗帜的字
	private List<Integer> buyFlag = Lists.newArrayList();//军团已经买过的旗帜列表

	public String getFlagName() {
		return flagName;
	}

	public void setFlagName(String flagName) {
		this.flagName = flagName;
		SetChanged();
	}

	public void initData(String flagName) {
		this.flagName = flagName;
		SetChanged();
	}

	@Override
	protected void fillMsg(JsonMsg msg) {
		msg.addProperty("guildFlag", this);
	}
	
}
