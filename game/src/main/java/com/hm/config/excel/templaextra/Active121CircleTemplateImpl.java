package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active1001CircleTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.stream.Collectors;

@FileConfig("active_1001_circle")
public class Active121CircleTemplateImpl extends Active1001CircleTemplate {
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
