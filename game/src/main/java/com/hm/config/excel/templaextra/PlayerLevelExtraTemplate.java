package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.PlayerLevelTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;
@FileConfig("player_level")
public class PlayerLevelExtraTemplate extends PlayerLevelTemplate {
	private List<Items> levelRewards = Lists.newArrayList();//等级大礼包
	private List<Items> bigRewards = Lists.newArrayList();//十级大礼包
	public void init(){
		this.levelRewards = ItemUtils.str2ItemList(this.getLevel_reward(), ",", ":");
		this.bigRewards = ItemUtils.str2ItemList(this.getTen_reward(), ",", ":");
	}
	public List<Items> getLevelRewards() {
		return levelRewards;
	}
	public List<Items> getBigRewards() {
		return bigRewards;
	}
}
