package com.hm.model.serverpublic;

public class ServerWorldLvData extends ServerPublicContext{
	private int type;//0-未开启世界等级  1-开启世界等级
	private int worldLv;//当前世界等级
	
	public void openWorldLv() {
		this.type = 1;
		SetChanged();
		save();
	}

	public int getWorldLv() {
		return worldLv;
	}

	public void setWorldLv(int worldLv) {
		this.worldLv = worldLv;
        SetChanged();
	}

	public int getType() {
		return type;
	}
	
	
}
