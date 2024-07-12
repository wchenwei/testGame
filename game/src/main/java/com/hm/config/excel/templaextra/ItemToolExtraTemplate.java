package com.hm.config.excel.templaextra;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ItemToolTemplate;
import com.hm.enums.TankAttrType;
import com.hm.util.TankAttrUtils;

import java.util.Map;

@FileConfig("item_tool")
public class ItemToolExtraTemplate extends ItemToolTemplate{
	private Map<TankAttrType, Double> attrMap = Maps.newHashMap();
	
	public void init(){
		if(StrUtil.isNotBlank(getAttri())) {
			this.attrMap = TankAttrUtils.str2TankAttrMap(getAttri(), ",", ":");
		}
	}
	
	public Map<TankAttrType, Double> getAttrMap() {
		return attrMap;
	}
	
	public Map<TankAttrType, Double> getAttrMap(int num){
		Map<TankAttrType, Double> map = Maps.newConcurrentMap();
		attrMap.forEach((key,value)->{
			map.put(key, value*(num));
		});
		return map;
	}
}
