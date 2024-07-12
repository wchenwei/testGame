package com.hm.model.player;

import com.hm.libcore.msg.JsonMsg;
//战车装备碎片
public class PlayerTankpiece extends PlayerBagBase{

	@Override
	public void fillMsg(JsonMsg msg) {
		msg.addProperty("playerTankpiece", this);
	}
}
