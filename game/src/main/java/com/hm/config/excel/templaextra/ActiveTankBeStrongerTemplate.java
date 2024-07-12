package com.hm.config.excel.templaextra;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.config.excel.temlate.ActiveTankGrowTemplate;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.util.string.StringUtil;
import com.hm.model.item.Items;
import com.hm.model.player.BasePlayer;
import com.hm.model.tank.Tank;
import com.hm.util.ItemUtils;

import java.util.List;
import java.util.stream.Collectors;

@FileConfig("active_tank_grow")
public class ActiveTankBeStrongerTemplate extends ActiveTankGrowTemplate{
	private List<Items> rewards = Lists.newArrayList();
	private List<String> finishs = Lists.newArrayList();
	private List<Integer> tankIds = Lists.newArrayList();

	public void init(){
		this.rewards = ItemUtils.str2ItemList(this.getReward(), ",", ":");
		this.finishs = StringUtil.splitStr2StrList(this.getFinish(), ",");
		this.tankIds = StringUtil.splitStr2IntegerList(this.getTank_id(), ",");
	}
	/**
	 * 配置1：等级 2：技能等级 3：改造等级
	 * @param player
	 * @return
	 */
	public boolean isFinish(BasePlayer player, List<Integer> tankIds) {
		if(CollUtil.isEmpty(tankIds)){
			return false;
		}
		List<Tank> tanks = tankIds.stream().map(t ->player.playerTank().getTank(t)).collect(Collectors.toList());
		for(String s:finishs){
			if(!getFinish(tanks,s)){
				return false;
			}
		}
		return true;
	}

	public boolean getFinish(List<Tank> tanks,String finsh){
		int type = Integer.parseInt(finsh.split(":")[0]);
		int num = Integer.parseInt(finsh.split(":")[1]);
		switch(type){
			case 1:
				return tanks.stream().allMatch(t ->t.getLv()>=num);
			case 2:
				//每个坦克都有技能等级达到该等级
				return tanks.stream().allMatch(t ->{
					return t.getSkillMap().values().stream().anyMatch(lv ->lv>=num);
				});
			case 3:
				return tanks.stream().allMatch(t ->t.getReLv()>=num);
		}
		return false;
	}
	public List<Items> getRewards() {
		return rewards;
	}
}
