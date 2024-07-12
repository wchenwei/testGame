package com.hm.config.excel.templaextra;

import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.MasteryEffectTemplate;
import com.hm.enums.TankAttrType;

import java.util.Map;
@FileConfig("mastery_effect")
public class MasteryEffectExtraTemplate extends MasteryEffectTemplate{
	private Map<TankAttrType, Double> attrMap = Maps.newHashMap();
	
	public void init(){
		for(String str:getEffect().split(",")){
			String[] strs = str.split(":");
			TankAttrType  attrType = TankAttrType.getType(Integer.parseInt(strs[0]));
			double value = Double.parseDouble(strs[1]);
			if(attrType!=null&&value>0){
				attrMap.put(attrType, value);
			}
		}
	}
	public Map<TankAttrType, Double> getAttrMap(){
		return attrMap;
	}
}
