package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.RankRewardTemplate;
import com.hm.enums.ItemType;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import lombok.Getter;

import java.util.List;

@Getter
@FileConfig("rank_reward")
public class RankTemplate extends RankRewardTemplate{
	private List<Items> rewardList;
	private Items titleItem;
	private Items headItem;
	
	public void init() {
		this.rewardList = ItemUtils.str2ItemList(getReward(), ",", ":");
		this.titleItem = this.rewardList.stream().filter(e -> e.getItemType() == ItemType.TITLE.getType())
				.findFirst().orElse(null);
		this.headItem = this.rewardList.stream().filter(e -> e.getItemType() == ItemType.ICON.getType())
				.findFirst().orElse(null);
		if(this.titleItem != null) {
			this.rewardList.remove(this.titleItem);
		}
		if(this.headItem != null) {
			this.rewardList.remove(this.headItem);
		}
	}

	public List<Items> getRewardList() {
		return rewardList;
	}
	
	public boolean isFit(int rank) {
		return rank >= getRank_down() && rank <= getRank_up();
	}
}
