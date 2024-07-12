package com.hm.config.excel.templaextra;

import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.GuildFactoryWeaponUpgradeTemplate;
import com.hm.enums.TankAttrType;
import com.hm.util.TankAttrUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

@FileConfig("guild_factory_weapon_upgrade")
public class GuildFactoryWeaponUpgradeExtraTemplate extends GuildFactoryWeaponUpgradeTemplate {
	private Map<TankAttrType,Double> attrMap = Maps.newConcurrentMap();
	public void init(){
		if(StringUtils.isNotBlank(getAttri())){
			this.attrMap = TankAttrUtils.str2TankAttrMap(getAttri(), ",", ":");
		}
	}
	public Map<TankAttrType, Double> getAttrMap() {
		return attrMap;
	}
	
	
}
