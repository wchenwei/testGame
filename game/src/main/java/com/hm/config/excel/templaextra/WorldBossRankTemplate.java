package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.WorldBossRankRewardTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("world_boss_rank_reward")
public class WorldBossRankTemplate extends WorldBossRankRewardTemplate{
	private List<Items> rewardList;
	
	public void init() {
		this.rewardList = ItemUtils.str2ItemList(getReward(), ",", ":");
	}

	public List<Items> getRewardList() {
		return rewardList;
	}
	
	public boolean isFit(int lv,int rank) {
		return lv>=getBoss_id_down() && lv<=getBoss_id_up()&&rank >= getRank_down() && rank <= getRank_up();
	}

}
