package com.hm.model.activity;

import com.hm.model.player.Player;

public interface IActivityTaskValue {
	public void taskFinish(Player player,int taskId);
	default boolean isCanReset(Player player,int id){
		return false;
	}
}
