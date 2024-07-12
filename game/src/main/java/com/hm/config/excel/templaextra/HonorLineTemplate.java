package com.hm.config.excel.templaextra;

import com.hm.config.excel.temlate.HonerLineTemplate;
import com.hm.libcore.annotation.ConfigInit;
import com.hm.libcore.annotation.FileConfig;
import com.hm.model.item.Items;
import com.hm.model.player.BasePlayer;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("honer_line")
public class HonorLineTemplate extends HonerLineTemplate{
	private List<Items> rewardList;

	@ConfigInit
	public void init() {
		this.rewardList = ItemUtils.str2ItemList(getReward(), ",", ":");
	}

	public List<Items> getRewardList() {
		return rewardList;
	}
	
	public boolean isFit(BasePlayer player) {
		if(player.playerHonor().containRewardIndex(getId())) {
			return false;
		}
		int loginFbId = player.playerHonor().getHonorLv();
		long totalHonor = player.playerHonor().getTotalHonor();
		return totalHonor >= getHoner() && loginFbId >= getMission_id_min() && loginFbId <= getMission_id_max();
	}

	public boolean isFit(int lv,long totalHonor) {
		return totalHonor >= getHoner() && lv >= getMission_id_min() && lv <= getMission_id_max();
	}
}
