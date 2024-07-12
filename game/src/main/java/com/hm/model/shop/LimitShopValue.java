package com.hm.model.shop;

import com.google.common.collect.Maps;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.ShopConfig;
import com.hm.config.excel.templaextra.ShopItemExtraTemplate;
import com.hm.enums.ShopType;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;

import java.util.Map;
/**
 * 限制购买次数类型的商店
 * @author xjt
 *
 */
public class LimitShopValue extends PlayerShopValue {
	//购买过的shopId,次数
	private Map<Integer,Integer> buyMap = Maps.newConcurrentMap();
	public LimitShopValue(){
		super();
	}
	public LimitShopValue(ShopType shopType){
		super(shopType);
	}
	
	//是否能购买
	@Override
	public boolean isCanBuy(Player player, ShopItemExtraTemplate template, int num){
		int buyCount = this.buyMap.getOrDefault(template,0);
		if(template.getDay_num()<=0){
			return true;
		}
		return buyCount+num <= template.getDay_num();
	}
	//设置购买数据
	@Override
	public void setShopBuyData(Player player, int id, int num) {
		int buyCount = this.buyMap.getOrDefault(id, 0)+num;
		this.buyMap.put(id, buyCount);
		player.playerShop().SetChanged();
	}
	@Override
	public void resetDay(BasePlayer player){
		refresh(player);
	}
	@Override
	public void refresh(BasePlayer player) {
		this.buyMap.clear();
	}
}
