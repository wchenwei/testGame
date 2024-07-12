package com.hm.model.shop;

import com.hm.model.item.Items;

public class ShopItem {
	private int shopItemId;
	private Items item;
	private Items costItem;//购买的花费
	private int discount=100;
	private int buyCount;//购买次数
	
	
	public ShopItem() {
		super();
	}
	public ShopItem(int id,Items item, Items costItem) {
		super();
		this.shopItemId = id;
		this.item = item;
		this.costItem = costItem;
	}
	public Items getItem() {
		return item;
	}
	public void setItem(Items item) {
		this.item = item;
	}
	
	public Items getCostItem() {
		return costItem;
	}
	public void setCostItem(Items costItem) {
		this.costItem = costItem;
	}
	public int getDiscount() {
		return discount;
	}
	public void setDiscount(int discount) {
		this.discount = discount;
	}
	public int getBuyCount() {
		return buyCount;
	}
	public void setBuyCount(int buyCount) {
		this.buyCount = buyCount;
	}
	public int getShopItemId() {
		return shopItemId;
	}
	public void setShopItemId(int shopItemId) {
		this.shopItemId = shopItemId;
	}
	public void addBuyCount() {
		this.buyCount++;
	}
	

}
