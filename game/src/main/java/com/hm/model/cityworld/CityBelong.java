package com.hm.model.cityworld;

public class CityBelong extends CityComponent{
	private int guildId;//所属部落id

	public void setGuildId(int guildId) {
		this.guildId = guildId;
		SetChanged();
	}

	public int getGuildId() {
		return guildId;
	}
}
