package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.util.string.StringUtil;
import com.hm.config.excel.temlate.EnemyArenaTemplate;
import com.hm.war.sg.troop.TankArmy;

import java.util.List;

@FileConfig("enemy_arena")
public class EnemyArenaExTemplate extends EnemyArenaTemplate{
	private List<Integer> npcIdList;
	private List<Integer> npcIndexList;
	
	public void init() {
		this.npcIdList = StringUtil.splitStr2IntegerList(getEnemy_config(), ",");
		this.npcIndexList = StringUtil.splitStr2IntegerList(getEnemy_pos(), ",");
	}
	public List<TankArmy> createNpcArmy() {
		List<TankArmy> armyList = Lists.newArrayList();
		for (int i = 0; i < npcIdList.size(); i++) {
			TankArmy tankArmy = new TankArmy(npcIndexList.get(i), npcIdList.get(i));
			armyList.add(tankArmy);
		}
		return armyList;
	}
}
