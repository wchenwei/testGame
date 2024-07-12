package com.hm.model.player;

import java.util.concurrent.ConcurrentHashMap;

public class PlayerBagBase extends PlayerDataContext{
	private ConcurrentHashMap<Integer, Long> itemMap = new ConcurrentHashMap<>();
	
	public long getItemCount(int itemId) {
		return itemMap.getOrDefault(itemId, 0L);
	}
	public boolean checkCanUse(int itemId,long num) {
		return getItemCount(itemId) >= num;
	}
	
	public ConcurrentHashMap<Integer, Long> getData() {
		return itemMap;
	}
	
	public void addItem(int itemId,long count) {
		this.itemMap.put(itemId, getItemCount(itemId)+count);
		SetChanged();
	}
	
	public void addItem(int itemId) {
		addItem(itemId, 1);
	}
	
	public boolean spendItem(int itemId) {
		return spendItem(itemId, 1);
	}
	public boolean spendItem(int itemId, long count) {
		if (checkCanUse(itemId, count)) {
			long lastCount = getItemCount(itemId);
			if (lastCount <= count) {
				this.itemMap.remove(itemId);
			} else {
				this.itemMap.put(itemId, lastCount-count);
			}
			SetChanged();
			return true;
		}
		return false;
	}
	
	public boolean removeItem(int itemId) {
		if(this.itemMap.remove(itemId) != null) {
			SetChanged();
			return true;
		}
		return false;
	}
}











