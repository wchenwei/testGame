package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.util.lv.ILvValue;
import com.hm.config.excel.temlate.WorldProjectLevelTemplate;
import com.hm.enums.WorldBuildAddType;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import com.hm.util.StringUtil;

import java.util.List;
import java.util.Map;

@FileConfig("world_project_level")
public class WorldBuildLevelTemplate extends WorldProjectLevelTemplate implements ILvValue{
	private List<Items> dayItems = Lists.newArrayList();
	private Map<Integer,List<Items>> campItems = Maps.newHashMap();
	private Map<Integer,Double> kfAttrMap = Maps.newHashMap();
	private Map<WorldBuildAddType,Double> typeAddMap = Maps.newConcurrentMap();
	
	private List<String> effectList = Lists.newArrayList();
	
	public void init() {
		this.dayItems = ItemUtils.str2DefaultItemList(getReward());
		this.effectList = StringUtil.splitStr2StrList(getEffect(), ",");
	}
	
	public void loadAddType() {
		for (String addStr : effectList) {
			WorldBuildAddType type = WorldBuildAddType.getWorldBuildAddType(Integer.parseInt(addStr.split(":")[0]));
			if(type == null) {
				System.err.println("世界建筑效果不存在!"+addStr);
				continue;
			}
			if(type == WorldBuildAddType.KFAttrAdd) {
				addAttrRate(addStr);//加属性
			}else {
				addAddType(type, addStr);
			}
		}
	}
	
	public void addAddType(WorldBuildAddType type,String addStr) {
		this.typeAddMap.put(type, Double.parseDouble(addStr.split(":")[1])+getTypeAddValue(type));
	}
	
	public void addAttrRate(String addStr) {
		int type = Integer.parseInt(addStr.split(":")[1]);
		double value = Double.parseDouble(addStr.split(":")[2]);
		this.kfAttrMap.put(type, value+this.kfAttrMap.getOrDefault(type, 0d));
	}

	public List<Items> getDayItems() {
		return dayItems;
	}
	
	public List<Items> getTaskItems(int campId) {
		return this.campItems.get(campId);
	}
	
	public double getTypeAddValue(WorldBuildAddType type) {
		return this.typeAddMap.getOrDefault(type, 0D);
	}

	public Map<Integer, Double> getKfAttrMap() {
		return kfAttrMap;
	}
	

	public List<String> getEffectList() {
		return effectList;
	}

	public void setEffectList(List<String> effectList) {
		this.effectList = Lists.newArrayList(effectList);
	}
	

	@Override
	public int getLv() {
		return getId();
	}

	@Override
	public long getExp() {
		return getCost();
	}
}
