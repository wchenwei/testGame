package com.hm.model.cityworld;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.hm.enums.AtkDefType;
import com.hm.war.sg.UnitGroup;
import com.hm.war.sg.WarResult;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Setter
@Getter
public class CityFight extends CityComponent{
	private WarResult warResult;
	//本次战斗参与玩家id
	private HashMultimap<Integer, Long> joinPlayerIds = HashMultimap.create();
	//单挑列表
	private List<Pvp1v1> pvp1v1List = Lists.newArrayList();
	//防守方第一只部队信息
	private UnitGroup defFirstTroop;


	//是否在战斗中
	public boolean isFighting() {
		return warResult != null && !warResult.isOver();
	}

	public void addJoinPlayer(AtkDefType type,long playerId) {
		this.joinPlayerIds.put(type.getType(), playerId);
	}
	public Set<Long> getJoinPlayerIds(AtkDefType type) {
		return this.joinPlayerIds.get(type.getType());
	}
	
	public void addPvp1v1(Pvp1v1 temp) {
		this.pvp1v1List.add(temp);
	}
	

	public Pvp1v1 getPvp1v1(String troopId) {
		return pvp1v1List.stream().filter(e -> e.isContain(troopId)).findFirst().orElse(null);
	}
	
	public boolean pvpListIsEmpty() {
		return CollUtil.isEmpty(pvp1v1List);
	}
	
	public void clearFightData() {
		this.warResult = null;
		this.joinPlayerIds.clear();
	}
}
