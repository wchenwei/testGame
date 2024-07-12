package com.hm.config.excel.templaextra;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.PlayerArmSuitTemplate;
import com.hm.enums.TankAttrType;

import java.util.Map;

@FileConfig("player_arm_suit")
public class PlayerArmSuitExtraTemplate extends PlayerArmSuitTemplate{
	private Map<TankAttrType, Double> attrMap = Maps.newHashMap();
	
	public void init(){
		if(StrUtil.isBlank(getAttr())){
			return;
		}
		for(String str:getAttr().split(",")){
			String[] strs = str.split(":");
			TankAttrType  attrType = TankAttrType.getType(Integer.parseInt(strs[0]));
			double value = Double.parseDouble(strs[1]);
			if(attrType!=null&&value>0){
				attrMap.put(attrType, value);
			}
		}
	}

	public Map<TankAttrType, Double> getAttrMap() {
		return attrMap;
	}
	
}
