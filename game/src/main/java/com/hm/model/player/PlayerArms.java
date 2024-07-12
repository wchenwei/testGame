package com.hm.model.player;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.msg.JsonMsg;

import java.util.List;
import java.util.Map;

public class PlayerArms extends PlayerDataContext {
	private Map<String,Arms> map = Maps.newConcurrentMap();
	private int state;//玩家武器状态 0-未开启 1-开启（部落达到5级开启，开启后不再关闭）
	
	public Arms getArms(String id){
		return map.get(id);
	}
	public List<Arms> getAllArms(){
		return Lists.newArrayList(map.values());
	}
	//获得武器
	public void addArms(Arms arms) {
		this.map.put(arms.getUid(), arms);
		SetChanged();
	}

	public void down(String uid) {
		Arms arms = getArms(uid);
		arms.setPos(-1);
		SetChanged();
	}
	
	
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
		SetChanged();
	}

	@Override
	protected void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerArms", this);
	}

	public void decompose(String uid) {
		this.map.remove(uid);
		SetChanged();
	}

}
