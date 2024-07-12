package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.config.excel.temlate.Active0801SignTemplate;
import com.hm.libcore.annotation.FileConfig;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;
import java.util.stream.Collectors;
@FileConfig("active_0801_sign")
public class Active0801SignTemplateImpl extends Active0801SignTemplate {
	private List<Items> signRewards = Lists.newArrayList();
	private List<Items> rechargeRewards = Lists.newArrayList();
	//奖励
	private List<Items> reward2 = Lists.newArrayList();
	//奖励
	private List<Items> reward3 = Lists.newArrayList();
	private List<Items> reward4 = Lists.newArrayList();
	
	public void init(){
		this.signRewards = ItemUtils.str2ItemList(this.getReward_sign(), ",", ":");
		this.reward2 = ItemUtils.str2ItemList(this.getReward_recharge(), ",", ":");
		this.reward3 = ItemUtils.str2ItemList(this.getReward_recharge_2(), ",", ":");
		this.reward4 = ItemUtils.str2ItemList(this.getReward_recharge_3(), ",", ":");
	}

	public List<Items> getSignRewards() {
		return signRewards;
	}

	public List<Items> getRechargeRewards() {
		return rechargeRewards;
	}

	public boolean isFit(int lv, int days) {
		return lv>=this.getLv_down()&&lv<=this.getLv_up()&&days>=this.getDay();
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
		return signRewards.stream().map(t -> t.clone()).collect(Collectors.toList());
	}

}
