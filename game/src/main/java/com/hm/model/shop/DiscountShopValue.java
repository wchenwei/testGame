package com.hm.model.shop;


import com.hm.enums.ShopType;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
/**
 * 折扣商店，类似配件材料商店，不限购，折扣每购买一次递增
 * @author Administrator
 *
 */
public class DiscountShopValue extends PlayerShopValue{
	private int buyCount;
	public DiscountShopValue(){
		super();
	}
	public DiscountShopValue(ShopType shopType){
		super(shopType);
	}
	@Override
	public void refresh(BasePlayer player) {
		this.buyCount=0;
	}
	@Override
	public int getShopDiscount(int id) {
		return Math.min(100, (buyCount+1)*10);
	}
	//设置购买数据
	@Override
	public void setShopBuyData(Player player, int id, int num) {
		this.buyCount += num;
		player.playerShop().SetChanged();
	}
	@Override
	public void resetDay(BasePlayer player){
		refresh(player);
	}

}
