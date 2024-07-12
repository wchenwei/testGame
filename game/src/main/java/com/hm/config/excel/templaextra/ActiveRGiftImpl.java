package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveRechargeGiftTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import com.hm.util.StringUtil;

import java.util.List;

@FileConfig("active_recharge_gift")
public class ActiveRGiftImpl extends ActiveRechargeGiftTemplate{
	private List<Items> rewardList = Lists.newArrayList();
	private int startLv = 0;
	private int endLv = 0;
	
	public void init(){
		this.rewardList = ItemUtils.str2ItemList(this.getReward(), ",", ":");
		int[] lvArr = StringUtil.strToIntArray(this.getLevel(), ",");
		startLv = lvArr[0];
		endLv = lvArr[1];
	}

	public List<Items> getRewardItems() {
		return rewardList;
	}
	public int getStartLv() {
		return startLv;
	}
	public int getEndLv() {
		return endLv;
	}
}





