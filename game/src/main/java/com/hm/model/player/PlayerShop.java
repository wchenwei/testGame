package com.hm.model.player;

import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.ShopConfig;
import com.hm.model.shop.PlayerShopValue;

import java.util.concurrent.ConcurrentHashMap;

public class PlayerShop extends PlayerDataContext {
	private ConcurrentHashMap<Integer, PlayerShopValue> shopMap = new ConcurrentHashMap<>();
	
	
	/**
	 * 获取玩家商店
	 * @param acitivty
	 * @return
	 */
	public PlayerShopValue getPlayerShop(int shopId) {
		return shopMap.getOrDefault(shopId, null);
	}
	public void refreshShop(int shopId){
		this.getPlayerShop(shopId).refresh(super.Context());
		SetChanged();
	}
	//开启商店
	public void unlockShop(int shopId){
		this.getPlayerShop(shopId).unlockShop(super.Context());
		SetChanged();
	}
	//解锁商店
	public void unlockShop(PlayerShopValue playerShopValue) {
		this.shopMap.put(playerShopValue.getShopId(), playerShopValue);
		//刷新商店物品
		unlockShop(playerShopValue.getShopId());
		playerShopValue.checkReset(super.Context());
		//检查刷新
		SetChanged();
	}
	//商店是否解锁
	public boolean isUnlockShop(int shopId) {
		return this.shopMap.containsKey(shopId);
	}
	//登录检查
	public boolean loginCheck(){
		boolean flag = false;
		for(PlayerShopValue playerShopValue:shopMap.values()){
			if(playerShopValue.checkReset(super.Context())){
				flag = true;
			}
		}
		if(flag){
			SetChanged();
		}
		return flag;
	}
	
	//登录检查
	public void resetDay(){
		for(PlayerShopValue playerShopValue:shopMap.values()){
			playerShopValue.resetDay(super.Context());
		}
		SetChanged();
	}
	
	@Override
	protected void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerShop", this);
	}
	public void init() {
		SpringUtil.getBean(ShopConfig.class);
	}
	
}
