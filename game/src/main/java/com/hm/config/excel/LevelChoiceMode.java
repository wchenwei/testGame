package com.hm.config.excel;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.hm.libcore.util.KeyValue;

import java.util.List;

public class LevelChoiceMode {
	//根据等级获取值
	private List<KeyValue<Integer, Double>> lvList = Lists.newArrayList();
	
	public LevelChoiceMode(String info) {
		List<KeyValue<Integer, Double>> lvList = Lists.newArrayList();
		for (String lvs : info.split(",")) {
			int lv = Integer.parseInt(lvs.split(":")[0]);
			double num = Double.parseDouble(lvs.split(":")[1]);
			lvList.add(new KeyValue<Integer, Double>(lv, num));
		}
		this.lvList = ImmutableList.copyOf(lvList);
	}
	
	public double getLvValue(int lv) {
		for (int i = this.lvList.size()-1; i >= 0; i--) {
			KeyValue<Integer, Double> keyv = this.lvList.get(i);
			if(lv >= keyv.GetKey()) {
				return keyv.GetValue();
			}
		}
		return 0;
	}
	
}
