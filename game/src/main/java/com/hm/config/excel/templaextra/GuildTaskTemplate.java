package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.GuildEmployTaskTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("guild_employ_task")
public class GuildTaskTemplate extends GuildEmployTaskTemplate{
	private List<Items> rewardList;
	
	public void init() {
		this.rewardList = ItemUtils.str2ItemList(getTask_reward(), ",", ":");
	}

	public List<Items> getRewardList() {
		return rewardList;
	}
}
