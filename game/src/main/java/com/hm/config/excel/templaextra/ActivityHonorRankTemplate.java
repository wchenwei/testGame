package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveNewplayerMatchTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("active_newplayer_match")
public class ActivityHonorRankTemplate extends ActiveNewplayerMatchTemplate{
	private List<Items> rewardList;
	
	public void init() {
		this.rewardList = ItemUtils.str2ItemList(getReward(), ",", ":");
	}

	public List<Items> getRewardList() {
		return rewardList;
	}
	
	public boolean isFit(int rank) {
		return rank >= getRank_down() && rank <= getRank_up();
	}
}
