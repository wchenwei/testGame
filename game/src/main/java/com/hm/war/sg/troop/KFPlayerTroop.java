package com.hm.war.sg.troop;

import com.hm.action.troop.client.ClientTroop;
import com.hm.model.player.Player;

/**
 * 
 * @Description: 玩家部队
 * @author siyunlong  
 * @date 2018年10月23日 下午1:44:58 
 * @version V1.0
 */
public class KFPlayerTroop extends AbstractPlayerFightTroop{
	private Player player;

	public KFPlayerTroop(long playerId, String troopId) {
		super(playerId, troopId);
	}

	public void loadClientTroop(Player player,ClientTroop clientTroop) {
		this.player = player;
		setTankList(clientTroop.getArmyList());
		setFormationId(clientTroop.getAircraftId());
	}

	@Override
	public Player getPlayer() {
		return player;
	}
}
