package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.util.string.StringUtil;
import com.hm.config.excel.temlate.EnemyBaseTemplate;
import com.hm.enums.NpcConfType;
import com.hm.war.sg.troop.TankArmy;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@FileConfig("enemy_base")
public class NpcTroopTemplate extends EnemyBaseTemplate{
	private List<Integer> npcIdList;
	private List<Integer> npcIndexList;
	private NpcConfType npcConfType;
	private Map<Integer, Integer> skillMap = Maps.newHashMap();// 座驾、装备等技能

	private int[] equQuality;

	public void init(Map<Integer, CarTemplate> carMap) {
		this.npcIdList = StringUtil.splitStr2IntegerList(getEnemy_config(), ",");
		this.npcIndexList = StringUtil.splitStr2IntegerList(getEnemy_pos(), ",");
		npcConfType = NpcConfType.getNpcConfType(this.getType());
		Integer car_lv = getCar_lv();
		for(int i=1;i<=car_lv;i++){
			Integer skillId = carMap.get(i).getCar_skill();
			if (skillId > 0){
				skillMap.put(skillId, 1);
			}
		}
		this.equQuality = StringUtil.splitStr2IntArray(getEquipment(),",");
	}
	public List<TankArmy> createNpcArmy() {
		List<TankArmy> armyList = Lists.newArrayList();
		for (int i = 0; i < npcIdList.size(); i++) {
			TankArmy tankArmy = new TankArmy(npcIndexList.get(i), npcIdList.get(i));
			armyList.add(tankArmy);
		}
		return armyList;
	}
	
	public NpcConfType getNpcConfType() {
		return npcConfType;
	}

	public int getPvpNpcId(){
		return npcIdList.get(0);
	}

	public boolean isNoCMorale() {
		return getNo_change_morale() == 1;
	}
}
