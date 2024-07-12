package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.PlayerMedalTemplate;
import com.hm.enums.TankAttrType;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import com.hm.util.StringUtil;

import java.util.List;
import java.util.Map;

@FileConfig("player_medal")
public class PlayerMedalExtraTemplate extends PlayerMedalTemplate{
	private List<Items> costList = Lists.newArrayList();
	private List<Items> dayRewardList = Lists.newArrayList();
	private Map<TankAttrType,Double> attrMap = Maps.newHashMap();
	private int oneSubLvValue;
	private int medalValue;
	
	public void init() {
		this.costList = ItemUtils.str2DefaultItemList(getCost());
		this.dayRewardList = ItemUtils.str2DefaultItemList(getReward());
		for (String temp : getAttri().split(",")) {
			double[] attr =StringUtil.strToDoubleArray(temp, ":");
			attrMap.put(TankAttrType.getType(new Double(attr[0]).intValue()), attr[1]);
		}
		this.oneSubLvValue = getValue()/getSub_level();
	}
	
	public void loadMedalValue(Map<Integer,PlayerMedalExtraTemplate> medalMap) {
		this.medalValue = medalMap.values().stream().filter(e -> e.getId() < getId())
				.mapToInt(e -> e.getValue()).sum();
	}

	public List<Items> getCostList() {
		return costList;
	}

	public List<Items> getDayRewardList() {
		return dayRewardList;
	}

	public Map<TankAttrType, Double> getAttrMap() {
		return attrMap;
	}

	public int getNextId() {
		return getMedal_id();
	}

	public int getOneSubLvValue() {
		return oneSubLvValue;
	}
	/**
	 * 获取勋章价值
	 * @return
	 */
	public int getMedalValue() {
		return medalValue;
	}
	
}
