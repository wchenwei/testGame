package com.hm.config.excel.templaextra;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.MissionMileFightTemplate;
import com.hm.enums.TankLimitType;
import com.hm.model.item.Items;
import com.hm.model.tankLimitAdapter.TankLimitAdapterGroup;
import com.hm.model.tankLimitAdapter.TankLimitFactory;
import com.hm.util.ItemUtils;
import lombok.Getter;

import java.util.List;

@FileConfig("mission_mile_fight")
public class FarWinMissiontTemplate extends MissionMileFightTemplate{
	private List<Items> rewards = Lists.newArrayList();
	private List<Items> fightRewards = Lists.newArrayList();
	private String prohibitCamp;//禁止阵营
	private String prohibitArms;//禁止兵种
	private TankLimitAdapterGroup tankLimitGroup = new TankLimitAdapterGroup();
	@Getter
	private List<Items> recordRewardList = Lists.newArrayList();

	public void init(){
		this.rewards = ItemUtils.str2ItemList(this.getReward(), ",", ":");
		this.fightRewards = ItemUtils.str2ItemList(this.getFirst_reward(), ",", ":");
		if(StrUtil.isNotEmpty(this.getTank_limit())) {
			tankLimitGroup.add(TankLimitFactory.createProhibitAdapter(TankLimitType.TankCampProhibit,this.getTank_limit().split(";")[0]));
			tankLimitGroup.add(TankLimitFactory.createProhibitAdapter(TankLimitType.TankTypeProhibit,this.getTank_limit().split(";")[1]));
			this.prohibitCamp = this.getTank_limit().split(";")[0];
			this.prohibitArms = this.getTank_limit().split(";")[1];
		}
		this.recordRewardList = ItemUtils.str2DefaultItemList(getRecord_reward());
	}

	public List<Items> getRewards() {
		return rewards;
	}

	public String getProhibitCamp() {
		return prohibitCamp;
	}

	public String getProhibitArms() {
		return prohibitArms;
	}

	public List<Items> getFightRewards() {
		return fightRewards;
	}

	public TankLimitAdapterGroup getTankLimitGroup() {
		return tankLimitGroup;
	}
	
	
	
}
