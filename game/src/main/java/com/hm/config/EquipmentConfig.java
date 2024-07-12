package com.hm.config;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.*;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.templaextra.*;
import com.hm.enums.EquCircleType;
import com.hm.enums.TankAttrType;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.model.item.Items;
import com.hm.model.player.Equipment;
import com.hm.model.player.Player;
import com.hm.util.ItemUtils;

import java.util.*;
import java.util.stream.Collectors;


@Config
public class EquipmentConfig extends ExcleConfig{
	private Map<Integer,PlayerArmExtraTemplate> equMap = Maps.newConcurrentMap();
	private Map<Integer,PlayerArmStrengthenTemplate> strengthenMap = Maps.newConcurrentMap();
	private Map<Integer,PlayerArmStoneTemplate> stoneMap = Maps.newConcurrentMap();
	private Table<Integer,Integer,PlayerArmCircleExtraTemplate> cricleTable = HashBasedTable.create();
	private Map<Integer,PlayerArmSuitExtraTemplate> suitMap = Maps.newConcurrentMap();
	private int maxStrengthenLv;

	private ArrayListMultimap<Integer,Integer> equipPosMap;
	private ArrayListMultimap<Integer,Integer> stonePosMap;

	public static PlayerArmStoneTemplate[] CanStoneLvUpArrays;

	private ListMultimap<Integer,PlayerArmCircleExtraTemplate> cricleMap = ArrayListMultimap.create();

	@Override
	public void loadConfig() {
		loadEquConifg();
		loadEquStrengthenConifg();
		loadStoneConfig();
		loadCircleConifg();
		loadEquSuitConfig();
	}
	
	public void loadEquConifg(){
		Map<Integer,PlayerArmExtraTemplate> equMap = Maps.newConcurrentMap();
		for(PlayerArmExtraTemplate template:JSONUtil.fromJson(getJson(PlayerArmExtraTemplate.class), new TypeReference<ArrayList<PlayerArmExtraTemplate>>(){})){
			template.init();
			equMap.put(template.getId(), template);
		}
		this.equMap = ImmutableMap.copyOf(equMap);

		ArrayListMultimap<Integer,Integer> posMap = ArrayListMultimap.create();
		for (int i = 1; i <= GameConstants.EquipMaxPos; i++) {
			int pos = i;
			List<Integer> list = this.equMap.values().stream().filter(e -> e.getArm_pos() == pos)
					.sorted(Comparator.comparingInt(PlayerArmExtraTemplate::getQuality))
					.map(e -> e.getId())
					.collect(Collectors.toList());
			posMap.putAll(pos,list);
		}
		this.equipPosMap = posMap;
	}
	public void loadEquStrengthenConifg(){
		Map<Integer,PlayerArmStrengthenTemplate> strengthenMap = Maps.newConcurrentMap();
		for(PlayerArmStrengthenTemplate template:JSONUtil.fromJson(getJson(PlayerArmStrengthenTemplate.class), new TypeReference<ArrayList<PlayerArmStrengthenTemplate>>(){})){
			template.init();
			strengthenMap.put(template.getLevel(), template);
			this.maxStrengthenLv = Math.max(template.getLevel(), maxStrengthenLv);
		}
		this.strengthenMap = ImmutableMap.copyOf(strengthenMap);
	}

	public void loadStoneConfig(){
		Map<Integer,PlayerArmStoneTemplate> stoneMap = Maps.newConcurrentMap();
		for(PlayerArmStoneTemplate template:JSONUtil.fromJson(getJson(PlayerArmStoneTemplate.class), new TypeReference<ArrayList<PlayerArmStoneTemplate>>(){})){
			template.init();
			stoneMap.put(template.getId(), template);
		}
		this.stoneMap = ImmutableMap.copyOf(stoneMap);

		//找出可以升级的宝石列表
		CanStoneLvUpArrays = this.stoneMap.values()
				.stream().filter(e -> e.getNext_id() > 0).sorted(Comparator.comparingInt(PlayerArmStoneTemplate::getId))
				.toArray(PlayerArmStoneTemplate[]::new);

		ArrayListMultimap<Integer,Integer> posMap = ArrayListMultimap.create();
		for (int i = 1; i <= GameConstants.EquipMaxPos; i++) {
			int pos = i;
			List<Integer> list = this.stoneMap.values().stream().filter(e -> e.getArm_pos() == pos)
					.sorted(Comparator.comparingInt(PlayerArmStoneTemplate::getLevel))
					.map(e -> e.getId())
					.collect(Collectors.toList());
			posMap.putAll(pos,list);
		}
		this.stonePosMap = posMap;
	}
	public void loadCircleConifg(){
		ListMultimap<Integer,PlayerArmCircleExtraTemplate> cricleMap = ArrayListMultimap.create();
		Table<Integer,Integer,PlayerArmCircleExtraTemplate> cricleTable = HashBasedTable.create();
		for(PlayerArmCircleExtraTemplate template:JSONUtil.fromJson(getJson(PlayerArmCircleExtraTemplate.class), new TypeReference<ArrayList<PlayerArmCircleExtraTemplate>>(){})){
			template.init();
			cricleTable.put(template.getType(),template.getLevel(), template);
		}
		this.cricleTable = ImmutableTable.copyOf(cricleTable);

		for (int type : cricleTable.rowKeySet()) {
			cricleMap.putAll(type,cricleTable.row(type).values().stream()
							.sorted(Comparator.comparingInt(PlayerArmCircleExtraTemplate::getRequest_case))
					.collect(Collectors.toList()));
		}
		this.cricleMap = cricleMap;
	}
	
	private void loadEquSuitConfig() {
		Map<Integer,PlayerArmSuitExtraTemplate> suitMap = Maps.newConcurrentMap();
		for(PlayerArmSuitExtraTemplate template:JSONUtil.fromJson(getJson(PlayerArmSuitExtraTemplate.class), new TypeReference<ArrayList<PlayerArmSuitExtraTemplate>>(){})){
			template.init();
			suitMap.put(template.getId(), template);
		}
		this.suitMap = ImmutableMap.copyOf(suitMap);
		
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(PlayerArmExtraTemplate.class,PlayerArmStrengthenTemplate.class,PlayerArmStoneTemplate.class,PlayerArmCircleExtraTemplate.class,PlayerArmSuitExtraTemplate.class);
	}
	
	
	public int getMaxStrengthenLv() {
		return maxStrengthenLv;
	}

	public PlayerArmExtraTemplate getEquTemplate(int id){
		return equMap.get(id);
	}
	public PlayerArmStrengthenTemplate getStrengthen(int lv){
		return strengthenMap.get(lv);
	}
	public PlayerArmStoneTemplate getStone(int id){
		return stoneMap.get(id);
	}


	public PlayerArmCircleExtraTemplate getCircle(EquCircleType type,int lv){
		return getCircle(type.getType(),lv);
	}
	public PlayerArmCircleExtraTemplate getCircle(Player player,EquCircleType type){
		int lv = player.playerEquip().getCircleLv(type);
		if(lv <= 0) return null;
		return getCircle(type,lv);
	}

	public PlayerArmCircleExtraTemplate getCircle(int type,int lv){
		return cricleTable.get(type, lv);
	}

	//装备本身的属性
	public Map<TankAttrType,Double> getEquAttr(int equId){
		if(equId==0){
			return Maps.newHashMap();
		}
		PlayerArmExtraTemplate template = getEquTemplate(equId);
		if(template==null){
			return Maps.newHashMap();
		}
		return template.getAttrMap();
	}
	//强化增加的属性
	public Map<TankAttrType, Double> getStrengthenAttr(int id, int strengthenLv) {
		if(strengthenLv==0){
			return Maps.newHashMap();
		}
		PlayerArmStrengthenTemplate template = getStrengthen(strengthenLv);
		if(template == null){
			return Maps.newHashMap();
		}
		return template.getAttrByBody(id);
	}

	//宝石增加的属性
	public Map<TankAttrType, Double> getStoneAttr(int stoneId) {
		if(stoneId==0){
			return Maps.newHashMap();
		}
		PlayerArmStoneTemplate template = getStone(stoneId);
		if(template == null){
			return Maps.newHashMap();
		}
		return template.getAttrMap();
	}
	//从startLv强化到endLv所需的消耗
	public List<Items> getStrengthenCost(int startLv,int endLv){
		List<Items> cost = Lists.newArrayList();
		for(int lv=startLv;lv<endLv;lv++){
			PlayerArmStrengthenTemplate template = getStrengthen(lv);
			if(template!=null){
				cost.addAll(template.getCost());
			}
		}
		return ItemUtils.mergeItemList(cost);
	}
	
	public List<PlayerArmSuitExtraTemplate> getSuitMap(){
		return Lists.newArrayList(suitMap.values());
	}
	
	public PlayerArmSuitExtraTemplate getSuit(int id){
		return suitMap.get(id);
	}

	public PlayerArmExtraTemplate getNextUpEquip(int id,int curEquipId) {
		List<Integer> eqList = this.equipPosMap.get(id);
		int equId = eqList.get(0);
		if(curEquipId > 0) {
			int index = eqList.indexOf(curEquipId);
			if(index < 0 || index >= eqList.size()) {
				return null;
			}
			equId = eqList.get(index+1);
		}
		return equMap.get(equId);
	}

	public List<Integer> getPosSortStoneIdList(int id) {
		return this.stonePosMap.get(id);
	}

	/**
	 * 检查石头是否能够装备到该部位
	 * @param stoneId
	 * @param id
	 * @return
	 */
	public boolean checkStoneBody(int stoneId,int id){
		PlayerArmStoneTemplate template = stoneMap.get(stoneId);
		if(template==null){
			return false;
		}
		return template.getArm_pos()==id;
	}

	public int getCircleLv(Player player, EquCircleType type) {
		int minLv = type.getMinLv(player);
		List<PlayerArmCircleExtraTemplate> templateList = this.cricleMap.get(type.getType());
		for (int i = templateList.size()-1; i >= 0; i--) {
			PlayerArmCircleExtraTemplate temp = templateList.get(i);
			if(minLv >= temp.getRequest_case()) {
				return temp.getLevel();
			}
		}
		return 0;
	}


	//获取玩家装备所有强化属性
	public Map<TankAttrType,Double> getAllEquStrengthenAttrMap(Player player) {
		Map<TankAttrType,Double> attrMap = Maps.newHashMap();
		for (Equipment equ : player.playerEquip().getEqus()) {
			if(equ.getStrengthenLv() > 0) {
				Map<TankAttrType,Double> strengthenAttrMap = getStrengthenAttr(equ.getId(),equ.getStrengthenLv());
				strengthenAttrMap.forEach((key,value)->attrMap.merge(key, value, (x,y)->(x+y)));
			}
		}
		return attrMap;
	}
	//获取玩家装备宝石的属性总和
	public Map<TankAttrType,Double> getAllEquStoneAttrMap(Player player) {
		Map<TankAttrType,Double> attrMap = Maps.newHashMap();
		for (Equipment equ : player.playerEquip().getEqus()) {
			for (int stoneId : equ.getStone()) {
				if(stoneId > 0) {
					Map<TankAttrType,Double> stoneAttrMap = getStoneAttr(stoneId);
					stoneAttrMap.forEach((key,value)->attrMap.merge(key, value, (x,y)->(x+y)));
				}
			}
		}
		return attrMap;
	}
}
