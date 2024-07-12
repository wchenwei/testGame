package com.hm.model.player;

import com.google.common.collect.Lists;
import com.hm.libcore.msg.JsonMsg;
import com.hm.config.GameConstants;

import java.util.List;

public class PlayerMysteryShop extends PlayerDataContext {
	private long endTime;
	private List<Integer> goods = Lists.newArrayList();
	private List<Integer> buys = Lists.newArrayList();
	private int count;
	private int progress;
	
	public boolean isCanBuy(int id){
		return goods.contains(id)&&!buys.contains(id)&&endTime>=System.currentTimeMillis();
	}

	public long getEndTime() {
		return endTime;
	}

	public List<Integer> getGoods() {
		return goods;
	}

	public List<Integer> getBuys() {
		return buys;
	}

	public int getCount() {
		return count;
	}

	public int getProgress() {
		return progress;
	}

	public void buy(int id) {
		this.buys.add(id);
		SetChanged();
	}

	public void addCount() {
		this.count++;
		SetChanged();
	}
	public void addProgress(int count){
		this.progress +=count;
		SetChanged();
	}
	public void createGoods(List<Integer> goods) {
		addCount();
		this.progress = 0;
		this.goods = goods;
		this.buys.clear();
		this.endTime = System.currentTimeMillis()+GameConstants.HOUR;
		SetChanged();
	}
	public void resetDay() {
		this.progress = 0;
		this.count = 0;
		SetChanged();
	}
	@Override
	protected void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerMysteryShop", this);
	}

}
