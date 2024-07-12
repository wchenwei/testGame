package com.hm.config.excel;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.*;
import com.hm.config.excel.temlate.*;
import com.hm.config.excel.templaextra.*;
import com.hm.enums.CommonValueType;
import com.hm.enums.TankAttrType;
import com.hm.enums.TankRareType;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.util.lv.LvAgent;
import com.hm.libcore.util.string.StringUtil;
import com.hm.model.item.Items;
import com.hm.model.tank.Tank;
import com.hm.model.tank.TankAttr;
import com.hm.util.MathUtils;
import com.hm.war.sg.setting.TankSetting;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Config
public class TankConfig extends ExcleConfig{
	private Map<Integer, TankSetting> tankMap = Maps.newHashMap(); //坦克基本配置
	private Map<Integer, TankLevelTemplate> tankLevelMap = Maps.newHashMap();//坦克升级
	private Map<Integer, TankPaperTemplate> tankPaperMap = Maps.newHashMap(); //坦克图纸
	
	private Map<Integer, StarUnlockTemplateImpl> tankStarMap = Maps.newHashMap(); //坦克升星
	private Table<Integer,Integer,ReformTemplate> tankReformTable = HashBasedTable.create();//改造配置
	private Map<TankAttrType,Integer> powerRateMap = Maps.newHashMap(); //战力系数
	private Table<Integer,Integer, StarRareTemplate> starRateTable = HashBasedTable.create(); //星级系数加成
	
	private Map<Integer, FactoryTemplateImpl> tankFactory = Maps.newHashMap(); //坦克随机权重配置表
	private LvAgent<WarGodTemplateImpl> warGoldLevel;//战神等级
	private Map<Integer, DriverTemplate> driverMap = Maps.newHashMap(); //车长
	private Map<Integer, DriverLvTemplate> driverLvMap = Maps.newHashMap(); //车长升级 
	private Table<Integer, Integer, DriverLvTemplate> driverLvTable = HashBasedTable.create(); //车长升级
	private Map<Integer, Integer> driverMaxLvMap = Maps.newHashMap(); //车长最大级
	private Table<Integer, Integer, Integer> driverEvolveMaxLvMap = HashBasedTable.create(); //车长每进化等级可升的最大等级

	private Table<Integer,Integer,FriendTemplate> friendTable = HashBasedTable.create(); //坦克羁绊
	private int tankLvMax; //坦克最大等级
	private int tankStarMax; //坦克最大星级
	private int tankReLvMax; //坦克改造最大等级
	//科技树id，科技排序，科技信息
	private Table<Integer,Integer,ChipTemplateImpl> chipTable = HashBasedTable.create(); //坦克科技信息
	private List<OilCostTemplate> oilCosts = Lists.newArrayList();

	private List<TankEvoGiftTemplateImpl> tankEvoGift = Lists.newArrayList();//坦克超进化礼包
	private Map<Integer, TankEvoGiftTemplateImpl> tankEvoGiftMap = Maps.newHashMap(); //坦克超进化礼包
	private Map<Integer, StarLevelTemplateImpl> starLevelMap = Maps.newHashMap();//星级
	private Table<Integer, Integer, Integer> starLastNodeMap = HashBasedTable.create();//类型-星级-最后一个节点
	private ListMultimap<Integer, StarLevelTemplateImpl> starLevelMultimap = ArrayListMultimap.create();//类型-星级
	private Map<Integer, TankMasterTemplateImpl> tankMasterMap = Maps.newHashMap();// 图鉴大师
	private Map<Integer, TankMasterRewardTemplateImpl> tankMasterRewardMap = Maps.newHashMap();
	public Map<Integer, Integer> tankReLvMaxLvMap = Maps.newHashMap();// 由改造的等级确定的tank可以升的最大等级
	private TankMasterTemplateImpl lastMasterTemplate;
	private int maxMasterStar;
	//军衔等级限制的tank等级
	private int[] MilitaryLimitTankLV;

	//SS坦克id
	public static List<Integer> SSTankIdList;

	@Override
	public void loadConfig() {
		loadTankSetting();
		loadTankLevel();
		loadTankPaper();
		loadTankStar(); 
		loadTankReform();
		loadTankPowerRate();
		loadStarRate();
		loadTankFactory();
		loadWarGod(); 
		loadDriver(); 
		loadDriverLv();
		loadFriend();
		loadTankChip();
		loadOilConfig();
		loadTankEvoGiftConfig();
		loadStarLevelConfig();
		loadTankMasterConfig();
	}

	private void loadTankMasterConfig() {
		this.tankMasterMap = json2ImmutableMap(TankMasterTemplate::getStar_total, TankMasterTemplateImpl.class);
		this.lastMasterTemplate = tankMasterMap.values().stream().max(Comparator.comparing(TankMasterTemplate::getStar_total)).get();
		this.maxMasterStar = lastMasterTemplate.getStar_total();
		this.tankMasterRewardMap = json2ImmutableMap(TankMasterRewardTemplate::getId, TankMasterRewardTemplateImpl.class);
	}

	private void loadStarLevelConfig() {
		this.starLevelMap = json2ImmutableMap(StarLevelTemplateImpl::getId, StarLevelTemplateImpl.class);
		ListMultimap<Integer, StarLevelTemplateImpl> templateListMultimap = ArrayListMultimap.create();
		Table<Integer, Integer, Integer> tempTable = HashBasedTable.create();
		starLevelMap.values().forEach(e -> {
			templateListMultimap.put(e.getType(), e);
			Integer node = tempTable.get(e.getType(), e.getStar());
			if (node == null || node < e.getNode()){
				tempTable.put(e.getType(), e.getStar(), e.getNode());
			}
		});
		this.starLastNodeMap = ImmutableTable.copyOf(tempTable);
		for (Integer type : templateListMultimap.keySet()) {
			List<StarLevelTemplateImpl> levelTemplates = templateListMultimap.get(type);
			levelTemplates.forEach(e -> e.calAttr(levelTemplates));
		}
		this.starLevelMultimap = ImmutableListMultimap.copyOf(templateListMultimap);
	}

	public void loadTankSetting() {
		Map<Integer, TankSetting> tankMap = Maps.newHashMap();
        List<TankSetting> templateList= JSONUtil.fromJson(getJson(TankSetting.class), new TypeReference<ArrayList<TankSetting>>() {});
        tankMap = templateList.stream()
				.collect(Collectors.toMap(TankSetting::getId, Function.identity()));
		tankMap.values().stream().forEach(e -> e.init());
		this.tankMap = ImmutableMap.copyOf(tankMap);
		this.SSTankIdList = ImmutableList.copyOf(tankMap.values().stream().filter(e -> e.getRare() >= TankRareType.SSR.getType()).map(e -> e.getId()).collect(Collectors.toSet()));

		log.info("坦克基本配置加载完成");
    }

	public void loadTankLevel() {
		Map<Integer, TankLevelTemplate> tankLevelMap = Maps.newHashMap();
        List<TankLevelTemplateImpl> templateList= JSONUtil.fromJson(getJson(TankLevelTemplateImpl.class), new TypeReference<ArrayList<TankLevelTemplateImpl>>() {});
        templateList.forEach(e -> e.init(templateList));

		Map<Integer, Integer> lvTemMap = Maps.newHashMap();
		Map<Integer, List<TankLevelTemplateImpl>> collect = templateList.stream().collect(Collectors.groupingBy(TankLevelTemplateImpl::getNeedReLv));
		for (Map.Entry<Integer, List<TankLevelTemplateImpl>> entry : collect.entrySet()) {
			lvTemMap.put(entry.getKey(), entry.getValue().stream().map(TankLevelTemplate::getLevel).max(Integer::compareTo).orElse(0));
		}
		this.tankReLvMaxLvMap = ImmutableMap.copyOf(lvTemMap);
		tankLevelMap = templateList.stream().collect(Collectors.toMap(TankLevelTemplate::getLevel, Function.identity()));
		this.tankLevelMap = ImmutableMap.copyOf(tankLevelMap);
		this.tankLvMax = tankLevelMap.keySet().stream().max(Comparator.comparing(Function.identity())).get();

		int maxMLv = this.tankLevelMap.values().stream().mapToInt(e -> e.getMilitary_level()).max().getAsInt();
		int[] MilitaryLimitTankLV = new int[maxMLv+1];
		for (int i = 0; i <= maxMLv; i++) {
			final int tempLv = i;
			MilitaryLimitTankLV[i] = tankLevelMap.values().stream()
					.filter(e -> e.getMilitary_level() <= tempLv)
					.mapToInt(TankLevelTemplate::getLevel)
					.max().orElse(0);
		}
		this.MilitaryLimitTankLV = MilitaryLimitTankLV;
		log.info("坦克升级配置加载完成");
	}

	public void loadTankPaper() {
        List<TankPaperTemplate> templateList= JSONUtil.fromJson(getJson(TankPaperTemplate.class), new TypeReference<ArrayList<TankPaperTemplate>>() {});
		Map<Integer, TankPaperTemplate> tankPaperMap = templateList.stream().collect(Collectors.toMap(TankPaperTemplate::getPaper_id, Function.identity()));
		this.tankPaperMap = ImmutableMap.copyOf(tankPaperMap);
		log.info("坦克图纸配置加载完成");
	}
	public void loadTankStar() {
        List<StarUnlockTemplateImpl> templateList= JSONUtil.fromJson(getJson(StarUnlockTemplateImpl.class), new TypeReference<ArrayList<StarUnlockTemplateImpl>>() {});
		templateList.forEach(e -> e.init(templateList));
		Map<Integer, StarUnlockTemplateImpl> tankStarMap = templateList.stream().collect(Collectors.toMap(StarUnlockTemplateImpl::getStar, Function.identity()));
		this.tankStarMap = ImmutableMap.copyOf(tankStarMap);
		this.tankStarMax = tankStarMap.keySet().stream().max(Comparator.comparing(Function.identity())).get();
		log.info("坦克升星配置加载完成");
	}

	
	public void loadTankReform() {
        List<ReformTemplate> templateList= JSONUtil.fromJson(getJson(ReformTemplate.class), new TypeReference<ArrayList<ReformTemplate>>() {});
        Table<Integer,Integer,ReformTemplate> tankReformTable = HashBasedTable.create();//改造配置
        for(ReformTemplate template:templateList){
        	template.init();
        	tankReformTable.put(template.getReform_type(), template.getReform_level(), template);
        	if(template.getReform_level()>this.tankReLvMax){
        		this.tankReLvMax = template.getReform_level();
        	}
        }
		for (Integer type : tankReformTable.rowKeySet()) {
			Map<Integer, ReformTemplate> templateMap = tankReformTable.row(type);
			Collection<ReformTemplate> templates = templateMap.values();
			templates.forEach(e -> e.calTotalCost(templates));
		}
        this.tankReformTable = ImmutableTable.copyOf(tankReformTable);
        log.info("坦克改造配置加载完成");
	}
	
	public void loadTankPowerRate() {
		Map<TankAttrType,Integer> powerRateMap = Maps.newHashMap();
        List<PowerRateTemplate> templateList= JSONUtil.fromJson(getJson(PowerRateTemplate.class), new TypeReference<ArrayList<PowerRateTemplate>>() {});
        for(PowerRateTemplate temp:templateList){
        	powerRateMap.put(TankAttrType.getType(temp.getAttr_id()), temp.getRate());
        }
		this.powerRateMap = ImmutableMap.copyOf(powerRateMap);
		log.info("坦克战力系数配置加载完成");
	}
	
	public void loadStarRate() {
		Table<Integer,Integer,StarRareTemplate> starRateTable = HashBasedTable.create();
        List<StarRareTemplate> templateList= JSONUtil.fromJson(getJson(StarRareTemplate.class), new TypeReference<ArrayList<StarRareTemplate>>() {});
        for(StarRareTemplate temp:templateList){
        	starRateTable.put(temp.getStar(), temp.getRare(), temp);
        }
		this.starRateTable = ImmutableTable.copyOf(starRateTable);
		log.info("坦克星级系数配置加载完成");
	}
	
	public void loadTankFactory() {
		Map<Integer, FactoryTemplateImpl> factory = Maps.newHashMap();
		List<FactoryTemplateImpl> templateList= JSONUtil.fromJson(getJson(FactoryTemplateImpl.class), new TypeReference<ArrayList<FactoryTemplateImpl>>() {});
        templateList.forEach(temp->{
        	temp.init();
        	factory.put(temp.getRare(), temp);
        });
		this.tankFactory = ImmutableMap.copyOf(factory);
		log.info("坦克随机权重配置表加载完成");
	}
	public void loadWarGod() {
		List<WarGodTemplateImpl> templateList= JSONUtil.fromJson(getJson(WarGodTemplateImpl.class), new TypeReference<ArrayList<WarGodTemplateImpl>>() {});
		this.warGoldLevel = new LvAgent<>(templateList, true);
		log.info("战神配置加载完成");
	}
	
	public void loadDriver() {
		List<DriverTemplate> templateList= JSONUtil.fromJson(getJson(DriverTemplate.class), new TypeReference<ArrayList<DriverTemplate>>() {});
		Map<Integer,DriverTemplate> driverMap = Maps.newHashMap();
		templateList.forEach(temp->{
        	temp.init();
        	driverMap.put(temp.getTank_id(), temp);
        });
		
		this.driverMap = ImmutableMap.copyOf(driverMap);
		log.info("车长配置加载完成");
	}
	
	public void loadDriverLv() {
		List<DriverLvTemplate> templateList= json2List(DriverLvTemplate.class);

		Map<Integer,DriverLvTemplate> driverLvMap = Maps.newHashMap();
		Map<Integer,Integer> driverMaxLvMap = Maps.newHashMap();
		Table<Integer, Integer, DriverLvTemplate> driverLvTable = HashBasedTable.create();
		Table<Integer, Integer, Integer> driverEvolveLvTable = HashBasedTable.create();
		templateList.forEach(temp->{
			temp.init(templateList);
        	driverLvMap.put(temp.getId(), temp);
			driverLvTable.put(temp.getRare_id(), temp.getLevel(), temp);
        });

		for (Integer rare : driverLvTable.rowKeySet()) {
			Map<Integer, DriverLvTemplate> templateMap = driverLvTable.row(rare);
			driverMaxLvMap.put(rare, templateMap.keySet().stream().max(Comparator.comparing(Function.identity())).get());
			Map<Integer, List<DriverLvTemplate>> collect = templateMap.values().stream().collect(Collectors.groupingBy(DriverLvTemplate::getNeedEvolveLv));
			for (Map.Entry<Integer, List<DriverLvTemplate>> entry : collect.entrySet()) {
				driverEvolveLvTable.put(rare, entry.getKey(), entry.getValue().stream().map(DriverLvTemplate::getLevel).max(Integer::compareTo).orElse(0));
			}
		}

		this.driverLvMap = ImmutableMap.copyOf(driverLvMap);
		this.driverMaxLvMap = ImmutableMap.copyOf(driverMaxLvMap);
		this.driverLvTable = ImmutableTable.copyOf(driverLvTable);
		this.driverEvolveMaxLvMap = ImmutableTable.copyOf(driverEvolveLvTable);

		log.info("车长升级配置加载完成");
	}
	
	public void loadFriend() {
		Table<Integer,Integer,FriendTemplate> friendTable = HashBasedTable.create();
        List<FriendTemplate> templateList= JSONUtil.fromJson(getJson(FriendTemplate.class), new TypeReference<ArrayList<FriendTemplate>>() {});
        for(FriendTemplate temp:templateList){
        	temp.init(); 
        	friendTable.put(temp.getTank_id(), temp.getIndex(),temp);
        }
		this.friendTable = ImmutableTable.copyOf(friendTable);
		log.info("坦克羁绊配置加载完成");
	}
	
	public void loadTankChip() {
		Table<Integer,Integer,ChipTemplateImpl> tempTable = HashBasedTable.create();
        List<ChipTemplateImpl> templateList= JSONUtil.fromJson(getJson(ChipTemplateImpl.class), new TypeReference<ArrayList<ChipTemplateImpl>>() {});
        for(ChipTemplateImpl temp:templateList){
        	temp.init(); 
        	tempTable.put(temp.getTreeid(), temp.getPosition(), temp);
        }
		this.chipTable = ImmutableTable.copyOf(tempTable);
		log.info("坦克科技配置加载完成");
	}
	
	public void loadOilConfig() {
        List<OilCostTemplate> templateList= JSONUtil.fromJson(getJson(OilCostTemplate.class), new TypeReference<ArrayList<OilCostTemplate>>() {});
		this.oilCosts = templateList;
		log.info("坦克耗油配置加载完成");
	}

	public void loadTankEvoGiftConfig() {
		Map<Integer, TankEvoGiftTemplateImpl> tempMap = Maps.newHashMap();
		List<TankEvoGiftTemplateImpl> templateList= JSONUtil.fromJson(getJson(TankEvoGiftTemplateImpl.class), new TypeReference<ArrayList<TankEvoGiftTemplateImpl>>() {});
		tempMap = templateList.stream()
				.collect(Collectors.toMap(TankEvoGiftTemplateImpl::getTank_id, Function.identity()));
		tempMap.values().stream().forEach(e -> e.init());
		this.tankEvoGiftMap = ImmutableMap.copyOf(tempMap);
		this.tankEvoGift = templateList;
		log.info("坦克坦克超进化礼包加载完成");
	}
	//校验是否要增加购买次数
	public boolean checkEvoGift(int tankId, int rechargeId, int times) {
		if(!tankEvoGiftMap.containsKey(tankId)) {
			return false;
		}
		TankEvoGiftTemplateImpl tempEvoGift = tankEvoGiftMap.get(tankId);
		if(!tempEvoGift.containsRechargeIds(rechargeId)) {
			return false;
		}
		if(times>=tempEvoGift.getTimes(rechargeId)) {
			return false;
		}
		return true;
	}
	public boolean checkEvoGiftOpen(int tankId) {
		if(!tankEvoGiftMap.containsKey(tankId)) {
			return false;
		}
		return true;
	}

	public int getEvoGiftShowTimes(int tankId) {
		if(!tankEvoGiftMap.containsKey(tankId)) {
			return 0;
		}
		TankEvoGiftTemplateImpl tempEvoGift = tankEvoGiftMap.get(tankId);
		return tempEvoGift.getLimit_buy();
	}

	public int getEvoGiftServerLv(int tankId) {
		if(!tankEvoGiftMap.containsKey(tankId)) {
			return -1;
		}
		TankEvoGiftTemplateImpl tempEvoGift = tankEvoGiftMap.get(tankId);
		return tempEvoGift.getServer_lv();
	}
	
	public ChipTemplateImpl getTankTechChip(int chipId, int position) {
		return chipTable.get(chipId, position);
	}
	//根据科技id，获取科技position的最大数量
	public int getTechMaxCount(int chipId) {
		return chipTable.row(chipId).keySet().size();
	}
	public Map<Integer, ChipTemplateImpl> getTech(int chipId) {
		return chipTable.row(chipId);
	}
	

	public TankSetting getTankSetting(int id) {
		return tankMap.get(id);
	}
	
	public int getTankLvMax(){
		return tankLvMax; 
	}
	
	public int getTankReLvMax() {
		return tankReLvMax;
	}

	public TankLevelTemplate getTankLevelTemplate(int lv){
		return tankLevelMap.get(lv);
	}
	public List<TankLevelTemplate> getTankLevelList(){
		return Lists.newArrayList(tankLevelMap.values());
	}

    public TankPaperTemplate getTankPaperTemplate(int paperId) {
	    return tankPaperMap.get(paperId);
    }
    
    public StarUnlockTemplateImpl getTankStarTemplate(int star) {
		return tankStarMap.get(star);
	}

	public int getTankStarMax(){
    	return tankStarMax;
    }

    
    public int getTankUnlock(int star){
    	return tankStarMap.get(star).getNeed_paper(); 
    }

	// 根据突破等级获取tank可以升的最大等级
	public int getTankMaxLvByReLv(int reLv){
		return this.tankReLvMaxLvMap.getOrDefault(reLv, 0);
	}
    
    
    public ReformTemplate getTankReformTemplate(Tank tank) {
    	int reType = tankMap.get(tank.getId()).getReform_type();
    	int reLv = tank.getReLv(); 
		return tankReformTable.get(reType, reLv);
	}

	public StarLevelTemplateImpl getTankStarLevelTemplate(int tankId, int star, int node){
		Integer type = tankMap.get(tankId).getType();
		if (node <= 0){
			star = star - 1;
			if (star <= 0){
				return null;
			}
			node = starLastNodeMap.get(type, star);
		}
		return getStarLevelTemplate(star, node, type);
	}

	private StarLevelTemplateImpl getStarLevelTemplate(int star, int node, Integer type) {
		List<StarLevelTemplateImpl> starLevelTemplates = starLevelMultimap.get(type);
		if (CollUtil.isNotEmpty(starLevelTemplates)){
			return starLevelTemplates.stream().filter(e -> e.getStar() == star)
					.filter(e -> e.getNode() == node).findFirst().orElse(null);
		}
		return null;
	}

	public int getTankPowerRate(TankAttrType attrType){
		return powerRateMap.getOrDefault(attrType,0); 
	}
	
	public Map<TankAttrType,Integer> getTankPowerRateMap(){
		return powerRateMap; 
	}
	
	public double getStarRate(int relv,int rare){
		StarRareTemplate starRareTemplate = starRateTable.get(relv, rare);
		if (starRareTemplate != null){
			return starRareTemplate.getValue();
		}
		return 0;
	}

	public int getStarMasterScore(int star,int rare){
		StarRareTemplate starRareTemplate = starRateTable.get(star, rare);
		if (starRareTemplate != null){
			return starRareTemplate.getTank_master_scores();
		}
		return 0;
	}

	public FactoryTemplateImpl getFactoryTemplate(int rare) {
		return tankFactory.get(rare);
	}
	public WarGodTemplate getWarGodTemplate(int lv){
		return this.warGoldLevel.getLevelValue(lv);
	}
	public int calNewPlayerCombatLv(long totalCombat,int curLv) {
		return this.warGoldLevel.getLevel(curLv, totalCombat);
	}
	public int getWarGodLvMax(){
		return this.warGoldLevel.getMaxLv(); 
	}
	public DriverTemplate getDriverTemplate(int tankId){
		return driverMap.get(tankId);
	}
	
	public DriverLvTemplate getDriverLvTemplate(Tank tank){
		TankSetting tankSetting = getTankSetting(tank.getId());
		return getDriverLvTemplate(tankSetting.getRare(), tank.getDriver().getLv());
	}

	public DriverLvTemplate getDriverLvTemplate(int tankRare, int lv){
		return driverLvTable.get(tankRare, lv);
	}

	public int getDriverLvMax(int tankRare) {
		return driverMaxLvMap.getOrDefault(tankRare, 0);
	}

	// 当前进化等级可升的最大等级
	public int getDriverUpLvMax(int tankRare, int evolveLv){
		Map<Integer, Integer> map = driverEvolveMaxLvMap.row(tankRare);
		if (CollUtil.isNotEmpty(map)){
			return map.getOrDefault(evolveLv, 0);
		}
		return 0;
	}

	public FriendTemplate getFriendTemplate(int tankId,int index){
		return friendTable.get(tankId, index);
	}
	//是否为禁止的坦克坦克阵营或类型
	public boolean isProhibit(int tankId,String prohibitCamp,String prohibitArms){
		if(StringUtils.isBlank(prohibitCamp)&&StringUtils.isBlank(prohibitArms)){
			return false;
		}
		TankSetting tankSetting = getTankSetting(tankId);
		if(tankSetting==null){//找不到该坦克配置也视为禁止
			return true;
		}
		List<Integer> camps = StringUtil.splitStr2IntegerList(prohibitCamp, ",");
		List<Integer> arms = StringUtil.splitStr2IntegerList(prohibitArms, ",");
		return camps.contains(tankSetting.getCountry())||arms.contains(tankSetting.getType());
	}
	
	//是否为必须出站的坦克资质
	public boolean isAllowRare(int tankId,String str){
		if(StringUtils.isBlank(str)){
			return true;
		}
		TankSetting tankSetting = getTankSetting(tankId);
		if(tankSetting==null){//找不到该坦克配置也视为不符合
			return false;
		}
		List<Integer> rares = StringUtil.splitStr2IntegerList(str, ",");
		return rares.contains(tankSetting.getRare());
	}
	
	/**
	 * getTechChipAttr:(获得部落科技开启完成后的所有属性加成). <br/>  
	 * @author zxj  
	 * @param chipId
	 * @return  使用说明
	 */
	public TankAttr getTechChipAttr(int chipId) {
		TankAttr attr = new TankAttr();
		Map<Integer, ChipTemplateImpl> tempChip = this.getTech(chipId);
		tempChip.values().forEach(template->{
			attr.addAttr(template.getTankAttr(template.getMax_level()));
		});
		return attr;
	}
	

	//根据坦克战力获取坦克耗油系数
	public double getTankOilRate(long combat){
		OilCostTemplate template = this.oilCosts.stream().filter(t ->combat>=t.getPower_down()&&combat<=t.getPower_up()).findFirst().orElse(null);
		if(template==null){
			return 6;
		}
		return template.getPower_proportion();
	}
	
	public long getPaperExp(List<Items> itemsList) {
		return itemsList.stream().mapToLong(e->{
			TankPaperTemplate paperTemp = tankPaperMap.get(e.getId());
			return paperTemp.getExp()*e.getCount();
		}).sum();
	}
	//为坦克随机出一个魔改技能
	public int randomMagicSkill(Tank tank) {
        CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
        //一阶段魔改等级上限
        int reformLvLimit = commValueConfig.getCommValue(CommonValueType.MagicReformLimit);
	    int grade = tank.getTankMagicReform().getReformLv()<reformLvLimit?1:2;
		List<Integer> fullSkillIds = tank.getTankMagicReform().getFullSkillIds();
        TankSetting tankSetting = getTankSetting(tank.getId());
		List<Integer> skillIds = grade==1?tankSetting.getMagicSkillIds():tankSetting.getSuperMagicSkillIds();
		skillIds.removeAll(fullSkillIds);
		return skillIds.get(MathUtils.random(0, skillIds.size()));
	}

	public int getTankReformQuality(int reformType, int reLv) {
		ReformTemplate reform = tankReformTable.get(reformType, reLv);
		if(null!=reform) {
			return reform.getQuality();
		}
		return 0;
	}

	public int getTankReformMaxLv(Tank tank){
		int reType = tankMap.get(tank.getId()).getReform_type();
		Map<Integer, ReformTemplate> templateMap = tankReformTable.row(reType);
		if (CollUtil.isNotEmpty(templateMap)){
			return templateMap.keySet().stream().max(Integer::compareTo).orElse(0);
		}
		return 0;
	}

	public int getTankDriverSoulId(int tankId) {
		DriverTemplate template =  driverMap.get(tankId);
		return template==null?0:template.getSoul();
	}
	//获取所有tankId(玩家可获得)
	public List<Integer> getAllTankIds(){
		return tankMap.values().stream().filter(t->t.getDisplay_playerlist()==1).map(t->t.getId()).collect(Collectors.toList());
	}
	//获取某个品质的tankId(玩家可获得的)
	public List<Integer> getTankIdsByRare(int rare){
		return tankMap.values().stream().filter(t ->t.getDisplay_playerlist()==1&&t.getRare()==rare).map(t->t.getId()).collect(Collectors.toList());
	}

	public List<DriverLvTemplate> getDriverLvUpAll(int lv, int expectLv) {
		return driverLvMap.values().stream().filter(e -> e.getLevel() >= lv && e.getLevel() < expectLv)
				.sorted(Comparator.comparing(DriverLevelTemplate::getLevel))
				.collect(Collectors.toList());
	}

	public int getTankRare(int tankId){
		TankSetting tankSetting = getTankSetting(tankId);
		return tankSetting==null?0:tankSetting.getRare();
	}

	public TankMasterRewardTemplateImpl getTankMasterRewardTemplate(int id){
		return tankMasterRewardMap.get(id);
	}

	public TankMasterTemplateImpl getTankMasterTemplate(int star){
		if (star > maxMasterStar){
			return lastMasterTemplate;
		}
		return tankMasterMap.get(star);
	}

	public int getPlayerMaxTankLvByMilitaryLv(int militaryLv){
		if(militaryLv >= this.MilitaryLimitTankLV.length) {
			return Integer.MAX_VALUE;
		}
		return this.MilitaryLimitTankLV[militaryLv];
	}
}





