package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.config.excel.temlate.ActiveThreeDayPointTemplate;
import com.hm.libcore.annotation.ConfigInit;
import com.hm.libcore.annotation.FileConfig;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import lombok.Getter;

import java.util.List;

@Getter
@FileConfig("active_three_day_point")
public class ActiveThreeDayPointTemplateImpl extends ActiveThreeDayPointTemplate {

	private List<Items> rewards = Lists.newArrayList();

	@ConfigInit
	public void init(){
		this.rewards = ItemUtils.str2DefaultItemImmutableList(getReward());
	}
}