package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.WarCraftSkillTemplate;
import com.hm.config.excel.templaextra.WarCraftSkillUpgradeTemplateImpl;
import com.hm.config.excel.templaextra.WarCraftTemplateImpl;
import com.hm.enums.TankType;
import com.hm.model.item.Items;
import com.hm.model.tank.TankAttr;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Config
public class WarCraftConfig extends ExcleConfig {
	private Map<Integer, WarCraftTemplateImpl> warCraftMap = Maps.newHashMap();
	//属性加成type,等级,属性
	private Table<Integer,Integer,TankAttr> tankAttrTable = HashBasedTable.create();
	
	private int warCraftMaxLv = 0;
	
	
	public int getWarCraftMaxLv() {
		return warCraftMaxLv;
	}
	private Map<Integer, WarCraftSkillUpgradeTemplateImpl> warCraftSkillUpMap = Maps.newHashMap();
	//skillid, 等级， 升级信息
	private Table<Integer, Integer, WarCraftSkillUpgradeTemplateImpl> warCraftSkillTable = HashBasedTable.create();
	
	private Map<Integer, WarCraftSkillTemplate> warCraftSkillMap = Maps.newHashMap();
	

	@Override
	public void loadConfig() {
		this.warCraftMap = loadWarCraftImpl().stream()
				.collect(Collectors.toMap(WarCraftTemplateImpl::getId, Function.identity()));
		this.warCraftMap.values().stream().forEach(e -> e.init());
		warCraftMaxLv = warCraftMap.keySet().stream().max(Comparator.comparing(Function.identity())).get();
		for(int i=1; i<=warCraftMaxLv; i++) {
			WarCraftTemplateImpl tempWarCraft = warCraftMap.get(i);
			int type = tempWarCraft.getAttri_type();
			for(TankType tankType:TankType.values()){
				TankAttr tempAttr = tempWarCraft.getAttr();
				TankAttr beforeAttr = tankAttrTable.get(tankType.getType(), i-1);
				TankAttr nowAttr = new TankAttr();
				nowAttr.addAttr(beforeAttr);
				if(tankType.getType()==type || type==0) {
					nowAttr.addAttr(tempAttr);
				}
				tankAttrTable.put(tankType.getType(), i, nowAttr);
			}
		}
		
		
		this.warCraftSkillUpMap = loadWarCraftSkillUp().stream()
				.collect(Collectors.toMap(WarCraftSkillUpgradeTemplateImpl::getId, Function.identity()));
		this.warCraftSkillUpMap.values().stream().forEach(e -> {
			warCraftSkillTable.put(e.getSkill_id(), e.getLevel(), e);
			e.init();
		});
		
		this.warCraftSkillMap = loadWarCraftSkill().stream()
				.collect(Collectors.toMap(WarCraftSkillTemplate::getBook, Function.identity()));
	}
	
	public TankAttr getTankAttrByLv(int type, int lv) {
		return tankAttrTable.get(type, lv);
	}
	
	public WarCraftSkillTemplate getWarCraftSkill(int id) {
		return warCraftSkillMap.get(id);
	}
	
	public WarCraftTemplateImpl getWarCraftByLv(int lv) {
		return warCraftMap.get(lv);
	}

	public WarCraftSkillUpgradeTemplateImpl getWarCraftSkillUp(int skillId, int lv) {
		return warCraftSkillTable.get(skillId, lv);
	}
	@Override
	public List<String> getDownloadFile() {
		return getConfigName(WarCraftTemplateImpl.class,WarCraftSkillUpgradeTemplateImpl.class,WarCraftSkillTemplate.class);
	}

	private List<WarCraftTemplateImpl> loadWarCraftImpl() {
		return JSONUtil.fromJson(getJson(WarCraftTemplateImpl.class), new TypeReference<ArrayList<WarCraftTemplateImpl>>(){});
	}
	private List<WarCraftSkillUpgradeTemplateImpl> loadWarCraftSkillUp() {
		return JSONUtil.fromJson(getJson(WarCraftSkillUpgradeTemplateImpl.class), new TypeReference<ArrayList<WarCraftSkillUpgradeTemplateImpl>>(){});
	}
	private List<WarCraftSkillTemplate> loadWarCraftSkill() {
		return JSONUtil.fromJson(getJson(WarCraftSkillTemplate.class), new TypeReference<ArrayList<WarCraftSkillTemplate>>(){});
	}

	public List<Items> getLvUpCost(int lv, int endLv) {
		List<Items> result = Lists.newArrayList();
		for(int i=lv; i<=endLv; i++) {
			result.addAll(warCraftMap.get(i).getCosts());
		}
		return result;
	}

	public List<Items> getSkillLvUpCost(int nowSkillLv, int type,  int endLv) {
		List<Items> result = Lists.newArrayList();
		Map<Integer, WarCraftSkillUpgradeTemplateImpl> skillMap = warCraftSkillTable.row(type);
		for(int i=nowSkillLv+1; i<=endLv; i++) {
			result.addAll(skillMap.get(i).getCosts());
		}
		return result;
	}
}


