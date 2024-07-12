package com.hm.config.excel;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.*;
import com.hm.config.excel.temlate.Active41PassGiftTemplate;
import com.hm.config.excel.temlate.Active41PassLevelTemplate;
import com.hm.config.excel.temlate.ActiveRichGiftTemplate;
import com.hm.config.excel.temlate.ActiveRichmanMapTemplate;
import com.hm.config.excel.templaextra.*;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import lombok.Getter;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
@Config
public class FestivalActivityConfig extends ExcleConfig{
	//大富翁
	private Map<Integer,ActiveRichManTemplate> richManMap = Maps.newConcurrentMap();
	//大富翁配置
	private Map<Integer,ActiveRichManConstantTemplate> richManConstantMap = Maps.newConcurrentMap();
	//充值奖励表
	private Map<Integer, ActiveRichRechargeTemplateImpl> rechargeMap = Maps.newConcurrentMap();
	// 成就奖励
	private Map<Integer, ActiveRichmanProgressTemplateImpl> progressMap = Maps.newConcurrentMap();
	/**
	 * ActiveRichGiftTemplate::getRecharge_gift_id
	 */
	private Set<Integer> richManRechargeId = Sets.newHashSet();
	// 运输车 奖励最大的 列数
	@Getter
	private int maxTransportNum;
	// 商店相关
	private Map<Integer, Active41GiftTemplateImpl> giftTemplateMap = Maps.newHashMap();
	private Table<Integer, Integer, Active41GiftTemplateImpl> giftIds = HashBasedTable.create();
	// 战令相关
	private Map<Integer, Active41PassTemplateImpl> rewardTemplateMap = Maps.newHashMap();
	private Map<Integer, Active41PassGiftTemplate> passTemplateMap = Maps.newHashMap();
	private Map<Integer, Active41PassLevelTemplate> passLevelTemplateMap = Maps.newHashMap();
	// 宝箱相关
	private Map<Integer, Active41TreasuryTemplateImpl> treasuryTemplateMap = Maps.newHashMap();


	@Override
	public void loadConfig() {
//		loadRichManConfig();
//		loadRichManConstantConfig();
//		loadRechrageConfig();
//		loadProgressConfig();
//		loadRichManRechargeIdConfig();
//
//		loadRewardTemplateMap();
//		loadGiftTemplateMap();
//		loadPassTemplateMap();
//		loadPassLevelTemplateMap();
//		loadTreasuryMap();
	}

	private void loadTreasuryMap() {
		List<Active41TreasuryTemplateImpl> list = JSONUtil.fromJson(getJson(Active41TreasuryTemplateImpl.class), new TypeReference<List<Active41TreasuryTemplateImpl>>() {
		});
		list.forEach(e -> {
			e.init();
		});
		Map<Integer, Active41TreasuryTemplateImpl> tempMap = list.stream().collect(Collectors.toMap(Active41TreasuryTemplateImpl::getId, Function.identity()));
		treasuryTemplateMap = ImmutableMap.copyOf(tempMap);
	}

	private void loadPassLevelTemplateMap() {
		List<Active41PassLevelTemplate> list = JSONUtil.fromJson(getJson(Active41PassLevelTemplate.class), new TypeReference<List<Active41PassLevelTemplate>>() {
		});
		Map<Integer, Active41PassLevelTemplate> tempMap = list.stream().collect(Collectors.toMap(Active41PassLevelTemplate::getId, Function.identity()));
		passLevelTemplateMap = ImmutableMap.copyOf(tempMap);
	}

	private void loadPassTemplateMap() {
		List<Active41PassGiftTemplate> list = JSONUtil.fromJson(getJson(Active41PassGiftTemplate.class), new TypeReference<List<Active41PassGiftTemplate>>() {
		});
		Map<Integer, Active41PassGiftTemplate> tempMap = list.stream().collect(Collectors.toMap(Active41PassGiftTemplate::getId, Function.identity()));
		passTemplateMap = ImmutableMap.copyOf(tempMap);
	}

	private void loadGiftTemplateMap() {
		List<Active41GiftTemplateImpl> list = JSONUtil.fromJson(getJson(Active41GiftTemplateImpl.class), new TypeReference<List<Active41GiftTemplateImpl>>() {
		});
		Map<Integer, Active41GiftTemplateImpl> tempMap = list.stream().collect(Collectors.toMap(Active41GiftTemplateImpl::getId, Function.identity()));
		giftTemplateMap = ImmutableMap.copyOf(tempMap);
		Table<Integer, Integer, Active41GiftTemplateImpl> giftIdsTemp = HashBasedTable.create();
		list.forEach(e -> {
			giftIdsTemp.put(e.getStage(), e.getRecharge_id(), e);
		});
		this.giftIds = ImmutableTable.copyOf(giftIdsTemp);
	}

	private void loadRewardTemplateMap() {
		List<Active41PassTemplateImpl> list = JSONUtil.fromJson(getJson(Active41PassTemplateImpl.class), new TypeReference<List<Active41PassTemplateImpl>>() {
		});
		list.forEach(e -> {
			e.init();
		});
		Map<Integer, Active41PassTemplateImpl> tempMap = list.stream().collect(Collectors.toMap(Active41PassTemplateImpl::getId, Function.identity()));
		rewardTemplateMap = ImmutableMap.copyOf(tempMap);
	}
	
	private void loadRichManConfig(){
		Map<Integer, ActiveRichManTemplate> richManMap = Maps.newConcurrentMap();
		for(ActiveRichManTemplate template :JSONUtil.fromJson(getJson(ActiveRichManTemplate.class),
				new TypeReference<ArrayList<ActiveRichManTemplate>>() {
				})) {
			template.init();
			richManMap.put(template.getId(), template);
		}
		this.richManMap = ImmutableMap.copyOf(richManMap);
		this.maxTransportNum = richManMap.values().stream()
				.filter(e -> CollUtil.isNotEmpty(e.getTransportRewards()))
				.mapToInt(e -> e.getTransportRewards().size())
				.min().orElse(0);
	}
	
	private void loadRichManConstantConfig(){
		Map<Integer, ActiveRichManConstantTemplate> richManConstantMap = Maps.newConcurrentMap();
		for(ActiveRichManConstantTemplate template :JSONUtil.fromJson(getJson(ActiveRichManConstantTemplate.class),
				new TypeReference<ArrayList<ActiveRichManConstantTemplate>>() {
				})) {
			template.init();
			richManConstantMap.put(template.getStage(), template);
		}
		this.richManConstantMap = ImmutableMap.copyOf(richManConstantMap);
	}
	
	private void loadRechrageConfig(){
		Map<Integer, ActiveRichRechargeTemplateImpl> tMap = Maps.newConcurrentMap();
		for(ActiveRichRechargeTemplateImpl template :JSONUtil.fromJson(getJson(ActiveRichRechargeTemplateImpl.class),
				new TypeReference<ArrayList<ActiveRichRechargeTemplateImpl>>() {
				})) {
			template.init();
			tMap.put(template.getId(), template);
		}
		this.rechargeMap = ImmutableMap.copyOf(tMap);
	}

	private void loadProgressConfig(){
		Map<Integer, ActiveRichmanProgressTemplateImpl> tMap = Maps.newConcurrentMap();
		for(ActiveRichmanProgressTemplateImpl template :JSONUtil.fromJson(getJson(ActiveRichmanProgressTemplateImpl.class),
				new TypeReference<ArrayList<ActiveRichmanProgressTemplateImpl>>() {
				})) {
			template.init();
			tMap.put(template.getId(), template);
		}
		this.progressMap = ImmutableMap.copyOf(tMap);
	}

	private void loadRichManRechargeIdConfig(){
		ArrayList<ActiveRichGiftTemplate> list = JSONUtil.fromJson(getJson(ActiveRichGiftTemplate.class), new TypeReference<ArrayList<ActiveRichGiftTemplate>>() {});
		Set<Integer> s = list.stream().map(ActiveRichGiftTemplate::getRecharge_gift_id).collect(Collectors.toSet());
		richManRechargeId = ImmutableSet.copyOf(s);
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(ActiveRichmanMapTemplate.class,ActiveRichManConstantTemplate.class,ActiveRichManRewardTemplate.class,
				ActiveRichRechargeTemplateImpl.class, ActiveRichmanProgressTemplateImpl.class, ActiveRichGiftTemplate.class,
				Active41GiftTemplateImpl.class, Active41PassTemplateImpl.class, Active41PassGiftTemplate.class, Active41PassLevelTemplate.class, Active41TreasuryTemplateImpl.class);
	}
	
	public ActiveRichManTemplate getRichMan(int id){
		return richManMap.get(id);
	}
	
	public ActiveRichManTemplate getRichMan(int version, int serverLv, int pos){
		return richManMap.values().stream().filter(t ->t.getStage()==version && t.inLv(serverLv) &&t.getPos()==pos).findFirst().orElse(null);
	}
	public ActiveRichManConstantTemplate getRichManConstant(int version){
		return richManConstantMap.get(version);
	}

	//获取本轮活动的终点
	public int getRichManPointEnd(int version, int serverLv){
		return richManMap.values().stream().filter(t -> t.getStage()==version && t.inLv(serverLv)).mapToInt(ActiveRichManTemplate::getPos).max().getAsInt();
	}

	public ActiveRichRechargeTemplateImpl getRechargeCfg(int id) {
		return rechargeMap.getOrDefault(id, null);
	}

	public ActiveRichmanProgressTemplateImpl getProgressCfg(int id) {
		return progressMap.getOrDefault(id, null);
	}

	/**
	 * 该recharge id 是否是richman 活动配置的id
	 *
	 * @param rechargeGiftId
	 * @return
	 */
	public boolean inRichManActivity(int rechargeGiftId) {
		return richManRechargeId.contains(rechargeGiftId);
	}

    public List<Active41PassTemplateImpl> receiveAll(int version, int level, int playerLv) {
		return rewardTemplateMap.values().stream()
                .filter(e -> e.isFit(version, playerLv) && level >= e.getPass_level()).collect(Collectors.toList());
    }

    public Active41PassTemplateImpl getActive41PassTemplate(int id) {
        return rewardTemplateMap.get(id);
	}

	public Active41GiftTemplateImpl getActive41GiftTemplate(int rechargeId) {
		return giftTemplateMap.getOrDefault(rechargeId, null);
	}

	public Active41GiftTemplateImpl contains(int version, int giftid) {
		return this.giftIds.get(version, giftid);
	}

	public Active41PassGiftTemplate getActive41PassGiftTemplate(int rechargeId) {
		return passTemplateMap.values().stream().filter(e -> e.getRecharge_gift_id() == rechargeId)
				.findFirst()
				.orElse(null);
	}

	public int getLevel(int throwTimes) {
		Active41PassLevelTemplate active41PassLevelTemplate = passLevelTemplateMap.values().stream()
				.sorted(Comparator.comparingInt(Active41PassLevelTemplate::getDice_total).reversed())
				.filter(e -> throwTimes >= e.getDice_total()).findFirst().orElse(null);
		return active41PassLevelTemplate == null ? 0 : active41PassLevelTemplate.getId();
	}


	public Active41TreasuryTemplateImpl getBigTreasuryTemplate(int version, int days, int playerLv) {
		return treasuryTemplateMap.values().stream()
				.filter(e -> e.getProgress() == days && e.isFit(playerLv))
				.filter(e -> e.getStage() == version)
				.filter(e -> CollUtil.isNotEmpty(e.getItemsBigList()))
				.findFirst()
				.orElse(null);
	}
}
