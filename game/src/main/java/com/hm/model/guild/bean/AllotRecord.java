package com.hm.model.guild.bean;

import com.hm.model.player.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllotRecord {
	private long playerId;
	private long targetPlayerId;
	private int id;
	private long time;
	
	public AllotRecord(Player player,Player targetPlayer,int id){
		this.playerId = player.getId();
        this.targetPlayerId = targetPlayer == null ? 0 : targetPlayer.getId();
		this.id = id;
		this.time = System.currentTimeMillis();
	}

	public AllotRecordVo createVo() {
		return new AllotRecordVo(this);
	}

}
