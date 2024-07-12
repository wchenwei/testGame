package com.hm.config;

import com.alibaba.fastjson.TypeReference;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.temlate.*;
import com.hm.config.excel.templaextra.Active771MovieTemplateImpl;
import com.hm.config.excel.templaextra.Active771PassTemplateImpl;
import com.hm.config.excel.templaextra.Active771RechargeTemplateImpl;
import com.hm.config.excel.templaextra.Active771RewardOnceTemplateImpl;
import com.hm.model.item.Items;

import java.util.*;
import java.util.stream.Collectors;
import com.google.common.collect.*;
@Config
public class ValentineDayNewConfig extends ExcleConfig {
	private Table<Integer,Integer, Active771Template> valentineTable = HashBasedTable.create();
	private Table<Integer,Integer, Active771MovieTemplateImpl> movieTable = HashBasedTable.create();
	private Map<Integer,Active771MovieTemplateImpl> movieMap = Maps.newConcurrentMap();
	private Map<Integer,Active771RewardOnceTemplateImpl> onceRewards = Maps.newConcurrentMap();
	private Map<Integer, Active771GiftTemplate> giftMap = Maps.newConcurrentMap();
	private Table<Integer, Integer,Active771GiftTemplate> giftIds = HashBasedTable.create();

	private Map<Integer, Active771PassTemplateImpl> passMap = Maps.newConcurrentMap();
	private Map<Integer, Active771PassLevelTemplate> passLevelMap = Maps.newConcurrentMap();
	private int passLevelMax = 0;
	private Map<Integer, Active771PassGiftTemplate> passgiftMap = Maps.newConcurrentMap();
	private Map<Integer, Active771RechargeTemplateImpl> rechargeMap = Maps.newConcurrentMap();
	private Table<Integer,Integer, Active771RechargeTemplateImpl> rechargeTable = HashBasedTable.create();
	/* passMap passLevelMap passgiftMap rechargeMap
	Active771PassTemplateImpl
	Active771PassLevelTemplate
	Active771PassGiftTemplate*/


	@Override
	public void loadConfig() {
//		loadValentineMain();
//		loadValentineMovie();
//		loadOnceRewardConfig();
//		loadGiftMapConfig();
//		loadPass();
//		loadPassLevel();
//		loadPassGift();
//		loadRecharge();
	}

	private void loadGiftMapConfig() {
		List<Active771GiftTemplate> list = JSONUtil.fromJson(getJson(Active771GiftTemplate.class), new TypeReference<List<Active771GiftTemplate>>(){});
		Map<Integer,Active771GiftTemplate> giftMap = list.stream().collect(Collectors.toMap(Active771GiftTemplate::getId, e->e));
		this.giftMap = ImmutableMap.copyOf(giftMap);
		Table<Integer,Integer,Active771GiftTemplate> giftIdsTemp = HashBasedTable.create();
		list.forEach(e->{
			giftIdsTemp.put(e.getStage(),e.getRecharge_id(), e);
		});
		this.giftIds = ImmutableTable.copyOf(giftIdsTemp);
	}

	private void loadValentineMain() {
		Table<Integer,Integer,Active771Template> valentineTable = HashBasedTable.create();
		for(Active771Template template:JSONUtil.fromJson(getJson(Active771Template.class), new TypeReference<List<Active771Template>>(){})){
			valentineTable.put(template.getRound(),template.getId(), template);
		}
		this.valentineTable = ImmutableTable.copyOf(valentineTable);
	}

	private void loadValentineMovie() {
		Table<Integer,Integer,Active771MovieTemplateImpl> movieTable = HashBasedTable.create();
		Map<Integer,Active771MovieTemplateImpl> movieMap = Maps.newConcurrentMap();
		for(Active771MovieTemplateImpl template:JSONUtil.fromJson(getJson(Active771MovieTemplateImpl.class), new TypeReference<List<Active771MovieTemplateImpl>>(){})){
			template.init();
			movieMap.put(template.getId(), template);
			movieTable.put(template.getMovie(),template.getType(), template);
		}
		this.movieMap = ImmutableMap.copyOf(movieMap);
		this.movieTable = ImmutableTable.copyOf(movieTable);
	}

	private void loadOnceRewardConfig() {
		Map<Integer,Active771RewardOnceTemplateImpl> onceRewards = Maps.newConcurrentMap();
		for(Active771RewardOnceTemplateImpl template:JSONUtil.fromJson(getJson(Active771RewardOnceTemplateImpl.class), new TypeReference<List<Active771RewardOnceTemplateImpl>>(){})){
			template.init();
			onceRewards.put(template.getId(), template);
		}
		this.onceRewards = ImmutableMap.copyOf(onceRewards);
	}
	/*Active771PassTemplateImpl
	Active771PassLevelTemplate   passMap passLevelMap passgiftMap
	Active771PassGiftTemplate*/
	private void loadPass() {
		List<Active771PassTemplateImpl> list = JSONUtil.fromJson(getJson(Active771PassTemplateImpl.class), new TypeReference<List<Active771PassTemplateImpl>>(){});
		Map<Integer,Active771PassTemplateImpl> tempMap = Maps.newConcurrentMap();
		for(Active771PassTemplateImpl temp: list) {
			temp.init();
			tempMap.put(temp.getId(), temp);
		}
		this.passMap = ImmutableMap.copyOf(tempMap);
	}
	private void loadPassLevel() {
		List<Active771PassLevelTemplate> list = JSONUtil.fromJson(getJson(Active771PassLevelTemplate.class), new TypeReference<List<Active771PassLevelTemplate>>(){});
		Map<Integer,Active771PassLevelTemplate> giftMap = list.stream().collect(Collectors.toMap(Active771PassLevelTemplate::getId, e->e));
		this.passLevelMap = ImmutableMap.copyOf(giftMap);
		this.passLevelMax = Collections.max(giftMap.keySet());
	}
	private void loadPassGift() {
		List<Active771PassGiftTemplate> list = JSONUtil.fromJson(getJson(Active771PassGiftTemplate.class), new TypeReference<List<Active771PassGiftTemplate>>(){});
		Map<Integer,Active771PassGiftTemplate> giftMap = list.stream().collect(Collectors.toMap(Active771PassGiftTemplate::getRecharge_gift_id, e->e));
		this.passgiftMap = ImmutableMap.copyOf(giftMap);
	}
	//rechargeMap rechargeTable
	private void loadRecharge() {
		Map<Integer,Active771RechargeTemplateImpl> rechargeMapTemp = Maps.newConcurrentMap();
		Table<Integer,Integer,Active771RechargeTemplateImpl> rechargeTableTemp = HashBasedTable.create();
		for(Active771RechargeTemplateImpl template:JSONUtil.fromJson(getJson(Active771RechargeTemplateImpl.class), new TypeReference<List<Active771RechargeTemplateImpl>>(){})){
			template.init();
			rechargeMapTemp.put(template.getId(), template);
			rechargeTableTemp.put(template.getStage(), template.getId(), template);
		}
		this.rechargeMap = ImmutableMap.copyOf(rechargeMapTemp);
		this.rechargeTable = ImmutableTable.copyOf(rechargeTableTemp);
	}

	@Override
	public List<String> getDownloadFile() {
		 return getConfigName(Active771Template.class,
				 Active771MovieTemplateImpl.class,
				 Active771RewardOnceTemplateImpl.class,
				 Active771GiftTemplate.class,
				 Active771PassTemplateImpl.class,
				 Active771PassLevelTemplate.class,
				 Active771PassGiftTemplate.class,
				 Active771RechargeTemplate.class);
	}
	
	public Active771Template getValentine(int version,int id){
		return valentineTable.get(version, id);
	}

	public int getNextId(int version, int id) {
		Active771Template template = valentineTable.get(version, id);
		if(template!=null){
			return template.getNext_level();
		}
		return valentineTable.values().stream().filter(t->t.getRound()==version).mapToInt(t ->t.getId()).min().getAsInt();
	}

	public Optional<Active771MovieTemplateImpl> getCostTemplate(int id,int choice,int lv) {
		return this.movieMap.values().stream().filter(t ->t.isFit(id,choice,lv)).findFirst();
	}
	
	/**
	 * 获取一次性奖励(战斗,福利,幸运)
	 * @param type
	 * @param lv
	 * @return
	 */
	public List<Items> getOnceRewards(int type,int lv){
		Optional<Active771RewardOnceTemplateImpl> optional = this.onceRewards.values().stream().filter(t ->t.isFit(type,lv)).findFirst();
		if(optional.isPresent()){
			return optional.get().getRewards();
		}
		return Lists.newArrayList();
	}
	
	public boolean contains(int version, int giftid){
		return this.giftIds.contains(version, giftid);
	}

	public Active771PassGiftTemplate getPassGift(int giftid){
		return this.passgiftMap.getOrDefault(giftid, null);
	}

	/**
	 * 获取单个的累充奖励
	 * @param stage
	 * @param id
	 * @param index
	 * @param lv
	 * @param dayRecharge
	 * @return
	 */
	public List<Items> getRechargeReward(int stage, int id, int index, int lv, Map<Integer, Integer> dayRecharge) {
		Active771RechargeTemplateImpl rechargeTmep = this.rechargeMap.get(id);
		int count = dayRecharge.getOrDefault(rechargeTmep.getDay(), 0);
		if(null!=rechargeTmep && rechargeTmep.fixLvStageRec(stage, lv, count, index)) {
			return rechargeTmep.getReward(index);
		}
		return null;
	}

	/**
	 * 获取累充的，所有可领取的奖励id列表
	 * @param stage 期数
	 * @param receive 已经领取的
	 * @param dayRecharge 充值记录
	 * @param lv	等级
	 * @return
	 */
	public Map<Integer, List<Integer>> getRechargeIdAll(int stage, Map<Integer, List<Integer>> receive, Map<Integer, Integer> dayRecharge, int lv) {
		Collection<Active771RechargeTemplateImpl> tempLIst = this.rechargeTable.row(stage).values();
		Map<Integer, List<Integer>> result = Maps.newHashMap();
		tempLIst.stream().filter(e->(e.fixLv(lv))).forEach(e->{
			int rechargeTemp = dayRecharge.getOrDefault(e.getDay(), 0);
			List<Integer> receiveTemp = receive.get(e.getId());
			result.put(e.getId(), e.getFixIds(rechargeTemp, receiveTemp));
		});
		return result;
	}

	/**
	 * 获取未领取奖励列表
	 * @param rechargeIds
	 * @return
	 */
	public List<Items> getRechargeItems(Map<Integer, List<Integer>> rechargeIds) {
		List<Items> tempResult = Lists.newArrayList();
		rechargeIds.forEach((key, value)->{
			tempResult.addAll(rechargeMap.get(key).getRewardAll(value));
		});
		return tempResult;
	}

	/**
	 * 获取单个的，节日礼包奖励
	 * @param stage
	 * @param id
	 * @return
	 */
	public Active771PassTemplateImpl getPassTemplate(int stage, int id) {
		Active771PassTemplateImpl passTemplate = passMap.values().stream()
				.filter(e -> e.getStage() == stage && e.getId() == id).findFirst().orElse(null);
		return passTemplate;
	}


	public List<Active771PassTemplateImpl> receiveAllPassTemp(int stage, int level){
		List<Active771PassTemplateImpl> collect = passMap.values().stream()
				.filter(e ->e.getStage() == stage && level >= e.getPass_level()).collect(Collectors.toList());
		return collect;
	}

	/**
	 * 根据亲密度，后去等级信息
	 * @param giveNum
	 * @return
	 */
	public int getPassLv(int giveNum) {
		List<Integer> ids = passLevelMap.values().stream().filter(e->giveNum>=e.getFlowers_total()).map(e->e.getId()).collect(Collectors.toList());
		return Collections.max(ids);
	}
}
