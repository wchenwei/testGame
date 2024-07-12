package com.hm.action.shop.vo;

import com.hm.model.item.Items;

public class ShopRewardVo {
	private int id;
	private Items reward;
	
	public ShopRewardVo() {
		super();
	}
	public ShopRewardVo(int id, Items reward) {
		super();
		this.id = id;
		this.reward = reward;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Items getReward() {
		return reward;
	}
	public void setReward(Items reward) {
		this.reward = reward;
	}
	

}
