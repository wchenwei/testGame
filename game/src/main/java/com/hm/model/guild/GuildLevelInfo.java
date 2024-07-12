package com.hm.model.guild;

import com.hm.libcore.msg.JsonMsg;

public class GuildLevelInfo extends GuildComponent {
	private int lv =1;//部落等级
	private int exp =0;//经验
	public int getLv() {
		return lv;
	}
	public void setLv(int lv) {
		this.lv = lv;
		SetChanged();
	}
	public int getExp() {
		return exp;
	}
	public void addExp(int exp){
		this.exp += exp;
		SetChanged();
	}
	public void updateGuildLv(int lv){
		if(this.lv!=lv) {
			this.lv=lv;
			SetChanged();
		}
	}
	@Override
	public void fillMsg(JsonMsg msg) {
		msg.addProperty("guildLevelInfo", this);
	}

}
