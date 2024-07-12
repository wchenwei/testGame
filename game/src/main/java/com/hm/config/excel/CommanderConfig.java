/**  
 * Project Name:SLG_GameFuture.  
 * File Name:ItemConfig.java  
 * Package Name:com.hm.config.excel  
 * Date:2017年12月29日上午11:39:27  
 * Copyright (c) 2017, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
*/  
  
package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.libcore.util.lv.LvAgent;
import com.hm.config.excel.temlate.CarSkillCostTemplate;
import com.hm.config.excel.temlate.MilitaryTemplate;
import com.hm.config.excel.templaextra.*;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.tank.TankAttr;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**  
 * ClassName:CommanderConfig <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason:   TODO ADD REASON. <br/>  
 * Date:     2017年12月29日 上午11:39:27 <br/>  
 * @author   zqh  
 * @version  1.1  
 * @since    
 */
@Slf4j
@Config
public class CommanderConfig extends ExcleConfig {
	private Map<Integer, CarTemplate> carMap = Maps.newConcurrentMap();
	private Map<Integer, CarSkillTemplate> carSkillMap = Maps.newConcurrentMap();
	@Getter
	private Map<Integer, MilitaryExtraTemplate> militaryMap = Maps.newConcurrentMap();
	private Map<Integer, CarModelExtraTemplate> carModelMap = Maps.newConcurrentMap(); 
	private Map<Integer, SuperWeaponExTemplate> superWeaponMap = Maps.newConcurrentMap();
	private Map<Integer, SuperWeaponUpgradeExtrTemplate> superWeaponUpgradeMap = Maps.newConcurrentMap(); 
	private int carLvMax; 
	private int militaryLvMax;
    private int carSkillLvMax;
    private int superWeaponLvMax;
    private int superWeaponUpgradeMax;
    //军工等级信息
    private LvAgent<MilitaryProjectLevelImpl> militaryProjectLevel;

    //自动驾驶
    private Map<Integer, CarDriverUpgradeExtraTemplate> autoDriveLvs = Maps.newConcurrentMap();
    private Map<Integer, CarDriverUnlockExtraTemplate> autoDrives = Maps.newConcurrentMap();
    private int autoDriveMaxLv;

    private Map<Integer, Integer> paperIdAndCarId = Maps.newConcurrentMap();

    @Override
    public void loadConfig() {
        loadCar();
        loadCarSkill();
        loadMilitary();
        loadCarModel();
        loadSuperWeapon();
        loadMilitaryProjectLevel();
        loadSuperWeaponUpgrade();
		loadAutoDriveLvConfig();
		loadAutoDriveConfig();
	}
	private void loadAutoDriveConfig() {
		List<CarDriverUnlockExtraTemplate> list = JSONUtil.fromJson(getJson(CarDriverUnlockExtraTemplate.class), new TypeReference<ArrayList<CarDriverUnlockExtraTemplate>>(){});
		list.forEach(t ->t.init());
		Map<Integer,CarDriverUnlockExtraTemplate> autoDrives = list.stream().collect(Collectors.toMap(CarDriverUnlockExtraTemplate::getId, Function.identity()));
		this.autoDrives = ImmutableMap.copyOf(autoDrives);
		
	}
	private void loadAutoDriveLvConfig() {
		List<CarDriverUpgradeExtraTemplate> list = JSONUtil.fromJson(getJson(CarDriverUpgradeExtraTemplate.class), new TypeReference<ArrayList<CarDriverUpgradeExtraTemplate>>(){});
		list.forEach(t ->t.init());
		Map<Integer,CarDriverUpgradeExtraTemplate> autoDriveLvs = list.stream().collect(Collectors.toMap(CarDriverUpgradeExtraTemplate::getLevel, Function.identity()));
		this.autoDriveLvs = ImmutableMap.copyOf(autoDriveLvs);
		this.autoDriveMaxLv = list.stream().mapToInt(t ->t.getLevel()).max().orElse(0);
	}
	//加载指挥官军工信息
	private void loadMilitaryProjectLevel() {
		List<MilitaryProjectLevelImpl> templateList = JSONUtil.fromJson(getJson(MilitaryProjectLevelImpl.class), new TypeReference<ArrayList<MilitaryProjectLevelImpl>>(){});
		templateList.stream().forEach(e->{
			e.init();
		});
		this.militaryProjectLevel = new LvAgent<MilitaryProjectLevelImpl>(templateList, true, true);
		log.info("指挥官军工等级信息加载完成");
	}
	private void loadCar() {
		List<CarTemplate> templateList = JSONUtil.fromJson(getJson(CarTemplate.class),
				new TypeReference<ArrayList<CarTemplate>>() {
				});
		Map<Integer, CarTemplate> carMap = Maps.newConcurrentMap();
		templateList.forEach(temp -> {
			temp.init();
			carMap.put(temp.getLevel(), temp);
		});
		this.carMap = ImmutableMap.copyOf(carMap);
		this.carLvMax = carMap.keySet().stream().max(Comparator.comparing(Function.identity())).orElse(0);

		log.info("座驾配置加载完成");
	}
	private void loadCarSkill() {
		List<CarSkillTemplate> templateList = JSONUtil.fromJson(getJson(CarSkillTemplate.class),
				new TypeReference<ArrayList<CarSkillTemplate>>() {
				});
		Map<Integer, CarSkillTemplate> carSkillMap = Maps.newConcurrentMap();
		templateList.forEach(temp -> {
			temp.init();
			carSkillMap.put(temp.getLevel(), temp);
		});
		this.carSkillMap = ImmutableMap.copyOf(carSkillMap);
		this.carSkillLvMax = carSkillMap.keySet().stream().max(Comparator.comparing(Function.identity())).orElse(0);

		log.info("座驾技能配置加载完成");
	}
	
	private void loadMilitary() {
		List<MilitaryExtraTemplate> templateList = JSONUtil.fromJson(getJson(MilitaryExtraTemplate.class),
				new TypeReference<ArrayList<MilitaryExtraTemplate>>() {
				});
		Map<Integer, MilitaryExtraTemplate> militaryMap = Maps.newConcurrentMap();
		templateList.forEach(temp -> {
			temp.init();
			militaryMap.put(temp.getLevel(), temp);
		});
		this.militaryMap = ImmutableMap.copyOf(militaryMap);
		this.militaryLvMax = militaryMap.keySet().stream().max(Comparator.comparing(Function.identity())).orElse(0);

		log.info("军衔配置加载完成");
	}
	
	private void loadCarModel() {
		List<CarModelExtraTemplate> templateList = JSONUtil.fromJson(getJson(CarModelExtraTemplate.class),
				new TypeReference<ArrayList<CarModelExtraTemplate>>() {
				});
		Map<Integer, CarModelExtraTemplate> carModelMap = Maps.newConcurrentMap();
		templateList.forEach(temp -> {
            temp.init();
            carModelMap.put(temp.getId(), temp);

            paperIdAndCarId.put(temp.getPaperId(), temp.getId());
        });
		this.carModelMap = ImmutableMap.copyOf(carModelMap);


		log.info("座驾幻化配置加载完成");
	}
	
	private void loadSuperWeapon() {
		List<SuperWeaponExTemplate> templateList = JSONUtil.fromJson(getJson(SuperWeaponExTemplate.class),
				new TypeReference<ArrayList<SuperWeaponExTemplate>>() {
				});
		Map<Integer, SuperWeaponExTemplate> superWeaponMap = Maps.newConcurrentMap();
		templateList.forEach(temp -> {
			temp.init();
			superWeaponMap.put(temp.getLevel(), temp);
		});
		this.superWeaponMap = ImmutableMap.copyOf(superWeaponMap);
		this.superWeaponLvMax = superWeaponMap.keySet().stream().max(Comparator.comparing(Function.identity())).orElse(0);

		log.info("战鼓配置加载完成");
	}
	
	private void loadSuperWeaponUpgrade() {
		List<SuperWeaponUpgradeExtrTemplate> templateList = JSONUtil.fromJson(getJson(SuperWeaponUpgradeExtrTemplate.class),
				new TypeReference<ArrayList<SuperWeaponUpgradeExtrTemplate>>() {
				});
		Map<Integer, SuperWeaponUpgradeExtrTemplate> superWeaponUpgradeMap = Maps.newConcurrentMap();
		templateList.forEach(temp -> {
			temp.init();
			superWeaponUpgradeMap.put(temp.getLevel(), temp);
		});
		this.superWeaponUpgradeMap = ImmutableMap.copyOf(superWeaponUpgradeMap);
		this.superWeaponUpgradeMax = superWeaponUpgradeMap.keySet().stream().max(Comparator.comparing(Function.identity())).orElse(0);

		log.info("战鼓配置加载完成");
	}
	
	@Override
	public List<String> getDownloadFile() {
		return getConfigName(CarTemplate.class,CarSkillCostTemplate.class,
				MilitaryTemplate.class,CarModelExtraTemplate.class,
				SuperWeaponExTemplate.class,
				MilitaryProjectLevelImpl.class,SuperWeaponUpgradeExtrTemplate.class,
				CarDriverUpgradeExtraTemplate.class,
				CarDriverUnlockExtraTemplate.class);
	}
	
	public CarTemplate getCarTemplate(int lv){
		return carMap.get(lv);
	}

	public Map<Integer, CarTemplate> getCarMap() {
		return carMap;
	}

	public MilitaryExtraTemplate getMilitaryExtraTemplate(int lv){
		return militaryMap.get(lv);
	}
	public MilitaryExtraTemplate getMilitaryExtraTemplate(Player player){
		return militaryMap.get(player.playerCommander().getMilitaryLv());
	}

	public int getCarLvMax() {
		return carLvMax;
	}
	public int getMilitaryLvMax() {
		return militaryLvMax;
	}
	public List<Integer> getUnlockSkills(int lv) {
		return carMap.values().stream().filter(t->lv>=t.getLevel()).filter(t->t.getMain_skill()>0).map(t->t.getMain_skill()).collect(Collectors.toList());
	}
	
	public int getCarSkillLvMax() {
		return carSkillLvMax;
	}
	public CarSkillTemplate getCarSkillTemplate(int lv){
		return carSkillMap.get(lv);
	}
	
	public List<CarSkillTemplate> getCarSkillList(){
		return Lists.newArrayList(carSkillMap.values());
	}
	
	public CarModelExtraTemplate getCarModelExtraTemplate(int id){
		return carModelMap.get(id);
	}
	public SuperWeaponExTemplate getSuperWeaponMap(int lv) {
		return superWeaponMap.get(lv);
	}
	public SuperWeaponUpgradeExtrTemplate getSuperWeaponUpgrade(int lv) {
		return superWeaponUpgradeMap.get(lv);
	}
	public int getSuperWeaponLvMax() {
		return superWeaponLvMax;
	}
	public int getSuperWeaponUpgradeMax() {
		return superWeaponUpgradeMax;
	}
	//获取指挥官军工等级
	public int getMilitaryProLv(long totalExp) {
		return militaryProjectLevel.getLevel(totalExp);
	}
	public int getMilitaryProMaxLv() {
		return militaryProjectLevel.getMaxLv();
	}
	public MilitaryProjectLevelImpl getMilitaryProject(int lv) {
		return militaryProjectLevel.getLevelValue(lv);
	}
	//获取座驾技能从startLv升级到endLv的花费
	
	public List<Items> getCarSkillLvUpCost(int startLv,int endLv){
		List<Items> cost = Lists.newArrayList();
		for(int i=startLv;i<endLv;i++){
			cost.add(getCarSkillTemplate(i).getCostItems());
		}
		return cost;
	}
	public int getAutoDriveMaxLv() {
		return autoDriveMaxLv;
	}
	public CarDriverUpgradeExtraTemplate getAutoDriveLvTemplate(int lv) {
        return autoDriveLvs.get(lv);
    }

    //获取自动驾驶特殊等级带来的属性加成
    public TankAttr getAutoDriveExtraAttr(int lv) {
        TankAttr tankAttr = new TankAttr();
        autoDrives.values().stream().filter(t -> lv >= t.getUnlock_lv()).forEach(t -> {
            tankAttr.addAttr(t.getAttrMap());
        });
        return tankAttr;
    }

    public int getCommanderId(int paperId) {
        return paperIdAndCarId.getOrDefault(paperId, 0);
    }

}
  



