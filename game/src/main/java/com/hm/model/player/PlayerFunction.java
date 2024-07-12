package com.hm.model.player;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.hm.libcore.msg.JsonMsg;
import com.hm.enums.PlayerFunctionType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerFunction extends PlayerDataContext {
	//功能开启id列表
	private ConcurrentHashSet<Integer> functionIds = new ConcurrentHashSet<>();
	
	//判断是否开放此功能
	public boolean isOpenFunction(PlayerFunctionType type) {
		return functionIds.contains(type.getType());
	}
	
	//判断是否开放此功能
	public boolean isOpenFunction(int type) {
		return functionIds.contains(type);
	}
	
	//增加开放功能
	public void addOpenFunction(int type) {
		this.functionIds.add(type);
		SetChanged();
	}
	
	@Override
	protected void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerFunction", this);
	}
	
}
