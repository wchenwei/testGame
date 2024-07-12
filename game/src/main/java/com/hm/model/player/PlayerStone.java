package com.hm.model.player;

import com.hm.libcore.msg.JsonMsg;
//宝石箱
public class PlayerStone extends PlayerBagBase{
	
	@Override
	public void fillMsg(JsonMsg msg) {
		msg.addProperty("playerStone", this);
	}
	
	
}
