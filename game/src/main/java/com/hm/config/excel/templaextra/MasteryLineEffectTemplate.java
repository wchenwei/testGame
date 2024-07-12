package com.hm.config.excel.templaextra;

import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.MasteryAuraTemplate;
import com.hm.enums.TankAttrType;

import java.util.Map;

@FileConfig("mastery_aura")
public class MasteryLineEffectTemplate extends MasteryAuraTemplate{
	private Map<TankAttrType, Double> attrMap = Maps.newHashMap(); 
	public void init(){
		for(String str:getAttribute().split(",")){
			String[] strs = str.split(":");
			TankAttrType  attrType = TankAttrType.getType(Integer.parseInt(strs[0]));
			double value = Double.parseDouble(strs[1]);
			if(attrType!=null&&value>0){
				attrMap.put(attrType, value);
			}
		}
	}

	//是否匹配
	public boolean isFit(int type, int id, int lv){
		return getTank_type() == type && getAura_type() == id && getLevel() == lv;
	}

	public Map<TankAttrType, Double> getAttrMap() {
		return attrMap;
	}
	
}
