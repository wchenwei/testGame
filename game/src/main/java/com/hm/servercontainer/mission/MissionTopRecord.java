package com.hm.servercontainer.mission;

import com.hm.model.player.Player;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MissionTopRecord {
	private long combat;
	private long playerId;
	private String tanks;
	private int superLv;
	
	public MissionTopRecord(Player player, long combat, String tanks) {
		this.playerId = player.getId();
		this.combat = combat;
		this.tanks = tanks;
		this.superLv = player.playerCommander().getSuperWeaponLv();
	}
	
}
