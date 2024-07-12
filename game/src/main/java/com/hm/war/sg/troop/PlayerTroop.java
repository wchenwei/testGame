package com.hm.war.sg.troop;

import com.hm.action.troop.client.ClientTroop;

import java.util.List;

/**
 * 
 * @Description: 玩家部队
 * @author siyunlong  
 * @date 2018年10月23日 下午1:44:58 
 * @version V1.0
 */
public class PlayerTroop extends AbstractPlayerFightTroop{

	public PlayerTroop(long playerId, String troopId) {
		super(playerId, troopId);
	}
	
	public PlayerTroop(long playerId, String troopId,List<TankArmy> tankList) {
		super(playerId, troopId);
		setTankList(tankList);
	}
	
	public void loadClientTroop(ClientTroop clientTroop) {
		setTankList(clientTroop.getArmyList());
		setFormationId(clientTroop.getAircraftId());
	}
}
