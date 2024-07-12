package com.hm.config.excel.templaextra;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.hm.config.excel.temlate.MissionSpecailTemplate;
import com.hm.enums.TankLimitType;
import com.hm.libcore.annotation.FileConfig;
import com.hm.model.item.Items;
import com.hm.model.tankLimitAdapter.TankLimitAdapterGroup;
import com.hm.model.tankLimitAdapter.TankLimitFactory;
import com.hm.model.weight.Drop;
import com.hm.util.ItemUtils;
import com.hm.util.StringUtil;
import lombok.Getter;

import java.util.List;
@Getter
@FileConfig("mission_specail")
public class MissionSpecailExtraTemplate extends MissionSpecailTemplate {
	//首通奖励
	private List<Items> firstRewards = Lists.newArrayList();
	//普通奖励
	private List<Drop> drops = Lists.newArrayList();
	//翻牌掉落
	private List<Drop> flops = Lists.newArrayList();
	// 每日奖励
	private List<Items> dayRewards = Lists.newArrayList();
	
	//精英掉落
	private List<Drop> specailDrops = Lists.newArrayList();
	
	private TankLimitAdapterGroup tankLimitGroup = new TankLimitAdapterGroup();
	private String prohibitCamp;//禁止阵营
	private String prohibitArms;//禁止兵种

	private List<Integer> enemyIdList = Lists.newArrayList();

	private List<Items> recordRewardList = Lists.newArrayList();
	private List<Items> fightRewards = Lists.newArrayList();
	private List<Items> sweepRewards = Lists.newArrayList();

	public void init(){
		this.firstRewards = ItemUtils.str2DefaultItemList(getFirst_reward());
		if(StrUtil.isNotEmpty(this.getTank_limit())) {
			tankLimitGroup.add(TankLimitFactory.createProhibitAdapter(TankLimitType.TankCampProhibit,this.getTank_limit().split(";")[0]));
			tankLimitGroup.add(TankLimitFactory.createProhibitAdapter(TankLimitType.TankTypeProhibit,this.getTank_limit().split(";")[1]));
			this.prohibitCamp = this.getTank_limit().split(";")[0];
			this.prohibitArms = this.getTank_limit().split(";")[1];
		}
		this.recordRewardList = ItemUtils.str2DefaultItemList(getRecord_reward());
		this.dayRewards = ItemUtils.str2DefaultItemList(getDay_reward());
		this.enemyIdList = StringUtil.splitStr2IntegerList(getEnemy_config(),",");
		this.fightRewards = ItemUtils.str2DefaultItemList(getReward());
		this.sweepRewards = ItemUtils.str2DefaultItemList(getSweep_reward());
	}



	public List<Items> getRewards(){
		List<Items> dropItems = Lists.newArrayList();
		for(Drop drop:drops){
			Items item = drop.randomItem();
			if(item != null) {
				dropItems.add(item);
			}
		}
		return dropItems;
	}
	
	public List<Items> getAdvanceRewards(){
		List<Items> dropItems = Lists.newArrayList();
		for(Drop drop:specailDrops){
			Items item = drop.randomItem();
			if(item != null) {
				dropItems.add(item);
			}
		}
		return dropItems;
	}

	// 单个部队的配置
	public Integer getNpcId(){
		return enemyIdList.get(0);
	}
	

}
