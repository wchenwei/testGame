package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active6yearCircleTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.stream.Collectors;
@FileConfig("active_6year_circle")
public class Active6yearCircleTemplateImpl extends Active6yearCircleTemplate {
	//奖励
	private List<Items> reward = Lists.newArrayList();
	public void init() {
		this.reward = ItemUtils.str2ItemList(this.getReward(), ",", ":");
	}
	
	public List<Items> getRewardList() {
		return reward.stream().map(t -> t.clone()).collect(Collectors.toList());
	}
	
	public Pair<Integer, Integer> genGroupKey() {
        return Pair.of(getServer_lv_down(), getServer_lv_up());
    }
}
