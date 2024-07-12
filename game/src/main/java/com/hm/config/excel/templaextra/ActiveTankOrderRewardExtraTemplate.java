package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveTankOrderRewardTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("active_tank_order_reward")
public class ActiveTankOrderRewardExtraTemplate extends ActiveTankOrderRewardTemplate{
	private List<Items> rewardList = Lists.newArrayList();

	public void init() {
		this.rewardList = ItemUtils.str2ItemList(getReward_extra(), ",", ":");
	}

	public List<Items> getRewardList() {
		return rewardList;
	}

	/**
	 * @description
	 * @param curId 关卡数
	 * @param lv  用户等级
	 * @return boolean
	 * @author wyp
	 * @date 2020/11/17 15:08
	 */
	public boolean isFit(int curId,int lv){
		return lv>=this.getPlayer_lv_down() &&lv<=this.getPlayer_lv_up() && curId == this.getLevel();
	}
	
}
