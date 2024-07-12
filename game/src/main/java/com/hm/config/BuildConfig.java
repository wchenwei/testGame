package com.hm.config;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.*;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.temlate.*;
import com.hm.config.excel.templaextra.*;
import com.hm.enums.ItemType;
import com.hm.model.item.Items;
import com.hm.model.player.BasePlayer;
import com.hm.model.tank.TankAttr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;


@Config
public class BuildConfig extends ExcleConfig{
	private Table<Integer,Integer, BuildUpTemplate> table = HashBasedTable.create();
	private Map<Integer,BuildingUnlockTemplate> buildMap = Maps.newConcurrentMap();
	private Map<Integer,ItemToolExtraTemplate> toolMap = Maps.newConcurrentMap();//训练工具
	private Map<Integer,TankTrainingGroundTemplate> trains = Maps.newConcurrentMap();//训练场
	private Table<Integer,Integer,TrainingDropTemplate> dropTable = HashBasedTable.create();//训练坦克掉落
	private Map<Integer,TankStrengthBreakTemplate> breakMap = Maps.newConcurrentMap();//坦克强化突破
	private Map<Integer,BuildingEnhanceTemplate> lines = Maps.newConcurrentMap();//养成线
	private Map<Integer,BuildingEnhanceAttriTemplate> enhances = Maps.newConcurrentMap();
	
	private Map<Integer,TankStrengthTaskTemplate> tasks = Maps.newConcurrentMap();
	
	public BuildUpTemplate getTemplateByType(int buildType,int lv) {
		return table.get(buildType,lv);
	}
	@Override
	public void loadConfig() {
//		loadBuildUpgradeConfig();
//		loadBuildUnlockConfig();
		loadToolConfig();
//		loadTrainsConfig();
//		loadDropConfig();
//		loadTankBreakConfig();
//		loadEnhanceLineConfig();
//		loadEnhanceConfig();
//		loadTaskConfig();
		
	}
	
	private void loadTaskConfig() {
		List<TankStrengthTaskTemplate> list = JSONUtil.fromJson(getJson(TankStrengthTaskTemplate.class), new TypeReference<ArrayList<TankStrengthTaskTemplate>>(){});
		Map<Integer,TankStrengthTaskTemplate> tasks = Maps.newConcurrentMap();
		for(TankStrengthTaskTemplate template:list) {
			template.init();
			tasks.put(template.getTask_id(), template);
		}
		this.tasks = ImmutableMap.copyOf(tasks);
	}
	private void loadEnhanceConfig() {
		List<BuildingEnhanceAttriTemplate> list = JSONUtil.fromJson(getJson(BuildingEnhanceAttriTemplate.class), new TypeReference<ArrayList<BuildingEnhanceAttriTemplate>>(){});
		Map<Integer,BuildingEnhanceAttriTemplate> enhances = Maps.newConcurrentMap();
		for(BuildingEnhanceAttriTemplate template:list) {
			enhances.put(template.getId(), template);
		}
		this.enhances = ImmutableMap.copyOf(enhances);
		
	}
	private void loadEnhanceLineConfig() {
		List<BuildingEnhanceTemplate> list = JSONUtil.fromJson(getJson(BuildingEnhanceTemplate.class), new TypeReference<ArrayList<BuildingEnhanceTemplate>>(){});
		Map<Integer,BuildingEnhanceTemplate> lines = list.stream().collect(Collectors.toMap(BuildingEnhanceTemplate::getId, Function.identity()));
		this.lines = ImmutableMap.copyOf(lines);
	}
	private void loadTankBreakConfig() {
		List<TankStrengthBreakTemplate> list = JSONUtil.fromJson(getJson(TankStrengthBreakTemplate.class), new TypeReference<ArrayList<TankStrengthBreakTemplate>>(){});
		list.forEach(t ->t.init());
		Map<Integer,TankStrengthBreakTemplate> breakMap = list.stream().collect(Collectors.toMap(TankStrengthBreakTemplate::getId, Function.identity()));
		this.breakMap = ImmutableMap.copyOf(breakMap);
	}
	private void loadDropConfig() {
		List<TrainingDropTemplate> list = JSONUtil.fromJson(getJson(TrainingDropTemplate.class), new TypeReference<ArrayList<TrainingDropTemplate>>(){});
		Table<Integer,Integer,TrainingDropTemplate> dropTable = HashBasedTable.create();
		for(TrainingDropTemplate template:list) {
			template.init();
			dropTable.put(template.getGround(), template.getType(), template);
		}
		this.dropTable = ImmutableTable.copyOf(dropTable);
	}
	private void loadTrainsConfig() {
		this.trains = json2ImmutableMap(TankTrainingGroundTemplate::getId,TankTrainingGroundTemplate.class);
	}
	private void loadToolConfig() {
		List<ItemToolExtraTemplate> list = JSONUtil.fromJson(getJson(ItemToolExtraTemplate.class), new TypeReference<ArrayList<ItemToolExtraTemplate>>(){});
		list.forEach(t ->t.init());
		Map<Integer,ItemToolExtraTemplate> toolMaps = list.stream().collect(Collectors.toMap(ItemToolExtraTemplate::getId, Function.identity()));
		this.toolMap = ImmutableMap.copyOf(toolMaps);
	}
	private void loadBuildUpgradeConfig(){
		Table<Integer,Integer, BuildUpTemplate> tempTable = HashBasedTable.create();
		for(BuildUpTemplate template:JSONUtil.fromJson(getJson(BuildUpTemplate.class), new TypeReference<ArrayList<BuildUpTemplate>>(){})) {
			template.init();
			tempTable.put(template.getBuild_type(),template.getLv_building(), template);
		}
		this.table = ImmutableTable.copyOf(tempTable);
	}
	
	private void loadBuildUnlockConfig(){
		List<BuildingUnlockTemplate> list = JSONUtil.fromJson(getJson(BuildingUnlockTemplate.class), new TypeReference<ArrayList<BuildingUnlockTemplate>>(){});
		Map<Integer,BuildingUnlockTemplate> buildMap = list.stream().collect(Collectors.toMap(BuildingUnlockTemplate::getId, Function.identity()));
		this.buildMap = ImmutableMap.copyOf(buildMap);
	}
	
	public BuildUpTemplate getBuildUpTemplate(int type,int lv){
		return this.table.get(type, lv);
	}
	
	public BuildingUnlockTemplate getBuild(int id){
		return this.buildMap.get(id);
	}
	
	public List<BuildingUnlockTemplate> getBuilds(){
		return Lists.newArrayList(buildMap.values());
	}
	
	public int getBuildType(int id){
		BuildingUnlockTemplate template = getBuild(id);
		if(template==null){
			return 0;
		}
		return template.getBuild_type();
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(BuildingUpgradeTemplate.class,BuildingUnlockTemplate.class
				,ItemToolExtraTemplate.class,BuildingTrainingGroundTemplate.class,TrainingDropTemplate.class
				,TankStrengthBreakTemplate.class,BuildingEnhanceTemplate.class,BuildingEnhanceAttriTemplate.class
				,TankStrengthTaskTemplate.class);
	}
	public boolean checkBuildBlock(int id, int blockId) {
		BuildingUnlockTemplate template = getBuild(id);
		if(template==null){
			return false;
		}
		//location==0说明不限制该建筑必须在哪个位置
		return template.getLocation()==0||template.getLocation()==blockId;
	}
	public boolean checkProduce(int buildType, int lv, int index) {
		BuildUpTemplate template = getBuildUpTemplate(buildType, lv);
		if(template==null){
			return false;
		}
		return false;
	}
	
	
	public BuildingEnhanceAttriTemplate getEchance(int type,int rank,int lv) {
		Optional<BuildingEnhanceAttriTemplate> optional = this.enhances.values().stream().filter(t ->t.getType()==type&&t.getRank()==rank&&t.getLevel()==lv).findFirst();
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	public ItemToolExtraTemplate getTool(int id) {
		return this.toolMap.get(id);
	}
	
	public BuildingEnhanceTemplate getNeedTool(int id) {
		return this.lines.get(id);
	}
	
	public Items getTankStrengthCost(int id) {
		BuildingEnhanceTemplate template = getNeedTool(id);
		if(template==null) {
			return null;
		}
		return new Items(template.getItem(),1,ItemType.TankStrenTool.getType());
	}
	//玩家满足突破条件所需的鱼的种类和数量
	public Map<Integer,Integer> getBreakNeed(int type,int lv){
		return enhances.values().stream().filter(t ->t.getType()==type&&t.getLevel()==lv).collect(Collectors.toMap(BuildingEnhanceAttriTemplate::getNeed_id, BuildingEnhanceAttriTemplate::getNeed_num));
	}
	//获取突破消耗
	public List<Items> getBreakCost(int type,int lv){
		Optional<TankStrengthBreakTemplate> optional = breakMap.values().stream().filter(t ->t.getType()==type&&t.getLevel()==lv).findFirst();
		if(optional.isPresent()){
			return optional.get().getCosts();
		}
		return Lists.newArrayList();
	}
	
	//训练场
	public TankTrainingGroundTemplate getTrain(int id) {
		return this.trains.get(id);
	}
	//随机npc奖励
	public List<Items> getNpcRewards(int id, int type,int state) {
		return dropTable.get(id, type).getRewards(state);
	}
	/**
	 * 获取n条鱼提供的属性
	 * @param id
	 * @param num
	 * @return
	 */
	public TankAttr getStrengthAttr(int id, int num) {
		BuildingEnhanceTemplate template = getNeedTool(id);
		if(template==null) {
			return null;
		}
		ItemToolExtraTemplate tool = getTool(template.getItem());
		if(tool==null) {
			return null;
		}
		return new TankAttr(tool.getAttrMap(num));
	}
	
	public Map<Integer,TankStrengthTaskTemplate> getTasks(){
		return tasks;
	}
	public TankStrengthTaskTemplate getTask(int taskId) {
		return tasks.get(taskId);
		
	}
	//根据类型取突破最大等级
	public int getBreakLvMax(int type){
		return breakMap.values().stream().filter(t->t.getType()==type).mapToInt(t->t.getLevel()).max().getAsInt();
	}
	
	//获取突破配置
	public TankStrengthBreakTemplate getBreakTemplate(int type,int lv){
		Optional<TankStrengthBreakTemplate> optional =  breakMap.values().stream().filter(t ->t.getType()==type&&t.getLevel()==lv).findFirst();
		if(optional.isPresent()){
			return optional.get();
		}
		return null;
	}
	//获取满足建造条件但是没有建造的建筑
	public List<Integer> getCanBuildIds(BasePlayer player){
		int centreLv = player.playerBuild().getCenterLv();
		return buildMap.values().stream().filter(t ->player.playerLevel().getLv()>=t.getLv_player()&&centreLv>=t.getLv_mbuild()&&!player.playerBuild().getBlocks().values().contains(t.getId())).map(t ->t.getId()).collect(Collectors.toList());
	}
	
}
