package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.config.excel.temlate.ActiveGrowProjectTemplate;
import com.hm.libcore.annotation.ConfigInit;
import com.hm.libcore.annotation.FileConfig;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import lombok.Getter;

import java.util.List;

@Getter
@FileConfig("active_grow_project")
public class ActivityGrowUpTemplate extends ActiveGrowProjectTemplate{
	private List<Items> rewards = Lists.newArrayList();
	private List<Items> superReward = Lists.newArrayList();

	@ConfigInit
	public void init(){
		this.rewards = ItemUtils.str2DefaultItemList(this.getReward());
		this.superReward = ItemUtils.str2DefaultItemList(this.getReward2());

	}

}
