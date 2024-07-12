package com.hm.model.player;

import com.google.common.collect.Lists;
import com.hm.libcore.msg.JsonMsg;

import java.util.List;

/**
 * 
 * 玩家自定义坦克阵容
 * @author siyunlong
 *
 */
public class PlayerCustomTanks extends PlayerDataContext{
	private List<CustomItem> customList = Lists.newArrayList();
	
	public void addCustomItem(CustomItem customItem) {
		this.customList.add(customItem);
		SetChanged();
	}
	
	public void removeCustomItem(int index) {
		this.customList.remove(index);
		SetChanged();
	}
	
	public void changeCustomItemName(int index,String name) {
		customList.get(index).setName(name);
		SetChanged();
	}
	
	public int getSize() {
		return this.customList.size();
	}
	
	@Override
	protected void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerCustomTanks", this);
	}
	
	
}
