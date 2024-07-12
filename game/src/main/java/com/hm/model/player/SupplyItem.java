package com.hm.model.player;

import com.google.common.collect.Lists;
import com.hm.enums.SupplyTroopType;
import com.hm.model.item.Items;
import com.hm.model.supplytroop.SupplyContants;
import com.hm.libcore.mongodb.PrimaryKeyWeb;
import lombok.Data;

import java.util.List;

@Data
public class SupplyItem {
	private String id;
	private int cityId;
	private int star;
	private Items item;
	private int type = SupplyTroopType.None.getType();
	private List<Integer> wayList = Lists.newLinkedList();
	private double robRate;//剩余
	private long endTime;//如果是配送状态代表到达时间
	//是否双倍
	private boolean isDouble;
	
	public void loadPlayer(Player player) {
		this.id = player.getId()+"_"+PrimaryKeyWeb.getPrimaryKey("SupplyItem", player.getServerId());
	}
	
	public void loadDouble(boolean isDouble) {
		this.isDouble = isDouble;
	}
	
	public boolean isOver() {
		return this.type == SupplyTroopType.Run.getType() && System.currentTimeMillis() > endTime;
	}
	
	public int getWaySize() {
		return wayList.size() - 1;
	}
	
	public boolean isRob() {
		return this.robRate > 0;
	}
	
	public void doRob() {
		this.robRate = SupplyContants.randomRate();
	}
	
	public void changeSupplyType(SupplyTroopType type) {
		this.type = type.getType();
	}
	
	public Items calDoubleItem(Items cloneItem) {
		if(isDouble) {
			cloneItem.setCount(cloneItem.getCount()*2);
		}
		return cloneItem;
	}
	
	public Items getSupplyItem() {
		if(this.robRate <= 0) {
			return calDoubleItem(this.item);//没有被抢
		}
		long robCount = Math.max((long)(item.getCount()*this.robRate), 1);
		Items clone = this.item.clone();
		clone.setCount(item.getCount()-robCount);
		return calDoubleItem(clone);
	}
	
	public Items getRobItem() {
		long robCount = Math.max((long)(item.getCount()*this.robRate), 1);
		Items clone = this.item.clone();
		clone.setCount(robCount);
		return calDoubleItem(clone);
	}
	

}
