package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ClassicMaginotTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;
import java.util.stream.Collectors;

@FileConfig("classic_maginot")
public class ClassicMaginotExtraTemplate extends ClassicMaginotTemplate implements IClassicBattleTemplate{
	private List<Items> rewards =Lists.newArrayList();
	private List<Items> checkRewards = Lists.newArrayList();
	public void init(){
		this.rewards = ItemUtils.str2ItemList(this.getReward(), ",", ":");
		this.checkRewards = ItemUtils.str2ItemList(this.getReward_check(), ",", ":");
	}
	@Override
	public List<Items> getRewards() {
		return rewards.stream().map(t->t.clone()).collect(Collectors.toList());
	}
	@Override
	public int getTankExp() {
		return this.getTank_exp();
	}
	@Override
	public int getPlayerExp() {
		return this.getPlayer_exp();
	}
	@Override
	public int getStartWave() {
		return this.getStart_wave();
	}
	@Override
	public int getMinCombat() {
		return this.getBottom_power();
	}
	@Override
	public List<Items> getCheckRewards() {
		return checkRewards.stream().map(t->t.clone()).collect(Collectors.toList());
	}
	@Override
	public List<Items> checkRewards(List<Items> upRewards) {
		boolean flag = false;
		for(Items item:upRewards){
			//如果有奖励超过了checkRewards的配置则认定玩家作弊
			if(this.checkRewards.stream().filter(t -> t.getId()==item.getId()&&t.getItemType()==item.getItemType()&&t.getCount()<item.getCount()).findAny().orElse(null)!=null){
				flag = true;
			}
		}
		//如果校验没有通过则发放基础奖励
		if(flag){
			return getRewards();
		}
		return upRewards;
	}
}
