package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.*;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.*;
import com.hm.config.excel.templaextra.*;
import com.hm.model.item.Items;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description: 简易活动配置
 * @author xjt
 * @date 2019年11月12日13:45:08
 * @version V1.0
 */
@Config ( "ActivitySimpleConfig" )
public class ActivitySimpleConfig extends ExcleConfig {
	// 充值狂欢
	private Map<Integer, ActiveRechargeCarnivalTemplate> rechargeCarnivalMap = Maps.newConcurrentMap();
	private Map<Integer, KfRechargeStageTemplate> rechargeStageMap = Maps.newConcurrentMap();
	// 消费狂欢
	private Map<Integer, ActiveConsumeCarnivalTemplate> consumeCarnivalMap = Maps.newConcurrentMap();
	//圣诞活动
	private Map<Integer,ActiveChirstWishTemplate> wishMap = Maps.newConcurrentMap();
	private Map<Integer,ActivityChirstWishItemTemplate> wishItemMap = Maps.newConcurrentMap();
	private Map<Integer,ActivityChirstProgressTemplate> wishProgressMap = Maps.newConcurrentMap();
	private Map<Integer,ActivityChirstDressTemplate> wishDressMap = Maps.newConcurrentMap();
	private Table<Integer,Integer,ActivityChirstDressTemplate>  wishDressTable = HashBasedTable.create();
	private List<ActivityChirstClickRewardTemplate> christGifts = Lists.newArrayList();
	//每周充值
	private Map<Integer,ActiveRechargeWeeklyTemplate> rechargeWeekMap = Maps.newConcurrentMap();
	//单笔充值
	private Map<Integer,ActiveRechargeSingleTemplate> rechargeOnceMap = Maps.newConcurrentMap();
	//充值金砖奖励-单笔充值
	private ArrayListMultimap<Integer, ActiveRechargeSingleTemplate> rechargeGoldTemplateMap = ArrayListMultimap.create();
	// 新版充值狂欢
	private Map<Integer, Active120RechargeTemplateImpl> recharge120Map = Maps.newConcurrentMap();
	//疯狂百抽
	private Map<Integer,ActivityCrazeDrawTemplate> crazeMap = Maps.newConcurrentMap();
	private Map<Integer, ActiveNewplayerTreasureRateTemplate> act2055cfgMap = Maps.newConcurrentMap();
	@Override
	public void loadConfig() {
//		loadRechargeCarnivalConfig();
//		loadConsumeCarnivalConfig();
//		loadWishDressConfig();
//		loadChristConfig();
//		loadWishItemConfig();
//		loadWishProgressConfig();
//		loadChristGiftConfig();
//		loadRechargeWeekConfig();
//		loadRechargeOnceConfig();
//		loadRecharge120Config();
//		loadKfRechargeStage();
//		loadCrazeDrawCardConfig();
//		loadAct2055Config();
	}

	private void loadAct2055Config() {
		List<ActiveNewplayerTreasureRateTemplate> list = JSONUtil.fromJson(getJson(ActiveNewplayerTreasureRateTemplate.class), new TypeReference<List<ActiveNewplayerTreasureRateTemplate>>() {
		});
		Map<Integer, ActiveNewplayerTreasureRateTemplate> map = list.stream().collect(Collectors.toMap(ActiveNewplayerTreasureRateTemplate::getId, Function.identity()));
		act2055cfgMap = ImmutableMap.copyOf(map);
	}

	private void loadCrazeDrawCardConfig(){
		List<ActivityCrazeDrawTemplate> list = JSONUtil.fromJson(getJson(ActivityCrazeDrawTemplate.class), new TypeReference<List<ActivityCrazeDrawTemplate>>() {});
		list.forEach(e -> e.init());
		Map<Integer,ActivityCrazeDrawTemplate> templateMap = list.stream().collect(Collectors.toMap(ActivityCrazeDrawTemplate::getId, Function.identity()));
		this.crazeMap = ImmutableMap.copyOf(templateMap);
	}

	private void loadRechargeOnceConfig() {
		List<ActiveRechargeSingleTemplate> list = JSONUtil.fromJson(getJson(ActiveRechargeSingleTemplate.class), new TypeReference<List<ActiveRechargeSingleTemplate>>() {});
		ArrayListMultimap<Integer, ActiveRechargeSingleTemplate> rechargeTemplateMap = ArrayListMultimap.create();
		list.forEach(e -> {
			e.init();
			rechargeTemplateMap.put(e.getRecharge_gold(), e);
		});
		Map<Integer,ActiveRechargeSingleTemplate> templateMap = list.stream().collect(Collectors.toMap(ActiveRechargeSingleTemplate::getId, Function.identity()));
		this.rechargeOnceMap = ImmutableMap.copyOf(templateMap);
		this.rechargeGoldTemplateMap = rechargeTemplateMap;
	}

	private void loadRechargeWeekConfig() {
		List<ActiveRechargeWeeklyTemplate> list = JSONUtil.fromJson(getJson(ActiveRechargeWeeklyTemplate.class), new TypeReference<List<ActiveRechargeWeeklyTemplate>>() {});
		list.forEach(e -> e.init());
		Map<Integer,ActiveRechargeWeeklyTemplate> templateMap = list.stream().collect(Collectors.toMap(ActiveRechargeWeeklyTemplate::getId, Function.identity()));
		this.rechargeWeekMap = ImmutableMap.copyOf(templateMap);
	}

	public void loadRechargeCarnivalConfig(){
		Map<Integer, ActiveRechargeCarnivalTemplate> rechargeCarnivalMap = Maps.newConcurrentMap();
		List<ActiveRechargeCarnivalTemplate> list = JSONUtil.fromJson(getJson(ActiveRechargeCarnivalTemplate.class), new TypeReference<List<ActiveRechargeCarnivalTemplate>>() {});
		list.forEach(t ->t.init());
		rechargeCarnivalMap = list.stream().collect(Collectors.toMap(ActiveRechargeCarnivalTemplate::getId, Function.identity()));
		this.rechargeCarnivalMap = ImmutableMap.copyOf(rechargeCarnivalMap);
	}

	public void loadRecharge120Config(){
		Map<Integer, Active120RechargeTemplateImpl> rechargeCarnivalMap = Maps.newConcurrentMap();
		List<Active120RechargeTemplateImpl> list = JSONUtil.fromJson(getJson(Active120RechargeTemplateImpl.class), new TypeReference<List<Active120RechargeTemplateImpl>>() {});
		list.forEach(t ->t.init());
		rechargeCarnivalMap = list.stream().collect(Collectors.toMap(Active120RechargeTemplateImpl::getId, Function.identity()));
		this.recharge120Map = ImmutableMap.copyOf(rechargeCarnivalMap);
	}
	
	public void loadConsumeCarnivalConfig(){
		Map<Integer, ActiveConsumeCarnivalTemplate> consumeCarnivalMap = Maps.newConcurrentMap();
		List<ActiveConsumeCarnivalTemplate> list = JSONUtil.fromJson(getJson(ActiveConsumeCarnivalTemplate.class), new TypeReference<List<ActiveConsumeCarnivalTemplate>>() {});
		list.forEach(t ->t.init());
		consumeCarnivalMap = list.stream().collect(Collectors.toMap(ActiveConsumeCarnivalTemplate::getId, Function.identity()));
		this.consumeCarnivalMap = ImmutableMap.copyOf(consumeCarnivalMap);
	}
	
	public void loadChristConfig(){
		List<ActiveChirstWishTemplate> list = JSONUtil.fromJson(getJson(ActiveChirstWishTemplate.class), new TypeReference<List<ActiveChirstWishTemplate>>() {});
		Map<Integer,ActiveChirstWishTemplate> wishMap = Maps.newConcurrentMap();
		wishMap = list.stream().collect(Collectors.toMap(ActiveChirstWishTemplate::getId, Function.identity()));
		this.wishMap = ImmutableMap.copyOf(wishMap);
	}
	
	public void loadWishItemConfig(){
		List<ActivityChirstWishItemTemplate> list = JSONUtil.fromJson(getJson(ActivityChirstWishItemTemplate.class), new TypeReference<List<ActivityChirstWishItemTemplate>>() {});
		list.forEach(t ->t.init());
		Map<Integer,ActivityChirstWishItemTemplate> wishItemMap = Maps.newConcurrentMap();
		wishItemMap = list.stream().collect(Collectors.toMap(ActivityChirstWishItemTemplate::getId, Function.identity()));
		this.wishItemMap = ImmutableMap.copyOf(wishItemMap);
	}
	
	public void loadWishDressConfig(){
		Table<Integer,Integer,ActivityChirstDressTemplate>  wishDressTable = HashBasedTable.create();
		List<ActivityChirstDressTemplate> list = JSONUtil.fromJson(getJson(ActivityChirstDressTemplate.class), new TypeReference<List<ActivityChirstDressTemplate>>() {});
		Map<Integer,ActivityChirstDressTemplate> wishDressMap = Maps.newConcurrentMap();
		list.forEach(t ->t.init());
		wishDressMap = list.stream().collect(Collectors.toMap(ActivityChirstDressTemplate::getId, Function.identity()));
		for(ActivityChirstDressTemplate template:list){
			wishDressTable.put(template.getWish_id(), template.getLocation(), template);
		}
		this.wishDressMap = ImmutableMap.copyOf(wishDressMap);
		this.wishDressTable = ImmutableTable.copyOf(wishDressTable);
	}
	
	public void loadWishProgressConfig(){
		List<ActivityChirstProgressTemplate> list = JSONUtil.fromJson(getJson(ActivityChirstProgressTemplate.class), new TypeReference<List<ActivityChirstProgressTemplate>>() {});
		list.forEach(t ->t.init());
		Map<Integer,ActivityChirstProgressTemplate> wishProgressMap = Maps.newConcurrentMap();
		wishProgressMap = list.stream().collect(Collectors.toMap(ActivityChirstProgressTemplate::getId, Function.identity()));
		this.wishProgressMap = ImmutableMap.copyOf(wishProgressMap);
	}
	
	public void loadChristGiftConfig(){
		List<ActivityChirstClickRewardTemplate> list = JSONUtil.fromJson(getJson(ActivityChirstClickRewardTemplate.class), new TypeReference<List<ActivityChirstClickRewardTemplate>>() {});
		list.forEach(t ->t.init());
		this.christGifts = ImmutableList.copyOf(list);
	}

	public void loadKfRechargeStage(){
		List<KfRechargeStageTemplate> list = JSONUtil.fromJson(getJson(KfRechargeStageTemplate.class), new TypeReference<List<KfRechargeStageTemplate>>() {});
		Map<Integer, KfRechargeStageTemplate> collect = list.stream().collect(Collectors.toMap(KfRechargeStageTemplate::getId, Function.identity()));
		this.rechargeStageMap = ImmutableMap.copyOf(collect);
	}
	
	@Override
	public List<String> getDownloadFile() {
		return getConfigName(ActiveRechargeCarnivalTemplate.class,
				ActiveChirstWishTemplate.class,
				ActivityChirstDressTemplate.class,
				ActivityChirstWishItemTemplate.class,
				ActivityChirstProgressTemplate.class,
				ActivityChirstClickRewardTemplate.class,
				ActiveRechargeWeeklyTemplate.class,
				ActiveRechargeSingleTemplate.class,
				ActiveConsumeCarnivalTemplate.class,
				Active120RechargeTemplateImpl.class,
				KfRechargeStageTemplate.class,
				ActivityCrazeDrawTemplate.class,
				ActiveNewplayerTreasureRateTemplate.class
				);
	}
	
	public ActiveRechargeCarnivalTemplate getRechargeCarnivalTemplate(int id){
		return this.rechargeCarnivalMap.get(id);
	}

	public Active120RechargeTemplateImpl getRecharge120Template(int id){
		return this.recharge120Map.get(id);
	}
	
	public ActiveConsumeCarnivalTemplate getConsumeCarnivalTemplate(int id){
		return this.consumeCarnivalMap.get(id);
	}

	public ActiveChirstWishTemplate getWish(int id) {
		return wishMap.get(id);
	}
	
	public ActivityChirstWishItemTemplate getWishItem(int id) {
		return wishItemMap.get(id);
	}
	
	public ActivityChirstDressTemplate getWishDress(int id,int index) {
		return wishDressTable.get(id, index);
	}

	public ActivityChirstProgressTemplate getWishProgress(int id) {
		return wishProgressMap.get(id);
	}
	
	public ActiveRechargeWeeklyTemplate getRechargeWeek(int id) {
		return rechargeWeekMap.get(id);
	}
	
	public ActiveRechargeSingleTemplate getRechargeOnce(int id) {
		return rechargeOnceMap.get(id);
	}

	public KfRechargeStageTemplate getKfRechargeStage(int stage) {
		return rechargeStageMap.get(stage);
	}
	
	public ActiveRechargeSingleTemplate getRechargeOnce(int gold,int playerLv) {
		Collection<ActiveRechargeSingleTemplate> templates = rechargeGoldTemplateMap.get(gold);
		for (ActiveRechargeSingleTemplate template : templates) {
			if(template.isFit(playerLv)) {
				return template;
			}
		}
		return null;
	}
	
	
	/**
	 * 根据服务器等级和类型获取圣诞老人奖励
	 * @param lv
	 * @param type
	 * @return
	 */
	public Items getChristGift(int lv,int type){
		Optional<ActivityChirstClickRewardTemplate> o =this.christGifts.stream().filter(t ->t.isFit(lv,type)).findFirst();
		if(!o.isPresent()){
			return null;
		}
		return o.get().randomReward();
	}
	
	/**
	 * 根据服务器等级和类型获取圣诞老人奖励
	 * @param lv
	 * @param type
	 * @return
	 */
	public List<Items> getChristGiftRewards(int lv,int type){
		Optional<ActivityChirstClickRewardTemplate> o =this.christGifts.stream().filter(t ->t.isFit(lv,type)).findFirst();
		if(!o.isPresent()){
			return null;
		}
		return o.get().getRewards();
	}

	public ActivityCrazeDrawTemplate getCrazeDrawTemplate(int id){
		return crazeMap.get(id);
	}

	public ActiveNewplayerTreasureRateTemplate getAct2055Cfg(int id) {
		return act2055cfgMap.getOrDefault(id, null);
	}
	public int getAct2055MaxId() {
		return act2055cfgMap.keySet().stream().max(Comparator.naturalOrder()).orElse(0);
	}
	
}


