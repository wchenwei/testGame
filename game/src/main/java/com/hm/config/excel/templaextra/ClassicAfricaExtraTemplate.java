package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ClassicAfricaTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;
import java.util.stream.Collectors;

@FileConfig("classic_africa")
public class ClassicAfricaExtraTemplate extends ClassicAfricaTemplate implements IClassicBattleTemplate{
	private List<Items> rewards = Lists.newArrayList();
	public void init(){
		this.rewards = ItemUtils.str2ItemList(this.getReward(), ",", ":");
	}
	@Override
	public List<Items> getRewards() {
		return rewards.stream().map(t->t.clone()).collect(Collectors.toList());
	}
	@Override
	public int getTankExp() {
		return 0;
	}
	@Override
	public int getPlayerExp() {
		return this.getPlayer_exp();
	}
	@Override
	public int getStartWave() {
		return 101;
	}
	@Override
	public int getMinCombat() {
		return this.getBottom_power();
	}
	@Override
	public List<Items> getCheckRewards() {
		return getRewards();
	}
	@Override
	public List<Items> checkRewards(List<Items> rewards) {
		return getRewards();
	}
	
	
}
