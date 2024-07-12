package com.hm.model.shop;

import com.hm.config.excel.templaextra.ShopItemExtraTemplate;
import com.hm.enums.ShopType;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;

public class GuildShopValue extends PlayerShopValue {
	private int lastBuyCount = 10;//剩余购买次数
	
	public GuildShopValue() {
		super();
	}
	public GuildShopValue(ShopType shopType) {
		super(shopType);
	}
	@Override
	public void refresh(BasePlayer player) {
		this.lastBuyCount = 10;//实际应该按玩家部落等级获得次数
	}
	@Override
	public void unlockShop(BasePlayer player) {
		refresh(player);
	}
	//是否能购买
	@Override
	public boolean isCanBuy(Player player, ShopItemExtraTemplate template, int num){
		return this.lastBuyCount>=num;
	}
	//设置购买数据
	@Override
	public void setShopBuyData(Player player, int id, int num) {
		this.lastBuyCount-=num;
		player.playerShop().SetChanged();
	}
	@Override
	public void resetDay(BasePlayer player){
		refresh(player);
	}

}
