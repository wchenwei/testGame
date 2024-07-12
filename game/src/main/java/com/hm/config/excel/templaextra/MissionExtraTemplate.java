package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.config.excel.temlate.MissionTemplate;
import com.hm.libcore.annotation.FileConfig;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import com.hm.util.StringUtil;
import lombok.Getter;

import java.util.List;

@FileConfig("mission")
public class MissionExtraTemplate extends MissionTemplate {
	private Items costItem ;
	@Getter
	private List<Items> recordRewardList = Lists.newArrayList();

	private MissionWaveDrop[] waveDrops;
	private List<Items> totalItemList = Lists.newArrayList();
	@Getter
	private List<Items> missRewardList = Lists.newArrayList();
	@Getter
	private List<Items> baseItemList = Lists.newArrayList();//每小时宝箱奖励

	public void init(){
		this.costItem = ItemUtils.str2Item(this.getCost(),":");
		this.recordRewardList = ItemUtils.str2DefaultItemList(getRecord_reward());
		this.missRewardList =  ItemUtils.str2DefaultItemList(getMission_reward());
		this.baseItemList =  ItemUtils.str2DefaultItemList(getBox_reward_base());

		this.waveDrops = new MissionWaveDrop[getEnemy_wave()];

		String[] posArray = getEnemy_pos().split(";");
		for (int i = 0; i < this.waveDrops.length; i++) {
			//平均分到每个坦克位置上
			String item1 = getReward().split(";")[i];
			String item2 = getElse_reward().split(";")[i];
			this.waveDrops[i] = new MissionWaveDrop(StringUtil.splitStr2IntegerList(posArray[i],","),item1,item2);

			this.totalItemList.addAll(ItemUtils.str2DefaultItemList(item1));
			this.totalItemList.addAll(this.waveDrops[i].getElseList());
		}
		this.totalItemList = ItemUtils.mergeItemList(this.totalItemList);
	}

	public MissionWaveDrop getMissionWaveDrop(int wave) {
		return this.waveDrops[wave];
	}

	//掉落
	public List<Items> getDropItemList() {
		return getDropItemList(1);
	}
	//掉落
	public List<Items> getDropItemList(int count) {
		return ItemUtils.calItemRateReward(totalItemList,count);
	}
	public Items getCostItem() {
		return costItem.clone();
	}
	
	public boolean isBandits() {
		return getType() == 2;
	}

	//是否为主线关卡
	public boolean isMainMission() {
		return getType() == 1;
	}
}
