package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.WorldBossTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("world_boss")
public class WorldBossExtraTemplate extends WorldBossTemplate{
	private List<Items> killRewards = Lists.newArrayList();
	public void init(){
		this.killRewards = ItemUtils.str2ItemList(this.getKill_reward(), ",", ":");
	}
	public List<Items> getKillRewards() {
		return killRewards;
	}
	
}
