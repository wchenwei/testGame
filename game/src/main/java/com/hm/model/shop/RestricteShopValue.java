package com.hm.model.shop;

import com.google.common.collect.Lists;
import com.hm.config.excel.templaextra.ShopItemExtraTemplate;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.ShopConfig;
import com.hm.config.excel.templaextra.ShopTypeExtraTemplate;
import com.hm.enums.ShopType;
import com.hm.model.item.Items;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * 杂货铺类型的商店，用货币刷新，每个商品限购一次的类型
 * @author xjt
 *
 */
public class RestricteShopValue extends PlayerShopValue {
	private int refreshCount;//刷新次数
	private List<Integer> buys = Lists.newArrayList();
	private ConcurrentHashMap<Integer, Integer> map = new  ConcurrentHashMap<Integer, Integer>();
	//每天刷出商品的次数
	private ConcurrentHashMap<Integer,Integer> referenceMap = new ConcurrentHashMap<Integer, Integer>();
	public RestricteShopValue() {
		super();
	}
	public RestricteShopValue(ShopType shopType){
		super(shopType);
	}
	@Override
	public void refresh(BasePlayer player) {
		refreshOnce(player);
		this.refreshCount++;
	}

	@Override
	public void refreshOnce(BasePlayer player) {
		ShopConfig shopConfig = SpringUtil.getBean(ShopConfig.class);
		this.map = shopConfig.referenceShop(this.getShopId(),player.playerLevel().getLv(),this.referenceMap);
		addRefreshMap(map);
		this.buys.clear();
	}

	private void addRefreshMap(Map<Integer, Integer> map) {
		for(int goodsId:map.values()){
			int count = referenceMap.getOrDefault(goodsId, 0);
			this.referenceMap.put(goodsId, count+1);
		}
	}
	@Override
	public void unlockShop(BasePlayer player) {
		ShopConfig shopConfig = SpringUtil.getBean(ShopConfig.class);
		this.map = shopConfig.referenceShop(this.getShopId(),player.playerLevel().getLv());
	}
	
	//是否能购买
	@Override
	public boolean isCanBuy(Player player, ShopItemExtraTemplate template, int num){
		if (template.getDay_num() > 0 && num > template.getDay_num()){
			return false;
		}
		if(buys.contains(template.getIndex())){
			return false;
		}
		return true;
	}
	//设置购买数据
	@Override
	public void setShopBuyData(Player player, int id, int num) {
		this.buys.add(id);
		player.playerShop().SetChanged();
	}
	@Override
	public int getGoodsId(int id) {
		return map.getOrDefault(id, 0);
	}
	@Override
	public Items getRefershCost(){
		ShopConfig shopConfig = SpringUtil.getBean(ShopConfig.class);
		ShopTypeExtraTemplate template = shopConfig.getShopTypeTemplate(this.getShopId());
		if(template==null){
			return null;
		}
		return template.getCostItem(this.refreshCount);
	}
	@Override
	public boolean isCanRefersh(){
		ShopConfig shopConfig = SpringUtil.getBean(ShopConfig.class);
		ShopTypeExtraTemplate template = shopConfig.getShopTypeTemplate(this.getShopId());
		if(template==null){
			return false;
		}
		Integer refreshNum = template.getRefresh_num();
		if (refreshNum < 0){// 不做次数限制
			return true;
		}
		return refreshCount < refreshNum;
	}
	public void resetDay(BasePlayer player){
		this.refreshCount = 0;
		this.referenceMap.clear();
	}
	public void reset(BasePlayer player){
		this.refreshCount = 0;
	}
	
	

}
