
package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableList;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.WorldUpgradeTemplate;

import java.util.ArrayList;
import java.util.List;

@Config
public class WorldLvConfig extends ExcleConfig {
	private WorldUpgradeTemplate[] lvWorlds = new WorldUpgradeTemplate[100];
	private int maxLv;
	@Override
	public void loadConfig() {
		loadWorldUpgradeTemplate();
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(WorldUpgradeTemplate.class);
	}
	
	private void loadWorldUpgradeTemplate(){
		List<WorldUpgradeTemplate> list = ImmutableList.copyOf(JSONUtil.fromJson(getJson(WorldUpgradeTemplate.class), new TypeReference<ArrayList<WorldUpgradeTemplate>>(){}));
		WorldUpgradeTemplate[] lvWorlds = new WorldUpgradeTemplate[list.size()+1];
		for(WorldUpgradeTemplate temp:list){
			lvWorlds[temp.getId()] = temp;
		}
		this.lvWorlds = lvWorlds;
		this.maxLv = list.stream().mapToInt(e -> e.getId()).max().orElse(15);
	}
	public WorldUpgradeTemplate getWorldUpgradeTemplate(int lv) {
		if(lv > getMaxLv()) {
			return null;
		}
		return this.lvWorlds[lv];
	}
	
	public int calCurWorldLv(int serverLv) {
		for (int i = this.lvWorlds.length-1; i > 0; i--) {
			if(this.lvWorlds[i].getServer_lv() <= serverLv) {
				return this.lvWorlds[i].getId();
			}
		}
		return 0;
	}

	public int getMaxLv() {
		return maxLv;
	}
}






