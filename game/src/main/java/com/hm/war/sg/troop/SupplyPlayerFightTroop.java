package com.hm.war.sg.troop;

import com.hm.action.troop.client.ClientTroop;
import com.hm.model.player.Player;
import com.hm.model.supplytroop.SupplyTroop;
import com.hm.war.sg.UnitGroup;

import java.util.List;

/**
 * 
 * @Description: 补给战斗部队
 * @author siyunlong  
 * @date 2019年1月25日 下午2:30:22 
 * @version V1.0
 */
public class SupplyPlayerFightTroop extends AbstractPlayerFightTroop{
	public SupplyPlayerFightTroop(SupplyTroop supplyTroop) {
		super(supplyTroop.getPlayerId(), supplyTroop.getId());
		setTankList(supplyTroop.getTankList());
		setFormationId(supplyTroop.getFormationId());
	}
	
	public SupplyPlayerFightTroop(Player player,List<TankArmy> armyList) {
		super(player.getId(), player+"");
		setTankList(armyList);
	}
	
	public SupplyPlayerFightTroop(Player player,ClientTroop clientTroop) {
		super(player.getId(), player+"");
		setTankList(clientTroop.getArmyList());
		setFormationId(clientTroop.getAircraftId());
	}
	
	@Override
	public void doWarResult(UnitGroup unitGroup) {
//		unitGroup.getUnits().forEach(e -> {
//			TankArmy tankArmy = getTankArmy(e.getSetting().getTankId());
//			if(tankArmy == null) {
//				return;
//			}
//			if(e.isDeath()) {
//				tankArmy.setHp(0);
//				return;
//			}
//			tankArmy.setUnitRetainType(UnitRetainType.HP, e.getHp());
//		});
	}
}
