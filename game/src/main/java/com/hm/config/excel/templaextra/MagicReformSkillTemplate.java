package com.hm.config.excel.templaextra;

import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.TankMagicReformSkillTemplate;
import com.hm.enums.TankAttrType;
import com.hm.util.TankAttrUtils;

import java.util.Map;
@FileConfig("tank_magic_reform_skill")
public class MagicReformSkillTemplate extends TankMagicReformSkillTemplate {
	private Map<TankAttrType,Double> attrMap = Maps.newConcurrentMap();
	public void init(){
		this.attrMap = TankAttrUtils.str2TankAttrMap(getAttri(), ",", ":");
	}
	
	public Map<TankAttrType,Double> getAttrMap(int lv){
		Map<TankAttrType,Double> map = Maps.newConcurrentMap();
		this.attrMap.forEach((key,value)->map.put(key, value+(lv-1)*this.getUpgrade()));
		return map;
	}
}
