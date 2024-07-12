package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.config.excel.temlate.Active6yearLoginTemplate;
import com.hm.libcore.annotation.FileConfig;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;
import java.util.stream.Collectors;

@FileConfig("active_6year_login")
public class Active6yearLoginTemplateImpl extends Active6yearLoginTemplate {
	//奖励
	private List<Items> reward1 = Lists.newArrayList();
	//奖励
	private List<Items> reward2 = Lists.newArrayList();
	//奖励
	private List<Items> reward3 = Lists.newArrayList();
	private List<Items> reward4 = Lists.newArrayList();

	public void init() {
		this.reward1 = ItemUtils.str2ItemList(this.getReward_sign(), ",", ":");
		this.reward2 = ItemUtils.str2ItemList(this.getReward_recharge(), ",", ":");
		this.reward3 = ItemUtils.str2ItemList(this.getReward_recharge_2(), ",", ":");
		this.reward4 = ItemUtils.str2ItemList(this.getReward_recharge_3(), ",", ":");
	}
	/**
	 * @Title: getReward   
	 * @Description: 获取奖励
	 * @param: @param type 1、登录奖励；2阶段奖励(1,一阶奖励，2二阶奖励)
	 * @param: @return      
	 * @return: List<Items>      
	 * @throws
	 */
	public List<Items> getRechargeRewards(int type, int receiveReward) {
		if(type==2) {
			if(receiveReward==0) {
				return reward2.stream().map(t -> t.clone()).collect(Collectors.toList());
			}else if(receiveReward==1) {
				return reward3.stream().map(t -> t.clone()).collect(Collectors.toList());
			}else {
				return reward4.stream().map(t -> t.clone()).collect(Collectors.toList());
			}
		}
		return reward1.stream().map(t -> t.clone()).collect(Collectors.toList());
	}

	//校验是否符合等级区间
	public boolean checkLv(int lv) {
		return lv >= this.getLv_down() && lv <= this.getLv_up();
	}
}


