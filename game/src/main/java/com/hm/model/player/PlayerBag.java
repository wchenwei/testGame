package com.hm.model.player;

import com.hm.libcore.msg.JsonMsg;
/**
 * @Description: 玩家背包
 * @author siyunlong  
 * @date 2017年12月18日 下午1:26:00 
 * @version V1.0
 */
public class PlayerBag extends PlayerBagBase{
	@Override
	public void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerBag", this);
	}
}
