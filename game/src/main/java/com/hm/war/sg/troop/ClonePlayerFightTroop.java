package com.hm.war.sg.troop;

import com.hm.model.cityworld.troop.ClonePlayerTroop;
import com.hm.war.sg.FightTroopType;
import com.hm.war.sg.UnitGroup;
import com.hm.war.sg.unit.Unit;
import com.hm.war.sg.unit.UnitAttr;

/**
 * @Description: 镜像战斗部队
 * @author siyunlong  
 * @date 2019年2月25日 下午4:00:26 
 * @version V1.0
 */
public class ClonePlayerFightTroop extends AbstractPlayerFightTroop{

	public ClonePlayerFightTroop(ClonePlayerTroop clonePlayerTroop) {
		super(clonePlayerTroop.getPlayerId(), clonePlayerTroop.getId());
		setTankList(clonePlayerTroop.getArmyList());
		setFormationId(clonePlayerTroop.getFormationId());
	}
	
	@Override
	public FightTroopType getFightTroopType() {
		return FightTroopType.ClonePlayer;
	}
	
	@Override
	public UnitGroup createUnitGroup() {
		UnitGroup unitGroup = super.createUnitGroup();
		unitGroup.setName(unitGroup.getName()+"-镜像");
		//属性80%
		for (Unit unit : unitGroup.getUnits()) {
			UnitAttr unitAttr = unit.getSetting().getUnitAttr();
			unitAttr.calAttrRate(0.8d);
		}
		return unitGroup;
	}

	
	@Override
	public boolean isClonePlayer() {
		return true;
	}
}
