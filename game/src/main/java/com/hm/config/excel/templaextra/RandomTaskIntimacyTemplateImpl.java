package com.hm.config.excel.templaextra;

import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.RandomTaskIntimacyTemplate;

import java.util.Map;

@FileConfig("random_task_intimacy")
public class RandomTaskIntimacyTemplateImpl extends RandomTaskIntimacyTemplate{
	private Map<Integer,Double> attrMap = Maps.newConcurrentMap();
	public void init() {
		for (String temp: getAttribute_add().split(",")) {
			int key = Integer.parseInt(temp.split(":")[0]);
			double value = Double.parseDouble(temp.split(":")[1]);
			if(value > 0) {
				this.attrMap.put(key, value);
			}
		}
	}
	
	public Map<Integer, Double> getAttrMap() {
		return attrMap;
	}
	
	
}
