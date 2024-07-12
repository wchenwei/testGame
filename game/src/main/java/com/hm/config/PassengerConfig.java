package com.hm.config;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.*;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.libcore.spring.SpringUtil;
import com.hm.action.item.ItemBiz;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.temlate.TankCrewPieceTemplate;
import com.hm.config.excel.templaextra.*;
import com.hm.enums.TankAttrType;
import com.hm.model.item.Items;
import com.hm.model.passenger.Passenger;
import com.hm.model.tank.TankAttr;

import java.util.*;
import java.util.stream.Collectors;


@Config
public class PassengerConfig extends ExcleConfig{
	private Map<Integer,TankPassengerTemplate> passengerMap = Maps.newConcurrentMap();
	private Map<Integer,TankCrewPieceTemplate> pieceMap = Maps.newConcurrentMap();
	private Table<Integer,Integer,TankPassengerStarTemplate> stars = HashBasedTable.create();
	private Table<Integer,Integer,TankPassengerSuitAttriTemplate> suitSkills = HashBasedTable.create();
	private Map<Integer,TankPassengerSuitAttriTemplate> suitSkillMaps = Maps.newConcurrentMap();
	private Table<Integer,Integer,TankPassengerUpgradeTemplate> lvUps = HashBasedTable.create();
	private Map<Integer,Integer> lvMaxs = Maps.newConcurrentMap();
	private Map<Integer,Integer> starMaxs = Maps.newConcurrentMap();
	private Map<Integer,TankPassengerWashTemplate> washs = Maps.newConcurrentMap();
	private Map<Integer,TankPassengerWashAttriTemplate> washAttriMap = Maps.newConcurrentMap();
	
	@Override
	public void loadConfig() {
		loadPassengerConifg();
		loadPieceConifg();
		loadStarConfig();
		loadSuitAttriConfig();
		loadLvUpConfig();
		loadWashConfig();
		loadWashAttriConfig();
	}
	
	public void loadPassengerConifg(){
		Map<Integer,TankPassengerTemplate> passengerMap = Maps.newConcurrentMap();
		for(TankPassengerTemplate template:JSONUtil.fromJson(getJson(TankPassengerTemplate.class), new TypeReference<ArrayList<TankPassengerTemplate>>(){})){
			template.init();
			passengerMap.put(template.getId(), template);
		}
		this.passengerMap = ImmutableMap.copyOf(passengerMap);
	}
	public void loadPieceConifg(){
		Map<Integer,TankCrewPieceTemplate> pieceMap = Maps.newConcurrentMap();
		for(TankCrewPieceTemplate template:JSONUtil.fromJson(getJson(TankCrewPieceTemplate.class), new TypeReference<ArrayList<TankCrewPieceTemplate>>(){})){
			pieceMap.put(template.getId(), template);
		}
		this.pieceMap = ImmutableMap.copyOf(pieceMap);
	}
	public void loadStarConfig(){
		Table<Integer,Integer,TankPassengerStarTemplate> stars = HashBasedTable.create();
		for(TankPassengerStarTemplate template:JSONUtil.fromJson(getJson(TankPassengerStarTemplate.class), new TypeReference<ArrayList<TankPassengerStarTemplate>>(){})){
			template.init();
			stars.put(template.getQuality(),template.getStar_level(), template);
			int max = Math.max(starMaxs.getOrDefault(template.getQuality(),0),template.getStar_level());
			this.starMaxs.put(template.getQuality(), max);
		}
		this.stars = ImmutableTable.copyOf(stars);
	}
	
	public void loadSuitAttriConfig(){
		Table<Integer,Integer,TankPassengerSuitAttriTemplate> suitSkills = HashBasedTable.create();
		Map<Integer,TankPassengerSuitAttriTemplate> suitSkillMaps = Maps.newConcurrentMap();
		for(TankPassengerSuitAttriTemplate template:JSONUtil.fromJson(getJson(TankPassengerSuitAttriTemplate.class), new TypeReference<ArrayList<TankPassengerSuitAttriTemplate>>(){})){
			template.init();
			suitSkills.put(template.getType(),template.getNum(), template);
			suitSkillMaps.put(template.getId(), template);
		}
		this.suitSkillMaps = ImmutableMap.copyOf(suitSkillMaps);
		this.suitSkills = ImmutableTable.copyOf(suitSkills);
	}
	
	public void loadLvUpConfig(){
		Table<Integer,Integer,TankPassengerUpgradeTemplate> lvUps = HashBasedTable.create();
		for(TankPassengerUpgradeTemplate template:JSONUtil.fromJson(getJson(TankPassengerUpgradeTemplate.class), new TypeReference<ArrayList<TankPassengerUpgradeTemplate>>(){})){
			template.init();
			lvUps.put(template.getAttri_type(),template.getAttri_lv(), template);
			int max = Math.max(lvMaxs.getOrDefault(template.getAttri_type(),0),template.getAttri_lv());
			this.lvMaxs.put(template.getAttri_type(), max);
		}
		this.lvUps = ImmutableTable.copyOf(lvUps);
	}
	
	public void loadWashConfig(){
		Map<Integer,TankPassengerWashTemplate> washs = Maps.newConcurrentMap();
		for(TankPassengerWashTemplate template:JSONUtil.fromJson(getJson(TankPassengerWashTemplate.class), new TypeReference<ArrayList<TankPassengerWashTemplate>>(){})){
			template.init();
			washs.put(template.getId(), template);
		}
		this.washs = ImmutableMap.copyOf(washs);
	}
	
	public void loadWashAttriConfig(){
		Map<Integer,TankPassengerWashAttriTemplate> washAttriMap = Maps.newConcurrentMap();
		for(TankPassengerWashAttriTemplate template:JSONUtil.fromJson(getJson(TankPassengerWashAttriTemplate.class), new TypeReference<ArrayList<TankPassengerWashAttriTemplate>>(){})){
			template.init();
			washAttriMap.put(template.getId(), template);
		}
		this.washAttriMap = ImmutableMap.copyOf(washAttriMap);
	}
	
	//获取乘员碎片
	public TankCrewPieceTemplate getPassengerPieceTemplate(int pieceId){
		return this.pieceMap.get(pieceId);
	}
	//获取乘员
	public TankPassengerTemplate getPassenger(int id){
		return this.passengerMap.get(id);
	}
	
	//获取合成乘员的消耗
	public Items getComposeCost(int id,int num){
		TankPassengerTemplate template =  getPassenger(id);
		if(template==null){
			return null;
		}
		Items cost = template.getComposeCost();
		cost.setCount(cost.getCount()*num);
		return cost;
	}
	
	public int getMaxLv(int id){
		TankPassengerTemplate template = getPassenger(id);
		return lvMaxs.get(template.getAttri_type());
	}
	
	public int getMaxStar(int type){
		return lvMaxs.get(type);
	}
	//获取升级消耗
	public List<Items> getLvUpCost(int id,int startLv,int endLv){
		List<Items> costs = Lists.newArrayList();
		TankPassengerTemplate template =  getPassenger(id);
		if(template==null){
			return costs;
		}
		for(int i=startLv;i<endLv;i++){
			costs.addAll(lvUps.get(template.getAttri_type(), i).getCosts());
		}
		ItemBiz itemBiz = SpringUtil.getBean(ItemBiz.class);
		return itemBiz.createItemList(costs);
	}
	//获取升星消耗
	public List<Items> getStarUpCost(int id,int star){
		List<Items> costs = Lists.newArrayList();
		TankPassengerTemplate template =  getPassenger(id);
		if(template==null){
			return costs;
		}
		return stars.get(template.getQuality(), star).getCosts();
	}
	
	//获取升星消耗
	public List<Items> getStarUpCost(int id,int startStar,int endStar){
		List<Items> costs = Lists.newArrayList();
		TankPassengerTemplate template =  getPassenger(id);
		if(template==null){
			return costs;
		}
		for(int i=startStar;i<endStar;i++){
			costs.addAll(stars.get(template.getQuality(), i).getCosts());
		}
		return costs;
	}
	
	//获取培养
	public List<Items> getCultureCost(int id){
		List<Items> costs = Lists.newArrayList();
		TankPassengerTemplate template =  getPassenger(id);
		if(template==null){
			return costs;
		}
		return washs.get(template.getQuality()).getCosts();
	}
	
	public List<Integer> createSkills(int id,int count){
		List<Integer> skills = Lists.newArrayList();
		TankPassengerTemplate template =  getPassenger(id);
		TankPassengerWashTemplate washTemplate = washs.get(template.getQuality());
		for(int i=1;i<=count;i++){
			skills.add(i>=3?washTemplate.getWash():washTemplate.getWash2());
		}
		Collections.shuffle(skills);
		return skills;
	}
	//获取套装激活的id
	public List<Integer> getSuitAttriIds(int id,int num){
		return suitSkills.row(id).values().stream().filter(t ->num>=t.getNum()).map(t ->t.getId()).collect(Collectors.toList());
	}
	@Override
	public List<String> getDownloadFile() {
		return getConfigName(TankPassengerTemplate.class,TankPassengerUpgradeTemplate.class,TankPassengerSuitAttriTemplate.class,TankCrewPieceTemplate.class,TankPassengerStarTemplate.class,TankPassengerWashTemplate.class,TankPassengerWashAttriTemplate.class);
	}
	//获取乘员本身属性
	public TankAttr getPassengerAttri(Passenger passenger) {
		TankAttr tankAttr = new TankAttr();
		TankPassengerTemplate template =  getPassenger(passenger.getId());
		if(template==null){
			return null;
		}
		int lv = passenger.getLv();
		int star = passenger.getStar();
		//乘员等级属性部分
		tankAttr.addAttr(lvUps.get(template.getAttri_type(), lv).getAttrMap());
		double buff = stars.get(template.getQuality(), star).getBuff();
		//乘员培养+升星属性部分
		Arrays.stream(passenger.getSkills()).forEach(skillId ->{
			if(skillId>0){
				TankPassengerWashAttriTemplate washAttrTemplate = washAttriMap.get(skillId);
				Map<TankAttrType, Double> map = washAttrTemplate.getAttrMap(buff);
				tankAttr.addAttr(map);
			}
		});
		return tankAttr;
	}
	public TankPassengerSuitAttriTemplate getPassengerSuitAttr(int id){
		return this.suitSkillMaps.get(id);
	}
	//获取套装属性
	public TankAttr getSuitAttri(List<Integer> skillIds) {
		TankAttr tankAttr = new TankAttr();
		skillIds.forEach(t ->{
			TankPassengerSuitAttriTemplate template = suitSkillMaps.get(t);
			if(template!=null){
				tankAttr.addAttr(template.getAttrMap());
			}
		});
		return tankAttr;
	}
	
	//获取套装激活的技能（真正的技能）
	public List<Integer> getSuitSkillIds(List<Integer> ids) {
		List<Integer> skillIds = Lists.newArrayList();
		ids.forEach(t ->{
			TankPassengerSuitAttriTemplate template = suitSkillMaps.get(t);
			if(template!=null&&template.getSkill()>0){
				skillIds.add(template.getSkill());
			}
		});
		return skillIds;
	}

	public List<Items> getRetireReward(Passenger passenger) {
		List<Items> rewards = Lists.newArrayList();
		TankPassengerTemplate template = getPassenger(passenger.getId());
		if(template==null){
			return rewards;
		}
		//乘员本身返还的材料
		rewards.addAll(template.getRetireReward());
		//等级返还的材料
		rewards.addAll(getLvUpCost(passenger.getId(), 1, passenger.getLv()));
		//星级返还的材料(星级从0开始)
		rewards.addAll(getStarUpCost(passenger.getId(),0,passenger.getStar()));
		return rewards;
	}

	public int getStarMax(int id) {
		TankPassengerTemplate template =  getPassenger(id);
		if(template==null){
			return 0;
		}
		return starMaxs.get(template.getQuality());
	}
	
}
