/**  
 * Project Name:SLG_GameFuture.  
 * File Name:PlayerLeadConfig.java  
 * Package Name:com.hm.config.excel  
 * Date:2017年12月29日上午9:25:30  
 * Copyright (c) 2017, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
*/  
  
package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.*;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.libcore.util.string.StringUtil;
import com.hm.config.excel.temlate.ShopTypeTemplate;
import com.hm.config.excel.temlate.VipTemplate;
import com.hm.config.excel.templaextra.ActivityShopTemplate;
import com.hm.config.excel.templaextra.ShopGoodsExtraTemplate;
import com.hm.config.excel.templaextra.ShopItemExtraTemplate;
import com.hm.config.excel.templaextra.ShopTypeExtraTemplate;
import com.hm.enums.ShopType;
import com.hm.model.player.BasePlayer;
import com.hm.model.shop.PlayerShopValue;
import com.hm.model.weight.GoodsDrop;
import com.hm.util.MathUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description: 商店配置
 * @author xjt  
 * @date 2018年10月22日10:24:14
 * @version V1.0
 */
@Slf4j
@Config
public class ShopConfig extends ExcleConfig {
	//key: shopId,position   value:不同等级的掉落权重
	private Table<Integer,Integer,List<ShopItemExtraTemplate>> itemTable = HashBasedTable.create();
	private Map<Integer,ShopItemExtraTemplate> itemMap = Maps.newConcurrentMap();
	private Map<Integer,ShopTypeExtraTemplate> shopTypeMap = Maps.newConcurrentMap();
	private Map<Integer,ShopGoodsExtraTemplate> goodsMap = Maps.newConcurrentMap();
	private Map<Integer, VipTemplate> vipShopMap = Maps.newConcurrentMap();
	
	private Map<Integer,ActivityShopTemplate> activityShopMap = Maps.newConcurrentMap();
	private Table<Integer,Integer,ActivityShopTemplate> shopTable = HashBasedTable.create();

	private int maxVipLv;
	
	@Override
	public void loadConfig() {
		loadShopItemConfig();
		loadShopTypeConfig();
		loadShopGoodsConfig();
		loadVipConfig();
		loadActivityShopConfig();
	}
	
	private void loadVipConfig() {
		try {
			Map<Integer,VipTemplate> goodsMap = Maps.newConcurrentMap();
			for(VipTemplate template:JSONUtil.fromJson(getJson(VipTemplate.class), new TypeReference<ArrayList<VipTemplate>>(){})){
				template.init();
				goodsMap.put(template.getLv_vip(), template);
			}
			this.vipShopMap = ImmutableMap.copyOf(goodsMap);
			this.maxVipLv = MathUtils.max(vipShopMap.keySet());
			log.info("vip加载完成");
		} catch (Exception e) {
			log.info("vip加载失败");
		}
		
	}
	
	private void loadShopGoodsConfig() {
		try {
			Map<Integer,ShopGoodsExtraTemplate> goodsMap = Maps.newConcurrentMap();
			for(ShopGoodsExtraTemplate template:JSONUtil.fromJson(getJson(ShopGoodsExtraTemplate.class), new TypeReference<ArrayList<ShopGoodsExtraTemplate>>(){})){
				template.init();
				goodsMap.put(template.getId(), template);
			}
			this.goodsMap = ImmutableMap.copyOf(goodsMap);
			log.info("商品表加载完成");
		} catch (Exception e) {
			log.info("商品表加载失败");
		}
		
	}
	private void loadShopTypeConfig() {
		try {
			Map<Integer,ShopTypeExtraTemplate> shopTypeMap = Maps.newConcurrentMap();
			for(ShopTypeExtraTemplate template:loadShopTypeFile()) {
				template.init();
				shopTypeMap.put(template.getShop_id(), template);
			}
			this.shopTypeMap = ImmutableMap.copyOf(shopTypeMap);
			log.info("商店类型加载完成");
		} catch (Exception e) {
			log.info("商店类型加载失败");
		}
		
	}
	private void loadShopItemConfig() {
		try {
			Table<Integer,Integer,List<ShopItemExtraTemplate>> itemTable = HashBasedTable.create();
			 Map<Integer,ShopItemExtraTemplate> itemMap = Maps.newConcurrentMap();
			for(ShopItemExtraTemplate template:loadFile()) {
				template.init();
				int shopId = template.getShop_id();
				int position = template.getPosition();
				List<ShopItemExtraTemplate> templist = itemTable.contains(shopId, position)?itemTable.get(shopId, position):Lists.newArrayList();
				templist.add(template);
				itemTable.put(shopId, position, templist);
				itemMap.put(template.getIndex(), template);
			}
			this.itemTable = ImmutableTable.copyOf(itemTable);
			this.itemMap = ImmutableMap.copyOf(itemMap);
			log.info("商店物品加载完成");
		} catch (Exception e) {
			log.info("商店物品加载失败");
		}
		
	}
	
	private void loadActivityShopConfig() {
		try {
			Map<Integer,ActivityShopTemplate> activityShopMap = Maps.newConcurrentMap();
			Table<Integer,Integer,ActivityShopTemplate> tempShopTable = HashBasedTable.create();
			List<ActivityShopTemplate> list = JSONUtil.fromJson(getJson(ActivityShopTemplate.class), new TypeReference<ArrayList<ActivityShopTemplate>>(){});
			list.forEach(t ->{
				t.init();
				tempShopTable.put(t.getShop_id(),t.getId(),t);
			});
			activityShopMap = list.stream().collect(Collectors.toMap(ActivityShopTemplate::getId, Function.identity()));
			this.activityShopMap = ImmutableMap.copyOf(activityShopMap);
			this.shopTable = tempShopTable;
			log.info("活动商店加载成功");
		} catch (Exception e) {
			log.info("活动商店加载失败");
		}
	}
	private List<ShopItemExtraTemplate> loadFile() {
		return JSONUtil.fromJson(getJson(ShopItemExtraTemplate.class), new TypeReference<ArrayList<ShopItemExtraTemplate>>(){});
	}
	private List<ShopTypeExtraTemplate> loadShopTypeFile() {
		return JSONUtil.fromJson(getJson(ShopTypeExtraTemplate.class), new TypeReference<ArrayList<ShopTypeExtraTemplate>>(){});
	}
	@Override
	public List<String> getDownloadFile() {
		return getConfigName(VipTemplate.class,ShopItemExtraTemplate.class,
				ShopTypeExtraTemplate.class,
				ShopGoodsExtraTemplate.class,
				ActivityShopTemplate.class);
	}
	
	public ShopTypeExtraTemplate getShopTypeTemplate(int shopId){
		return shopTypeMap.get(shopId);
	}
	
	public ShopItemExtraTemplate getShopTemplate(int id){
		return itemMap.get(id);
	}
	//获取商店的商品配置,每个位置取一个符合当前等级的商品权重信息
	public List<ShopItemExtraTemplate> getGoods(int shopId,int lv){
		List<ShopItemExtraTemplate> result = Lists.newArrayList();
		Map<Integer,List<ShopItemExtraTemplate>> shopMap = itemTable.row(shopId);
		for(List<ShopItemExtraTemplate> list:shopMap.values()){
			List<ShopItemExtraTemplate> templist = list.stream().filter(t -> lv>=t.getMinLv()&&lv<=t.getMaxLv()).collect(Collectors.toList());
			if(!templist.isEmpty()){
				result.add(templist.get(0));
			}
		}
		return result;
	}
	//根据玩家等级生成商店物品
	public ConcurrentHashMap<Integer,Integer> referenceShop(int shopId,int lv,Map<Integer,Integer> referenceMap){
		ShopTypeExtraTemplate typeTemplate = getShopTypeTemplate(shopId);
		ConcurrentHashMap<Integer,Integer> map = new ConcurrentHashMap<Integer, Integer>();
		List<ShopItemExtraTemplate> list = getGoods(shopId,lv);
		for(ShopItemExtraTemplate template:list){
			Set<Integer> filterIds = new HashSet<Integer>();
			int goodsId = template.getDrop().randomDropId();
			int limitCount = typeTemplate.getGoodsLimit(goodsId);
			//循环查找满足条件的goods,最多查找10次
			for(int i=0;i<10;i++) {
				if(limitCount<=0) {
					break;
				}
				if(referenceMap.getOrDefault(goodsId, 0)<limitCount) {
					break;
				}
				filterIds.add(goodsId);
				GoodsDrop goodsDrop = new GoodsDrop(template.getGoods(),template.getRate(),Lists.newArrayList(filterIds));
				goodsId = goodsDrop.randomDropId();
				limitCount = typeTemplate.getGoodsLimit(goodsId);
			}
			
			map.put(template.getIndex(), goodsId);
		}
		return map;
	}
	//根据玩家等级生成商店物品
	public ConcurrentHashMap<Integer,Integer> referenceShop(int shopId,int lv){
		return referenceShop(shopId, lv, Maps.newConcurrentMap());
	}
	//检查是否需要开启商店
	public void checkShopOpen(BasePlayer player){
		int lv = player.playerCommander().getMilitaryLv();
		List<ShopTypeTemplate> list=this.shopTypeMap.values().stream().filter(t -> lv>=t.getUnlock_level()&&!player.playerShop().isUnlockShop(t.getShop_id())).collect(Collectors.toList());
		for(ShopTypeTemplate template:list){
			ShopType shopType= ShopType.getShopType(template.getShop_id());
			if(shopType!=null){
				PlayerShopValue playerShopValue = shopType.getPlayerShopValue();
				player.playerShop().unlockShop(playerShopValue);
			}
		}
	}
	public List<Integer> getShopRestHour(int shopType){
		ShopTypeTemplate template = getShopTypeTemplate(shopType);
		if(template==null){
			return Lists.newArrayList();
		}
		return StringUtil.splitStr2IntegerList(template.getReset_time(), ",");
	}
	
	public ShopGoodsExtraTemplate getGoodsTemplate(int id){
		return this.goodsMap.get(id);
	}
	
	public VipTemplate getVipTemplate(int id) {
		return vipShopMap.get(id);
	}
	public int getMaxVipLv() {
		return maxVipLv;
	}
	public int getVipLevel(int vipLv,long exp) {
		if(vipLv >= this.maxVipLv) {
			return this.maxVipLv;
		}
		while(true) {
			if(exp >= vipShopMap.get(vipLv+1).getExp_vip()) {
				vipLv ++;
				if(vipLv >= this.maxVipLv) {
					break;
				}
			}else {
				break;
			}
		}
		return Math.min(this.maxVipLv, vipLv);
	}
	
	public ActivityShopTemplate getActiviyShopTemplate(int id){
		return this.activityShopMap.get(id);
	}

	public ActivityShopTemplate getActiviyShopTemplate(int shopId,int id){
		return this.shopTable.get(shopId,id);
	}


	public ActivityShopTemplate getTemplateByShopId(int shopId, int playerLv, int stage){
		ActivityShopTemplate template = activityShopMap.values().stream().filter(e -> e.getShop_id() == shopId && e.isFit(playerLv) && e.getRound() == stage).findFirst().orElse(null);
		return template;
	}


	
}
  
