package com.hm.config.excel;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.*;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.libcore.util.string.StringUtil;
import com.hm.config.excel.temlate.Active55GiftTemplate;
import com.hm.config.excel.temlate.Active55PassGiftTemplate;
import com.hm.config.excel.temlate.Active55PassLevelTemplate;
import com.hm.config.excel.temlate.ActiveDragonboatStageTemplate;
import com.hm.config.excel.templaextra.*;
import com.hm.model.item.Items;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 
 * @ClassName:  Active55Config   
 * @Description:
 * @author: zxj
 * @date:   2020年5月9日 下午4:42:29      
 *
 */
@Config
public class Active55Config extends ExcleConfig {
	//消费返利配置
	private Map<Integer, Active55ConsumeGiftImpl> giftTemplate = Maps.newConcurrentMap();
	//配方
	private Map<Integer, Active55FormulaImpl> formulaTemplate = Maps.newConcurrentMap();
	//单笔充值
	private Map<Integer, Active55RechargeOnceImpl> rechargeOnceTemplate = Maps.newConcurrentMap();
	//单笔充值
	private ArrayListMultimap<Integer, Active55RechargeOnceImpl> rechargeGoldTemplateMap = ArrayListMultimap.create();
	//金砖购买道具配置
	private Map<Integer, Active55ShopImpl> shopTemplate = Maps.newConcurrentMap();
	//根据期数，邮件配置
	private Table<Integer, Integer, Integer> mailStageTemplate = HashBasedTable.create();

	//根据期数，邮件配置
	private Map<Integer, List<Integer>> mailStageMapTemplate = Maps.newConcurrentMap();

	// 计费点
	private ArrayListMultimap<Integer, Active55GiftTemplateImpl> giftTemplateMap = ArrayListMultimap.create();
	private Table<Integer, Integer,Active55GiftTemplate> giftIds = HashBasedTable.create();

	private Map<Integer, Active55PassTemplateImpl> passMap = Maps.newConcurrentMap();
	private int passLevelMax = 0;
	private Map<Integer, Active55PassLevelTemplate> passLevelMap = Maps.newConcurrentMap();
	private Map<Integer, Active55PassGiftTemplate> passgiftMap = Maps.newConcurrentMap();

	private Map<Integer, Active55GuessrewardTemplateImpl> guessRewardTemplateMap = Maps.newConcurrentMap();
	private Map<Integer, Active55GuessTemplateImpl> guessTemplateMap = Maps.newConcurrentMap();

	@Override
	public void loadConfig() {
//		loadFormulaTemplate();
//		loadGiftTemplate();
//		loadBuyGiftTemplate();
//		loadRechargeOnce();
//		loadMailStageTemplate();
//		loadActive55GiftTemplate();
//		loadPass();
//		loadPassLevel();
//		loadPassGift();
//		loadGuessRewardTemplate();
//		loadGuessTemplate();
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(Active55ConsumeGiftImpl.class,
				Active55FormulaImpl.class,
				Active55RechargeOnceImpl.class,
				Active55ShopImpl.class,
				ActiveDragonboatStageTemplate.class,
				Active55GiftTemplateImpl.class,
				Active55PassTemplateImpl.class,
				Active55PassLevelTemplate.class,
				Active55PassGiftTemplate.class,
				Active55GuessrewardTemplateImpl.class,
				Active55GuessTemplateImpl.class
		);
	}
	
	private void loadFormulaTemplate() {
		Map<Integer, Active55FormulaImpl> tempFormulaTemplate = Maps.newConcurrentMap();
		for(Active55FormulaImpl template :JSONUtil.fromJson(getJson(Active55FormulaImpl.class),
				new TypeReference<ArrayList<Active55FormulaImpl>>() {
				})) {
			template.init();
			tempFormulaTemplate.put(template.getId(), template);
		}
		this.formulaTemplate  = ImmutableMap.copyOf(tempFormulaTemplate);
	}
	//获取资源礼包信息
	private void loadGiftTemplate() {
		Map<Integer, Active55ConsumeGiftImpl> tempGiftTemplate = Maps.newConcurrentMap();
		for(Active55ConsumeGiftImpl template :JSONUtil.fromJson(getJson(Active55ConsumeGiftImpl.class),
				new TypeReference<ArrayList<Active55ConsumeGiftImpl>>() {
				})) {
			template.init();
			tempGiftTemplate.put(template.getId(), template);
		}
		this.giftTemplate  = ImmutableMap.copyOf(tempGiftTemplate);
	}
	
	private void loadBuyGiftTemplate() {
		Map<Integer, Active55ShopImpl> tempBuyGiftTemplate = Maps.newConcurrentMap();
		for(Active55ShopImpl template :JSONUtil.fromJson(getJson(Active55ShopImpl.class),
				new TypeReference<ArrayList<Active55ShopImpl>>() {
				})) {
			template.init();
			tempBuyGiftTemplate.put(template.getId(), template);
		}
		this.shopTemplate  = ImmutableMap.copyOf(tempBuyGiftTemplate);
	}
	
	//获取资源礼包信息
	private void loadRechargeOnce() {
		Map<Integer, Active55RechargeOnceImpl> tempTemplate = Maps.newConcurrentMap();
		ArrayListMultimap<Integer, Active55RechargeOnceImpl> tempGoldTemplate = ArrayListMultimap.create();
		for(Active55RechargeOnceImpl template :JSONUtil.fromJson(getJson(Active55RechargeOnceImpl.class),
				new TypeReference<ArrayList<Active55RechargeOnceImpl>>() {
				})) {
			template.init();
			tempTemplate.put(template.getId(), template);
			tempGoldTemplate.put(template.getRecharge_gold(), template);
		}
		this.rechargeOnceTemplate  = ImmutableMap.copyOf(tempTemplate);
		this.rechargeGoldTemplateMap  = tempGoldTemplate;
	}
	
	//获取资源礼包信息
	private void loadMailStageTemplate() {
		Table<Integer, Integer, Integer> tempMailStageTemplate = HashBasedTable.create();
		Map<Integer, List<Integer>> tempMailStageMapTemplate = Maps.newConcurrentMap();
		for(ActiveDragonboatStageTemplate template :JSONUtil.fromJson(getJson(ActiveDragonboatStageTemplate.class),
				new TypeReference<ArrayList<ActiveDragonboatStageTemplate>>() {
				})) {
			List<Integer> tempRankType = StringUtil.splitStr2IntegerList(template.getRank_type(), ",");
			List<Integer> tempMailId = StringUtil.splitStr2IntegerList(template.getMail_id(), ",");
			if(tempRankType.size()==0 || tempRankType.size()!= tempMailId.size()) {
				System.out.println("=========================获取端午节活动，邮件期数配置异常=========================");
			}
			tempMailStageMapTemplate.put(template.getId(), tempRankType);
			for(int i=0; i<tempRankType.size(); i++) {
				tempMailStageTemplate.put(template.getId(), tempRankType.get(i), tempMailId.get(i));
			}
		}
		this.mailStageMapTemplate = ImmutableMap.copyOf(tempMailStageMapTemplate);
		this.mailStageTemplate  = ImmutableTable.copyOf(tempMailStageTemplate);
	}
	private void loadActive55GiftTemplate() {
		ArrayListMultimap<Integer,Active55GiftTemplateImpl> tempMap = ArrayListMultimap.create();
		List<Active55GiftTemplateImpl> list = JSONUtil.fromJson(getJson(Active55GiftTemplateImpl.class), new TypeReference<List<Active55GiftTemplateImpl>>() {});
		list.forEach(e ->{
			tempMap.put(e.getStage(), e);
		});
		giftTemplateMap = tempMap;

		Table<Integer,Integer,Active55GiftTemplate> giftIdsTemp = HashBasedTable.create();
		list.forEach(e->{
			giftIdsTemp.put(e.getStage(),e.getRecharge_gift_id(), e);
		});
		this.giftIds = ImmutableTable.copyOf(giftIdsTemp);
	}

	public Active55FormulaImpl getFormula(int id) {
		return formulaTemplate.get(id);
	}
	public Active55ConsumeGiftImpl getGift(int id) {
		return this.giftTemplate.get(id);
	}
	public Active55ShopImpl getBuyGift(int id) {
		return this.shopTemplate.get(id);
	}
	public Active55RechargeOnceImpl getRechargeOnce(int id) {
		return rechargeOnceTemplate.get(id);
	}
	
	public Active55RechargeOnceImpl getRechargeOnce(int gold,int playerLv, int version) {
		Collection<Active55RechargeOnceImpl> templates = rechargeGoldTemplateMap.get(gold);
		for (Active55RechargeOnceImpl template : templates) {
			if(template.isFit(playerLv) && template.getStage()==version) {
				return template;
			}
		}
		return null;
	}
	
	
	public Set<Integer> getRankTypeId(int stage) {
		return mailStageTemplate.row(stage).keySet();
	}

	public int getRankTypeId(int stage, int index) {
		if(mailStageMapTemplate.get(stage).size()>index) {
			return mailStageMapTemplate.get(stage).get(index);
		}
		return -1;
	}

	public Active55GiftTemplateImpl getActive55GiftTemplate(int stage, int playerLv, int giftId){
		List<Active55GiftTemplateImpl> active55GiftTemplates = giftTemplateMap.get(stage);
		if (CollUtil.isNotEmpty(active55GiftTemplates)){
			return active55GiftTemplates.stream().filter(e -> e.isFit(playerLv)).filter(e -> e.getRecharge_gift_id() == giftId).findFirst().orElse(null);
		}
		return null;
	}

	private void loadPass() {
		List<Active55PassTemplateImpl> list = JSONUtil.fromJson(getJson(Active55PassTemplateImpl.class), new TypeReference<List<Active55PassTemplateImpl>>(){});
		Map<Integer,Active55PassTemplateImpl> tempMap = Maps.newConcurrentMap();
		for(Active55PassTemplateImpl temp: list) {
			temp.init();
			tempMap.put(temp.getId(), temp);
		}
		this.passMap = ImmutableMap.copyOf(tempMap);
	}
	private void loadPassLevel() {
		List<Active55PassLevelTemplate> list = JSONUtil.fromJson(getJson(Active55PassLevelTemplate.class), new TypeReference<List<Active55PassLevelTemplate>>(){});
		Map<Integer,Active55PassLevelTemplate> giftMap = list.stream().collect(Collectors.toMap(Active55PassLevelTemplate::getId, e->e));
		this.passLevelMap = ImmutableMap.copyOf(giftMap);
		this.passLevelMax = Collections.max(giftMap.keySet());
	}
	private void loadPassGift() {
		List<Active55PassGiftTemplate> list = JSONUtil.fromJson(getJson(Active55PassGiftTemplate.class), new TypeReference<List<Active55PassGiftTemplate>>(){});
		Map<Integer,Active55PassGiftTemplate> giftMap = list.stream().collect(Collectors.toMap(Active55PassGiftTemplate::getRecharge_gift_id, e->e));
		this.passgiftMap = ImmutableMap.copyOf(giftMap);
	}

	private void loadGuessRewardTemplate() {
		Map<Integer, Active55GuessrewardTemplateImpl> tempBuyGiftTemplate = Maps.newConcurrentMap();
		for(Active55GuessrewardTemplateImpl template :JSONUtil.fromJson(getJson(Active55GuessrewardTemplateImpl.class),
				new TypeReference<ArrayList<Active55GuessrewardTemplateImpl>>() {
				})) {
			template.init();
			tempBuyGiftTemplate.put(template.getId(), template);
		}
		this.guessRewardTemplateMap  = ImmutableMap.copyOf(tempBuyGiftTemplate);
	}

	private void loadGuessTemplate() {
		Map<Integer, Active55GuessTemplateImpl> tempBuyGiftTemplate = Maps.newConcurrentMap();
		for(Active55GuessTemplateImpl template :JSONUtil.fromJson(getJson(Active55GuessTemplateImpl.class),
				new TypeReference<ArrayList<Active55GuessTemplateImpl>>() {
				})) {
			template.init();
			tempBuyGiftTemplate.put(template.getId(), template);
		}
		this.guessTemplateMap  = ImmutableMap.copyOf(tempBuyGiftTemplate);
	}

	/**
	 * 获取单个的，节日礼包奖励
	 * @param stage
	 * @param id
	 * @return
	 */
	public Active55PassTemplateImpl getPassTemplate(int stage, int id) {
		Active55PassTemplateImpl passTemplate = passMap.values().stream()
				.filter(e -> e.getStage() == stage && e.getId() == id).findFirst().orElse(null);
		return passTemplate;
	}
	/**
	 * 根据亲密度，后去等级信息
	 * @param giveNum
	 * @return
	 */
	public int getPassLv(int giveNum) {
		List<Integer> ids = passLevelMap.values().stream().filter(e->giveNum>=e.getZongzi_total()).map(e->e.getId()).collect(Collectors.toList());
		return Collections.max(ids);
	}
	public List<Active55PassTemplateImpl> receiveAllPassTemp(int stage, int level, int playerLv){
		List<Active55PassTemplateImpl> collect = passMap.values().stream()
				.filter(e ->e.isFit(stage, playerLv) && level >= e.getPass_level()).collect(Collectors.toList());
		return collect;
	}

	public boolean contains(int version, int giftid){
		return this.giftIds.contains(version, giftid);
	}

	public Active55PassGiftTemplate getPassGift(int giftid){
		return this.passgiftMap.getOrDefault(giftid, null);
	}

	public List<Items> getGuessReward(int stage, int playerLv, List<Integer> successIds) {
		List<Items> list = guessTemplateMap.values().stream()
				.filter(e -> e.isFit(playerLv, stage))
				.filter(e -> CollUtil.contains(successIds, e.getId()))
				.flatMap(e -> e.getRewards().stream())
				.collect(Collectors.toList());
		Active55GuessrewardTemplateImpl active55GuessrewardTemplate = guessRewardTemplateMap.values().stream().filter(e -> e.isFitRule(playerLv, successIds.size(), stage)).findFirst().orElse(null);
		if(active55GuessrewardTemplate != null){
			list.addAll(active55GuessrewardTemplate.getRewards());
		}
		return list;
	}
}






