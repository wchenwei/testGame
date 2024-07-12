package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.config.excel.temlate.ActiveRichmanMapTemplate;
import com.hm.libcore.annotation.FileConfig;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;
import java.util.stream.Collectors;

@FileConfig("active_richman_map")
public class ActiveRichManTemplate extends ActiveRichmanMapTemplate{
	private List<Items> rewards = Lists.newArrayList();
    private List<Items> transportRewards = Lists.newArrayList();
	public void init(){
        this.rewards = ItemUtils.str2DefaultItemImmutableList(this.getReward());
        this.transportRewards = ItemUtils.str2DefaultItemImmutableList(this.getReward2());
	}
	
	public List<Items> getRewards(){
		return this.rewards.stream().map(t ->t.clone()).collect(Collectors.toList());
	}

	public boolean inLv(int serverLv) {
		return getServer_lv_down() <= serverLv && serverLv <= getServer_lv_up();
	}

    public List<Items> getTransportRewards() {
        return transportRewards;
    }

}
