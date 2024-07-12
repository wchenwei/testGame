package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.MissionPeakLevelTemplate;
import com.hm.enums.TankLimitType;
import com.hm.model.item.Items;
import com.hm.model.tankLimitAdapter.TankLimitAdapterGroup;
import com.hm.model.tankLimitAdapter.TankLimitFactory;
import com.hm.model.weight.RandomRatio;
import com.hm.util.ItemUtils;

import java.util.List;
@FileConfig("mission_peak_level")
public class PeakBattleLevelTemplate extends MissionPeakLevelTemplate {
	private List<Items> rewards = Lists.newArrayList();
	private List<Items> sweepRewards = Lists.newArrayList();
	private RandomRatio npcRatio;
	private TankLimitAdapterGroup tankLimitGroup = new TankLimitAdapterGroup();
	public void init(){
		this.rewards = ItemUtils.str2ItemList(this.getFirst_reward(), ",", ":");
		this.sweepRewards = ItemUtils.str2ItemList(this.getReward(), ",", ":");
		this.npcRatio = new RandomRatio(this.getLibrary());
		tankLimitGroup.add(TankLimitFactory.createProhibitAdapter(TankLimitType.TankRareProhibit, this.getRare()));
	}
	public List<Items> getRewards() {
		return rewards;
	}
	public List<Items> getSweepRewards() {
		return sweepRewards;
	}
	public RandomRatio getNpcRatio() {
		return npcRatio;
	}
	
	public int randomNpc(){
		return npcRatio.randomRatio();
	}
	public TankLimitAdapterGroup getTankLimitGroup() {
		return tankLimitGroup;
	}
}
