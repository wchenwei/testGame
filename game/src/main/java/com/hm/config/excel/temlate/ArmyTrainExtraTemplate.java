package com.hm.config.excel.temlate;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("army_train")
public class ArmyTrainExtraTemplate extends ArmyTrainTemplate {
	private Items extraQueueCost;//扩建队列消耗
	private Items overTimeCost;//募兵加时消耗
	private Items materialsCost;//作坊扩建消耗
	private List<Items> tankRewards = Lists.newArrayList();//扩建坦克营队列奖励
	private List<Items> rocketRewards =Lists.newArrayList();//扩建火箭营奖励
	private List<Items> armorRewards =Lists.newArrayList();//扩建装甲营奖励
	public void init() {
		extraQueueCost = ItemUtils.str2Item(this.getCost(), ":");
		overTimeCost = ItemUtils.str2Item(this.getCost_time_add(), ":");
		materialsCost = ItemUtils.str2Item(this.getCost_materials_room(), ":");
		tankRewards = ItemUtils.str2ItemList(this.getReward_tank(), ",", ":");
		rocketRewards = ItemUtils.str2ItemList(this.getReward_rocket(), ",", ":");
		armorRewards = ItemUtils.str2ItemList(this.getReward_armor(), ",", ":");
	}
	public Items getExtraQueueCost() {
		return extraQueueCost;
	}
	public Items getOverTimeCost() {
		return overTimeCost;
	}

    public List<Items> getTankRewards() {
		return tankRewards;
	}
	public List<Items> getRocketRewards() {
		return rocketRewards;
	}
	public List<Items> getArmorRewards() {
		return armorRewards;
	}
	/**
     * 作坊扩建消耗
     * @return
     */
    public Items getMaterialsCost() {
        return materialsCost;
    }
}
