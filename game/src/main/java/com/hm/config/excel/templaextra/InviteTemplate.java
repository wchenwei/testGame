package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.InviteConfigTemplate;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.redis.InviteInfoData;
import com.hm.redis.util.RedisUtil;
import com.hm.util.ItemUtils;

import java.util.List;
@FileConfig("invite_config")
public class InviteTemplate extends InviteConfigTemplate {
	private List<Items> rewards = Lists.newArrayList();
	public void init(){
		this.rewards = ItemUtils.str2ItemList(this.getReward(), ",", ":");
	}
	public List<Items> getRewards() {
		return rewards;
	}
	public boolean finish(Player player) {
		InviteInfoData data = RedisUtil.getInviteInfoData(player.getId());
		if(data==null){
			return false;
		}
		switch(this.getType()){
		case 1://邀请多少人(成就)
			int needNum = Integer.parseInt(this.getCondiion());
			return data.getInviteNum(player, getTask_type())>=needNum;
		case 2://累计充值
			long rechargeGold = Integer.parseInt(this.getCondiion());
			return data.getRecharge()>=rechargeGold;
		case 3://达到多少级以上多少人
			String[] str = this.getCondiion().split(":");
			int needLv = Integer.parseInt(str[0]);
			int num = Integer.parseInt(str[1]);
			return data.getLvNum(player, needLv, getTask_type())>=num;
		case 4://收集N量S以上坦克的人数
			String[] finish = this.getCondiion().split(":");
			return data.getLvNum(player, Integer.parseInt(finish[0]), getTask_type())>=Integer.parseInt(finish[1]);
		}
		return false;
	}
	
}
