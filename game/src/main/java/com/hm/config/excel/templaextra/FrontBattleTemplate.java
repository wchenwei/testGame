package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.util.string.StringUtil;
import com.hm.config.excel.temlate.MissionFrontBattleTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import com.hm.war.sg.troop.TankArmy;

import java.util.List;

@FileConfig("mission_front_battle")
public class FrontBattleTemplate extends MissionFrontBattleTemplate{
	private List<Integer> npcIdList;
	private List<Integer> npcIndexList;
	private List<Items> rewards = Lists.newArrayList();
	
	public void init() {
		this.npcIdList = StringUtil.splitStr2IntegerList(getEnemy_config(), ",");
		this.npcIndexList = StringUtil.splitStr2IntegerList(getEnemy_pos(), ",");
		this.rewards = ItemUtils.str2ItemList(this.getReward(), ",", ":");
	}
	public List<TankArmy> createNpcArmy() {
		List<TankArmy> armyList = Lists.newArrayList();
		for (int i = 0; i < npcIdList.size(); i++) {
			TankArmy tankArmy = new TankArmy(npcIndexList.get(i), npcIdList.get(i));
			armyList.add(tankArmy);
		}
		return armyList;
	}
	
	public List<Items> getRewards(){
		return rewards;
	}
	public boolean isFit(int type,int playerLv) {
		return this.getType()==type&&this.getLevel()==playerLv;
	}
}
