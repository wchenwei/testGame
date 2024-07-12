package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveLogin100gachaTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import lombok.Data;

import java.util.List;

@FileConfig("active_login_100gacha")
@Data
public class ActivityCrazeDrawTemplate extends ActiveLogin100gachaTemplate {
	private List<Items> rewards = Lists.newArrayList();

	public void init(){
		this.rewards= ItemUtils.str2DefaultItemImmutableList(this.getReward());
	}
	public List<Items> getRewards(){
		return rewards;
	}
}
