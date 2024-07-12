package com.hm.model.shop;

import com.google.common.collect.Lists;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.GameConstants;
import com.hm.config.excel.ShopConfig;
import com.hm.config.excel.templaextra.ShopItemExtraTemplate;
import com.hm.config.excel.templaextra.ShopTypeExtraTemplate;
import com.hm.enums.ShopType;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class LevelShop extends PlayerShopValue {
	private int lv;
	private List<Integer> buys = Lists.newArrayList();
	private ConcurrentHashMap<Integer, Integer> map = new  ConcurrentHashMap<Integer, Integer>();
	public LevelShop() {
		super();
	}
	
	public LevelShop(ShopType shopType){
		super(shopType);
	}
	
	public int getLv() {
		return lv;
	}

	@Override
	public void unlockShop(BasePlayer player) {
		ShopConfig shopConfig = SpringUtil.getBean(ShopConfig.class);
		this.map = shopConfig.referenceShop(this.getShopId(),player.playerLevel().getLv());
		ShopTypeExtraTemplate template = shopConfig.getShopTypeTemplate(this.getShopId());
		this.lv = template.getUnlock_level();
		this.setEndTime(System.currentTimeMillis()+template.getExist_time()*GameConstants.DAY);
	}
	//是否能购买
	@Override
	public boolean isCanBuy(Player player, ShopItemExtraTemplate template, int num){
		if (template.getDay_num() > 0 && num > template.getDay_num()){
			return false;
		}
		return !buys.contains(template.getIndex());
	}
	//设置购买数据
	@Override
	public void setShopBuyData(Player player, int id, int num) {
		this.buys.add(id);
		player.playerShop().SetChanged();
	}
	public void resetDay(BasePlayer player){
		this.buys.clear();
	}
	public void unlockRepeat(Player player,String param){
		ShopConfig shopConfig = SpringUtil.getBean(ShopConfig.class);
		this.map = shopConfig.referenceShop(this.getShopId(),player.playerLevel().getLv());
		this.setNextRestTime(0);
		int lv = Integer.parseInt(param.split(":")[0]);
		int days = Integer.parseInt(param.split(":")[1]);
		this.lv = lv;
		this.setEndTime(System.currentTimeMillis()+days*GameConstants.DAY);
	}
}
