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
import com.hm.config.excel.temlate.*;
import com.hm.config.excel.templaextra.*;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.model.activity.threedays.PlayerThreeDayValue;
import com.hm.model.item.Items;
import com.hm.util.RandomUtils;
import com.hm.util.WeightMeta;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * @Description: 活动配置
 * @author siyunlong
 * @date 2018年1月9日 下午4:31:09
 * @version V1.0
 */
@Config ( "ActivityConfig" )
public class ActivityConfig extends ExcleConfig {
	// 活动模板
	private Map<Integer, ActivityMainTemplate> activityMainMap = Maps.newConcurrentMap();
	// 7日登录福利活动
	private Map<Integer, ActivitySevenLoginWelfareTemplate> loginWelfareMap = Maps.newConcurrentMap();
	// 后勤补给列表
	private List<ActivityArmyFeteTemplate> armyFeteList = Lists.newArrayList();
	private List<Integer> armyFeteStartHours = Lists.newArrayList();
	// 签到奖励
	private Map<Integer, ActivitySignTemplate> signMap = Maps.newConcurrentMap();
	// 累计签到奖励
	private Map<Integer, ActivitySignTotalTemplate> signTotalMap = Maps.newConcurrentMap();
	private Map<Integer,ActivitySignMonthRewardTemplate> signMonthMap = Maps.newConcurrentMap();
	private int maxSignDay;  //最大签到天数
	//在线转盘
	private Map<Integer, Integer> circleMap = Maps.newConcurrentMap(); 
	private Map<Integer, ActivityCircleRewardTemplate> circleRewardMap = Maps.newConcurrentMap();
	private Map<Integer, ActiveCircleRewardJuzuoTemplateImpl> circleJzRewardMap = Maps.newConcurrentMap();
	//===========================SSTank======================================
	// 期数,position:config obj
    private Table<Integer, Integer, ActiveSscarRewardTemplateImpl> sscarRewardTable = HashBasedTable.create();
	/**
	 * ActiveSscarRewardLibraryTemplateImpl::getReward_type : ActiveSscarRewardLibraryTemplateImpl
	 */
    private Multimap<Integer, ActiveSscarRewardLibraryTemplateImpl> rewardLibraryMap = ArrayListMultimap.create();
	// 期数:weight
	// 普通奖励
	private Map<Integer, WeightMeta<Integer>> normalWM = Maps.newHashMap();
	// 幸运奖励
	private Map<Integer, WeightMeta<Integer>> luckyWM = Maps.newHashMap();
    // id:object
    private Map<Integer, ActiveSscarCountTemplateImpl> sscarCountTemplateMap = Maps.newConcurrentMap();
	// 期数,position:config obj
	private Table<Integer, Integer, ActiveSscarShopTemplate> sscarShopTable = HashBasedTable.create();

	// 冲榜活动
	// rank type, rank number, object
	private Table<Integer, Integer, ActiveNewplayerRankTemplateImpl> newPlayerRankTable = HashBasedTable.create();
	//===========================SSTank======================================

	//累计充值
    private Map<Integer, ActiveRechargeTemplateExt> rechargeMap = Maps.newConcurrentMap(); 
    //充值回馈
    private Map<Integer, ActiveRechargeFeedbackTemplateExt> rechargeFeedbackMap = Maps.newConcurrentMap(); 
	//三日活动-新7日活动
	private Map<Integer, ActivityThreeDayTemplate> threeDayMap = Maps.newConcurrentMap(); 
	private Map<Integer, ActiveThreeDayPointTemplateImpl> threeDayPointMap = Maps.newConcurrentMap();
	//十连活动
	private Map<Integer, ActivityHighResearchTemplate> highResearchMap = Maps.newConcurrentMap();
	// 限时消费
	private Map<Integer, ActiveConsumeTemplateImpl> consumeTemplateMap = Maps.newConcurrentMap();
	
	//7日充值活动
	private Map<Integer, ActiveRechargeSevendayImpl> sevenDayMap = Maps.newConcurrentMap(); 
	//坦克打靶
	private Map<Integer,ActivityShootingTemplate> shootingMap = Maps.newConcurrentMap(); 
	
	//坦克图纸绘制活动
	private Map<Integer,ActiveDrawPaperTemplateImpl> tankDraw = Maps.newConcurrentMap();

	// 惊奇充值（渠道累充）
	private Map<Integer, ActiveRechargeTurningendTemplateImpl> turnMap = Maps.newConcurrentMap();
	//active type (6)
	private Map<Integer, ActiveOnlineRewardTemplateImpl> onlineMap = Maps.newConcurrentMap();
	//active 2059
	private Map<Integer, ActiveLoginTurningendTemplateImpl> loinTrunMap = Maps.newConcurrentMap();
	/**
	 * 专属签到活动 最多可领取天数
	 */
	private int loginTruningEndMaxDay;
	private Map<Integer, DouyinSevenLoginRewardTemplateImpl> douyinMap = Maps.newConcurrentMap();

	@Override
	public void loadConfig() {
		loadActivityMain();
//		loadActivitySupply();
		loadActivitySevenLogin();
//		loadActivitySign();
//		loadActivitySignMoth();
//		loadActivitySignTotal();
//		loadActivityCircle();
//		loadActivityCircleReward();
//		loadActiveRecharge();
//		loadActiveRechargeFeedback();
//		loadActiveSscarReward();
		loadActivityThreeDay();
//		loadActivityHighResearch();
//		loadActiveRechargeSevenDay();
//		loadActivityConsume();
//		loadActivityShooting();
//		loadTankDraw();
//		loadTurn();
//		loadOnline();
//		loadLoginTurn();
//		loadDouYin();
	}


	//===========================SSTank======================================
	/**
	 * SS坦克活动
	 */
	private void loadActiveSscarReward() {
		List<ActiveSscarRewardTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveSscarRewardTemplateImpl.class), new TypeReference<List<ActiveSscarRewardTemplateImpl>>() {
		});
		// 期数,position:config obj
		Table<Integer, Integer, ActiveSscarRewardTemplateImpl> tempTable = HashBasedTable.create();
		list.forEach(template -> {
			tempTable.put(template.getStage(), template.getPos(), template);
		});
		sscarRewardTable = ImmutableTable.copyOf(tempTable);

		// 构造随机WeightMeta obj
		Map<Integer, WeightMeta<Integer>> normal = Maps.newHashMap();
		Map<Integer, WeightMeta<Integer>> lucky = Maps.newHashMap();
		tempTable.rowMap().forEach((stage, value) -> {
			Map<Integer, Integer> rate = Maps.newHashMap();
			Map<Integer, Integer> luck = Maps.newHashMap();

			value.forEach((position, rewardTemplate) -> {
				if (rewardTemplate.getRate() > 0) {
					rate.put(position, rewardTemplate.getRate());
				}
				if (rewardTemplate.getLucky_rate() > 0) {
					luck.put(position, rewardTemplate.getLucky_rate());
				}
			});
			normal.put(stage, RandomUtils.buildWeightMeta(rate));
			lucky.put(stage, RandomUtils.buildWeightMeta(luck));
		});
		normalWM = ImmutableMap.copyOf(normal);
		luckyWM = ImmutableMap.copyOf(lucky);

		List<ActiveSscarCountTemplateImpl> list1 = JSONUtil.fromJson(getJson(ActiveSscarCountTemplateImpl.class), new TypeReference<List<ActiveSscarCountTemplateImpl>>() {
		});
		Map<Integer, ActiveSscarCountTemplateImpl> tMap = Maps.newConcurrentMap();
		list1.forEach(template -> {
			template.init();
			tMap.put(template.getId(), template);
		});
		sscarCountTemplateMap = ImmutableMap.copyOf(tMap);

		Table<Integer, Integer, ActiveSscarShopTemplate> t1 = HashBasedTable.create();
		List<ActiveSscarShopTemplate> list2 = JSONUtil.fromJson(getJson(ActiveSscarShopTemplate.class), new TypeReference<List<ActiveSscarShopTemplate>>() {
		});
		list2.forEach(t -> {
			t1.put(t.getStage(), t.getPos(), t);
		});
		sscarShopTable = ImmutableTable.copyOf(t1);

		List<ActiveNewplayerRankTemplateImpl> list3 = JSONUtil.fromJson(getJson(ActiveNewplayerRankTemplateImpl.class), new TypeReference<List<ActiveNewplayerRankTemplateImpl>>() {
		});

		Table<Integer, Integer, ActiveNewplayerRankTemplateImpl> t2 = HashBasedTable.create();
		list3.forEach(t -> {
			t.init();
			t2.put(t.getRank_type(), t.getRank_num(), t);
		});
		newPlayerRankTable = ImmutableTable.copyOf(t2);

		Multimap<Integer, ActiveSscarRewardLibraryTemplateImpl> tMap1 = ArrayListMultimap.create();
		List<ActiveSscarRewardLibraryTemplateImpl> l2 = JSONUtil.fromJson(getJson(ActiveSscarRewardLibraryTemplateImpl.class), new TypeReference<List<ActiveSscarRewardLibraryTemplateImpl>>() {
		});
		l2.forEach(t->{
			t.init();
			tMap1.put(t.getReward_type(), t);
		});

		rewardLibraryMap = ImmutableListMultimap.copyOf(tMap1);
	}

	/**
	 * 获取sstank 活动随机到的
	 *
	 * @param stage 期数
	 * @param isNormal 是否常规抽奖
	 * @param playerLv
	 * @return
	 */
	public ActiveSscarRewardLibraryTemplateImpl getSSTankRandReward(int stage, boolean isNormal, int playerLv) {
	    if (!sscarRewardTable.containsRow(stage)) {
	    	return null;
		}

		WeightMeta<Integer> meta = isNormal ? normalWM.get(stage) : luckyWM.get(stage);

		Integer randomPos = meta.random();
		if (randomPos == null) {
			return null;
		}

		ActiveSscarRewardTemplateImpl template = sscarRewardTable.get(stage, randomPos);
		if (template == null) {
			return null;
		}
		return rewardLibraryMap.get(template.getReward_id()).stream().
				filter(e -> e.getPlayer_lv_down() <= playerLv && playerLv <= e.getPlayer_lv_up()).
				findFirst().orElse(null);
	}
	
	/**
	 * 根据期数列表获取展示坦克ids
	 * @param markList
	 * @return
	 */
	public Set<Integer> getSsTankForActivity(List<Integer> markList,int type) {
		return markList.stream().flatMap(stage -> 
				sscarRewardTable.row(stage).values().stream().map(e -> e.getActivityTankId(type)).filter(e -> e > 0))
				.collect(Collectors.toSet());
	}
	
	/**
	 * 获取 累计抽奖次数奖励配置
	 *
	 * @param id
	 * @return
	 */
	public ActiveSscarCountTemplateImpl getSSTankCountCfg(int id) {
		return sscarCountTemplateMap.getOrDefault(id, null);
	}

	/**
	 * 获取宝库配置
	 *
	 * @param stage    期数
	 * @param position 位置
	 * @param playerLv
	 * @return
	 */
	public ActiveSscarRewardLibraryTemplateImpl getTreasureCfg(int stage, int position, int playerLv) {
		if (!sscarShopTable.contains(stage, position)) {
			return null;
		}

		ActiveSscarShopTemplate template = sscarShopTable.get(stage, position);
		if (template == null) {
			return null;
		}
		return rewardLibraryMap.get(template.getReward_id()).stream().
				filter(e -> e.getPlayer_lv_down() <= playerLv && playerLv <= e.getPlayer_lv_up()).
				findFirst().orElse(null);
	}

	/**
     * 获取该玩家等级、该期数用户可以获取的vip点数上限
	 *
	 * @param stage
	 * @param playerLv
	 * @return
	 */
	public int getVipPointMax(int stage, int playerLv) {
		List<Integer> rewardIdList = Lists.newArrayList();
		for (ActiveSscarShopTemplate template : sscarShopTable.row(stage).values()) {
			rewardIdList.add(template.getReward_id());
		}

		int sum = 0;
		for (Integer rewardId : rewardIdList) {
			if (!rewardLibraryMap.containsKey(rewardId)) {
				continue;
			}
			ActiveSscarRewardLibraryTemplateImpl template = rewardLibraryMap.get(rewardId).stream().filter(e -> e.getPlayer_lv_down() <= playerLv && playerLv <= e.getPlayer_lv_up()).findFirst().orElse(null);
			if (template == null) {
				continue;
			}
			int cost = template.getBuy_limit() * template.getScore_price();
			sum += cost;
		}

		return sum;
	}

	/**
	 * 冲榜活动所有排行榜类型
	 *
	 * @return
	 */
	public Set<Integer> getNewPlayerAllRankType() {
		return newPlayerRankTable.rowKeySet();
	}

	/**
	 * 获取所有需要发奖励的名词
	 *
	 * @param rankType
	 * @return
	 */
	public Set<Integer> getNewPlayerRankNumSet(int rankType) {
		if (newPlayerRankTable.containsRow(rankType)) {
			return newPlayerRankTable.row(rankType).keySet();
		}
		return Sets.newHashSet();
	}

	public ActiveNewplayerRankTemplateImpl getNewPlayerRankCfg(int rankType, int rankNum) {
		if (!newPlayerRankTable.contains(rankType, rankNum)) {
			return null;
		}
		return newPlayerRankTable.get(rankType, rankNum);
	}
	//===========================SSTank======================================

	// 加载活动列表
	private void loadActivityMain() {
		Map<Integer, ActivityMainTemplate> activityMainMap = Maps.newConcurrentMap();
		for(ActivityMainTemplate template :JSONUtil.fromJson(getJson(ActivityMainTemplate.class),
				new TypeReference<ArrayList<ActivityMainTemplate>>() {
				})) {
			activityMainMap.put(template.getActive_id(), template);
		}
		this.activityMainMap = ImmutableMap.copyOf(activityMainMap);
	}
	// 加载后勤补给
	private void loadActivitySupply() {
		List<ActivityArmyFeteTemplate> armyFeteList = Lists.newArrayList();
		List<Integer> armyFeteStartHours = Lists.newArrayList();
		for(ActivityArmyFeteTemplate template:JSONUtil.fromJson(getJson(ActivityArmyFeteTemplate.class),new TypeReference<ArrayList<ActivityArmyFeteTemplate>>() {})){
			template.init();
			armyFeteList.add(template);
			if(!armyFeteStartHours.contains(template.getOpen_time())){
				armyFeteStartHours.add(template.getOpen_time());
			}
		}
		this.armyFeteList = ImmutableList.copyOf(armyFeteList);
		Collections.reverse(armyFeteStartHours);
		this.armyFeteStartHours = ImmutableList.copyOf(armyFeteStartHours);
	}
	// 加载七日登录
	private void loadActivitySevenLogin() {
		this.loginWelfareMap = json2ImmutableMap(ActivitySevenLoginWelfareTemplate::getId,ActivitySevenLoginWelfareTemplate.class);
	}
	// 签到奖励
	private void loadActivitySign() {
		Map<Integer, ActivitySignTemplate> signMap = Maps.newConcurrentMap();
		for(ActivitySignTemplate template :JSONUtil.fromJson(getJson(ActivitySignTemplate.class),
				new TypeReference<ArrayList<ActivitySignTemplate>>() {
				})) {
			template.init();
			signMap.put(template.getId(), template);
		}
		this.signMap = ImmutableMap.copyOf(signMap);
		this.maxSignDay = signMap.keySet().stream().max(Comparator.comparing(Function.identity())).get();

	}
	// 累计签到奖励
	private void loadActivitySignTotal() {
		Map<Integer, ActivitySignTotalTemplate> signTotalMap = Maps.newConcurrentMap();
		for(ActivitySignTotalTemplate template :JSONUtil.fromJson(getJson(ActivitySignTotalTemplate.class),
				new TypeReference<ArrayList<ActivitySignTotalTemplate>>() {
				})) {
			template.init();
			signTotalMap.put(template.getId(), template);
		}
		this.signTotalMap = ImmutableMap.copyOf(signTotalMap);

	}
	
	// 签到轮数奖励
	private void loadActivitySignMoth() {
		Map<Integer, ActivitySignMonthRewardTemplate> signMonthMap = Maps.newConcurrentMap();
		for(ActivitySignMonthRewardTemplate template :JSONUtil.fromJson(getJson(ActivitySignMonthRewardTemplate.class),
				new TypeReference<ArrayList<ActivitySignMonthRewardTemplate>>() {
				})) {
			template.init();
			signMonthMap.put(template.getId(), template);
		}
		this.signMonthMap = ImmutableMap.copyOf(signMonthMap);

	}
	
	// 在线转盘
	private void loadActivityCircle() {
		Map<Integer, Integer> circleMap = Maps.newConcurrentMap();
		for(ActiveCircleTemplate template :JSONUtil.fromJson(getJson(ActiveCircleTemplate.class),
				new TypeReference<ArrayList<ActiveCircleTemplate>>() {
				})) {
			circleMap.put(template.getCount(), template.getGet_time());
		}
		this.circleMap = ImmutableMap.copyOf(circleMap);
	}
	
	private void loadActivityCircleReward() {
		Map<Integer, ActivityCircleRewardTemplate> circleRewardMap = Maps.newConcurrentMap();
		for(ActivityCircleRewardTemplate template :JSONUtil.fromJson(getJson(ActivityCircleRewardTemplate.class),
				new TypeReference<ArrayList<ActivityCircleRewardTemplate>>() {
				})) {
			template.init();
			circleRewardMap.put(template.getId(), template);
		}
		this.circleRewardMap = ImmutableMap.copyOf(circleRewardMap);

		Map<Integer, ActiveCircleRewardJuzuoTemplateImpl> tMap = Maps.newConcurrentMap();
		for(ActiveCircleRewardJuzuoTemplateImpl template :JSONUtil.fromJson(getJson(ActiveCircleRewardJuzuoTemplateImpl.class),
				new TypeReference<ArrayList<ActiveCircleRewardJuzuoTemplateImpl>>() {
				})) {
			template.init();
			tMap.put(template.getId(), template);
		}
		this.circleJzRewardMap = ImmutableMap.copyOf(tMap);
	}
	//累计充值
	private void loadActiveRecharge() {
		Map<Integer, ActiveRechargeTemplateExt> tempMap = Maps.newConcurrentMap();
		for(ActiveRechargeTemplateExt template :JSONUtil.fromJson(getJson(ActiveRechargeTemplateExt.class),
				new TypeReference<ArrayList<ActiveRechargeTemplateExt>>() {
				})) {
			template.init();
			tempMap.put(template.getId(), template);
		}
		this.rechargeMap = ImmutableMap.copyOf(tempMap);
	}
	//充值回馈
	private void loadActiveRechargeFeedback() {
		Map<Integer, ActiveRechargeFeedbackTemplateExt> tempMap = Maps.newConcurrentMap();
		for(ActiveRechargeFeedbackTemplateExt template :JSONUtil.fromJson(getJson(ActiveRechargeFeedbackTemplateExt.class),
				new TypeReference<ArrayList<ActiveRechargeFeedbackTemplateExt>>() {
				})) {
			template.init();
			tempMap.put(template.getId(), template);
		}
		this.rechargeFeedbackMap = ImmutableMap.copyOf(tempMap);
	}
	
	private void loadActivityThreeDay() {
		this.threeDayMap = json2ImmutableMap(ActivityThreeDayTemplate::getIndex, ActivityThreeDayTemplate.class);
		this.threeDayPointMap = json2ImmutableMap(ActiveThreeDayPointTemplateImpl::getId, ActiveThreeDayPointTemplateImpl.class);
	}
	
	private void loadActivityHighResearch() {
		Map<Integer, ActivityHighResearchTemplate> highResearchMap = Maps.newConcurrentMap();
		for(ActivityHighResearchTemplate template :JSONUtil.fromJson(getJson(ActivityHighResearchTemplate.class),
				new TypeReference<ArrayList<ActivityHighResearchTemplate>>() {
				})) {
			template.init();
			highResearchMap.put(template.getId(), template);
		}
		this.highResearchMap = ImmutableMap.copyOf(highResearchMap);
	}
	//累计充值
	private void loadActiveRechargeSevenDay() {
		Map<Integer, ActiveRechargeSevendayImpl> tempMap = Maps.newConcurrentMap();
		for(ActiveRechargeSevendayImpl template :JSONUtil.fromJson(getJson(ActiveRechargeSevendayImpl.class),
				new TypeReference<ArrayList<ActiveRechargeSevendayImpl>>() {
				})) {
			template.init();
			tempMap.put(template.getDay(), template);
		}
		this.sevenDayMap = ImmutableMap.copyOf(tempMap);
	}

	private void loadActivityConsume() {
		List<ActiveConsumeTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveConsumeTemplateImpl.class), new TypeReference<List<ActiveConsumeTemplateImpl>>() {
		});

		Map<Integer, ActiveConsumeTemplateImpl> tmp = Maps.newConcurrentMap();
		list.forEach(t->{
			t.init();
			tmp.put(t.getId(), t);
		});

		consumeTemplateMap = ImmutableMap.copyOf(tmp);
	}

	public ActiveConsumeTemplateImpl getConsumeCfg(int id) {
		return consumeTemplateMap.getOrDefault(id, null);
	}
	
	private void loadActivityShooting() {
		Map<Integer, ActivityShootingTemplate> shootingMap = Maps.newConcurrentMap();
		for(ActivityShootingTemplate template :JSONUtil.fromJson(getJson(ActivityShootingTemplate.class),
				new TypeReference<ArrayList<ActivityShootingTemplate>>() {
				})) {
			template.init();
			shootingMap.put(template.getIndex(), template);
		}
		this.shootingMap = ImmutableMap.copyOf(shootingMap);
	}
	
	private void loadTankDraw() {
		Map<Integer, ActiveDrawPaperTemplateImpl> tempMap = Maps.newConcurrentMap();
		for(ActiveDrawPaperTemplateImpl template :JSONUtil.fromJson(getJson(ActiveDrawPaperTemplateImpl.class),
				new TypeReference<ArrayList<ActiveDrawPaperTemplateImpl>>() {
				})) {
			template.init();
			tempMap.put(template.getId(), template);
		}
		this.tankDraw = ImmutableMap.copyOf(tempMap);
	}

	private void loadTurn() {
		Map<Integer, ActiveRechargeTurningendTemplateImpl> map = Maps.newConcurrentMap();
		List<ActiveRechargeTurningendTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveRechargeTurningendTemplateImpl.class), new TypeReference<List<ActiveRechargeTurningendTemplateImpl>>() {
		});
		list.forEach(e->{
			e.init();
			map.put(e.getId(), e);
		});

		turnMap = ImmutableMap.copyOf(map);
	}

	private void loadOnline() {
		Map<Integer, ActiveOnlineRewardTemplateImpl> map = Maps.newConcurrentMap();
		List<ActiveOnlineRewardTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveOnlineRewardTemplateImpl.class), new TypeReference<List<ActiveOnlineRewardTemplateImpl>>() {
		});
		list.forEach(e->{
			e.init();
			map.put(e.getId(), e);
		});

		onlineMap = ImmutableMap.copyOf(map);
	}

	private void loadLoginTurn() {
		Map<Integer, ActiveLoginTurningendTemplateImpl> map = Maps.newConcurrentMap();
		List<ActiveLoginTurningendTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveLoginTurningendTemplateImpl.class), new TypeReference<List<ActiveLoginTurningendTemplateImpl>>() {
		});
		list.forEach(e->{
			e.init();
			map.put(e.getId(), e);
		});

        loginTruningEndMaxDay = list.stream().map(ActiveLoginTurningendTemplate::getDays).max(Comparator.naturalOrder()).orElse(0);

        loinTrunMap = ImmutableMap.copyOf(map);
	}

	private void loadDouYin() {
		Map<Integer, DouyinSevenLoginRewardTemplateImpl> map = Maps.newConcurrentMap();
		List<DouyinSevenLoginRewardTemplateImpl> list = JSONUtil.fromJson(getJson(DouyinSevenLoginRewardTemplateImpl.class), new TypeReference<List<DouyinSevenLoginRewardTemplateImpl>>() {
		});
		list.forEach(e->{
			e.init();
			map.put(e.getId(), e);
		});


		douyinMap = ImmutableMap.copyOf(map);
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(ActivityMainTemplate.class, ActivitySevenLoginWelfareTemplate.class,
				ActivityArmyFeteTemplate.class,ActivitySignTemplate.class,ActivitySignTotalTemplate.class,
				ActivityCircleRewardTemplate.class,ActiveCircleTemplate.class,
				ActiveRechargeFeedbackTemplateExt.class,
				ActiveRechargeTemplateExt.class,
				ActiveSscarRewardTemplate.class,
				ActiveSscarCountTemplateImpl.class,
				ActiveSscarShopTemplate.class,
				ActiveNewplayerRankTemplateImpl.class,
				ActivityThreeDayTemplate.class,
				ActivityHighResearchTemplate.class,
				ActiveRechargeSevendayImpl.class,
				ActivityHighResearchTemplate.class,
				ActiveConsumeTemplateImpl.class,
				ActivityShootingTemplate.class,
				ActiveDrawPaperTemplateImpl.class,
				ActiveCircleRewardJuzuoTemplateImpl.class,
				ActivitySignMonthRewardTemplate.class,
				ActiveSscarRewardLibraryTemplateImpl.class,
				ActiveRechargeTurningendTemplateImpl.class,
				ActiveOnlineRewardTemplateImpl.class,
				ActiveLoginTurningendTemplateImpl.class,
				DouyinSevenLoginRewardTemplateImpl.class
				);
	}
	// 查找合适时间点的后勤补给
	public ActivityArmyFeteTemplate getFitArmyFeteTemplate(int lv) {
		return armyFeteList.stream().filter(e -> e.isCanReward(lv)).findAny().orElse(null);
	}
	public Map<Integer, ActivitySevenLoginWelfareTemplate> getLoginWelfareMap() {
		return loginWelfareMap;
	}
	public ActivitySevenLoginWelfareTemplate getSevenLoginWelfare(int id) {
		return loginWelfareMap.get(id);
	}
	public ActivitySignTemplate getActivitySignTemplate(int id) {
		return signMap.get(id);
	}
	
	public ActivitySignTotalTemplate getActivitySignTotalTemplate(int id){
		return signTotalMap.get(id);
	}
	public int getMaxSignDay() {
		return maxSignDay;
	}
	public int getCircleMinute(int id) {
		return circleMap.getOrDefault(id, 0); 
	}
	public ActivityCircleRewardTemplate getCircleRewardTemplate(int id) {
		return circleRewardMap.get(id);
	}
	public List<ActivityCircleRewardTemplate> getCircleRewardList(){
		return Lists.newArrayList(circleRewardMap.values());
	}
	public ActiveCircleRewardJuzuoTemplateImpl getCircleRewardJzTemplate(int id) {
		return circleJzRewardMap.get(id);
	}
	public List<ActiveCircleRewardJuzuoTemplateImpl> getCircleRewardJzList(){
		return Lists.newArrayList(circleJzRewardMap.values());
	}
	//获取石油补给的所有开始时间
	public List<Integer> getArmyFeteStartHours(){
		return this.armyFeteStartHours; 
	}
	//累计充值
	public ActiveRechargeTemplateExt getRecharge(int id) {
		return this.rechargeMap.get(id);
	}
	//充值回馈
	public ActiveRechargeFeedbackTemplateExt getRechargeFeedBack(int id) {
		return this.rechargeFeedbackMap.get(id);
	}
	
	public ActivityMainTemplate getActivityMainTemplate(int type) {
		return activityMainMap.get(type);
	}
		public List<ActivityThreeDayTemplate> getActivityThreeDayList(){
		return Lists.newArrayList(this.threeDayMap.values());
	}
	
	public ActivityThreeDayTemplate getActivityThreeDayTemplate(int id){
		return threeDayMap.get(id); 
	}

	public ActiveThreeDayPointTemplateImpl getActivityThreeDayPointTemplate(int id){
		return threeDayPointMap.get(id);
	}

	public List<ActiveThreeDayPointTemplateImpl> getPlayerCanRewardThreeDayPointTemplates(int score, List<Integer> receiveIds){
		return threeDayPointMap.values().stream().filter(e -> e.getPoint() <= score).filter(e -> !receiveIds.contains(e.getId())).collect(Collectors.toList());
	}

	public Collection<ActiveThreeDayPointTemplateImpl> getThreeDayPointTemplates(){
		return threeDayPointMap.values();
	}

	public List<ActivityThreeDayTemplate> getNoCompleteThreeDayList(int type,PlayerThreeDayValue threeDayValue){
		//该类型的所有任务
    	List<ActivityThreeDayTemplate> taskList = getActivityThreeDayList().stream().filter(temp->temp.getType() == type).collect(Collectors.toList());; 
    	//去掉已经领取的任务
    	taskList.removeAll(threeDayValue.getIdList());
    	return taskList; 
	}
	
	public ActivityHighResearchTemplate getActivityHighResearchTemplate(int id){
		return highResearchMap.get(id); 
	}
	//获得7日活动奖励信息
	public List<Items> getServenDayReward(int day) {
		if(sevenDayMap.containsKey(day)) {
			return sevenDayMap.get(day).getRewards();
		}
		return null;
	} 
	
	public ActivityShootingTemplate getActivityShootingTemplate(int id){
		return shootingMap.get(id); 
	}

	public List<ActivityShootingTemplate> getActivityShootingTemplateByScore(int score){
		return shootingMap.values().stream().filter(e-> e.getCircle() <= score).collect(Collectors.toList());
	}
	
	public ActiveDrawPaperTemplateImpl getTankDraw(int id) {
		return tankDraw.get(id);
	}
	//获取玩家当前等级能够领取的补给
	public List<Items> getArmyFeteReward(int lv){
		ActivityArmyFeteTemplate template = this.armyFeteList.stream().filter(t ->lv>=t.getMinLv()&&lv<=t.getMaxLv()).findFirst().orElse(null);
		if(template ==null){
			return null;
		}
		return template.getRewards();
	}


	public List<Items> getSignMothReward(int round) {
		ActivitySignMonthRewardTemplate template = this.signMonthMap.get(round%signMonthMap.size()+1);
		if(template==null){
			return Lists.newArrayList();
		}
		return template.getRewards();
	}

	public List<Integer> getCircleByTime(long second){
		int minute = (int) (second/60);
		return circleMap.entrySet().stream().filter(e -> e.getValue() <= minute).map(e -> e.getKey()).collect(Collectors.toList());
	}

	public ActiveRechargeTurningendTemplateImpl getTurnCfg(int id) {
		return turnMap.getOrDefault(id, null);
	}

	public ActiveOnlineRewardTemplateImpl getOnlineCfg(int id) {
		return onlineMap.getOrDefault(id, null);
	}

	public ActiveLoginTurningendTemplateImpl getLoginTurnCfg(int id) {
		return loinTrunMap.getOrDefault(id, null);
	}

	/**
	 * 最多可领天数
	 *
	 * @return
	 */
	public int getLoginTruningEndMaxDay() {
		return loginTruningEndMaxDay;
	}

	public DouyinSevenLoginRewardTemplateImpl getDouyinCfg(int id) {
		return douyinMap.getOrDefault(id, null);
	}

	/**
	 * 抖音7日活动 活动动总天数
	 *
	 * @return
	 */
	public int getDouyinMaxCnt() {
		return douyinMap.size();
	}
}


