package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.enums.TankAttrType;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.DriverBaseTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import com.hm.util.TankAttrUtils;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@FileConfig("driver_base")
public class DriverTemplate extends DriverBaseTemplate {

	private int maxEvolveLv = 0;
	private Map<Integer, Float> evolveAttrRate = Maps.newHashMap();
	private Map<Integer, Float> evolveTecRate = Maps.newHashMap();
	private Map<Integer, List<Items>> evolveCost = Maps.newHashMap();
	private Map<Integer, Map<TankAttrType, Double>> evolveAddition = Maps.newHashMap();

	public void init() {
		List<Float> attrRateList = com.hm.util.StringUtil.splitStr2FloatList(this.getEvolve_attr_rate(), ",");
		List<Float> tecRateList = com.hm.util.StringUtil.splitStr2FloatList(this.getEvolve_tec_rate(), ",");
		List<String> costList = com.hm.util.StringUtil.splitStr2StrList(this.getEvolve_cost(), ";");
		List<String> addition = com.hm.util.StringUtil.splitStr2StrList(this.getEvolve_addition(), ";");
		maxEvolveLv = attrRateList.size();
		for(int i=0; i<attrRateList.size(); i++) {
			evolveAttrRate.put(i+1, attrRateList.get(i));
			evolveTecRate.put(i+1, tecRateList.get(i));
			evolveCost.put(i+1, ItemUtils.str2ItemList(costList.get(i), ",", ":"));

			Map<TankAttrType, Double> attrMap = TankAttrUtils.str2TankAttrMap(String.valueOf(addition.get(i)), ",", ":");
			if (i > 0){
				Map<TankAttrType, Double> beforeAttrMap = evolveAddition.get(i);
				beforeAttrMap.forEach((key,value)-> attrMap.merge(key, value, (x,y)->(x+y)));
			}
			evolveAddition.put(i+1, attrMap);
		}
	}

	/**
	 * 获取重铸返还
	 * @param evolveLv
	 * @return
	 */
	public List<Items> getRecastEvolveReward(int evolveLv) {
		List<Items> rewardList = Lists.newArrayList();
		for (int i = 1; i <= evolveLv; i++) {
			rewardList.addAll(this.evolveCost.getOrDefault(i, Lists.newArrayList()));
		}
		return ItemUtils.mergeItemList(rewardList);
	}


	public float getEvolveAttrRate(int evolveLv) {
		return evolveAttrRate.getOrDefault(evolveLv, 0f);
	}
	public float getEvolveTecRate(int evolveLv) {
		return this.evolveTecRate.getOrDefault(evolveLv, 0f);
	}

	public List<Items> getEvolveCost(int evolveLv) {
		return this.evolveCost.getOrDefault(evolveLv, null);
	}

	public Map<TankAttrType, Double> getEvolveAdditionAttrMap(int evolveLv) {
		return evolveAddition.get(evolveLv);
	}
}
