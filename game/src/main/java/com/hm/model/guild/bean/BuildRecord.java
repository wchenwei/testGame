package com.hm.model.guild.bean;

import com.hm.model.player.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuildRecord {
	private long playerId;
	private int type;
	private long time;
	private int count;
	
	public BuildRecord(Player player, int type, int count){
		this.playerId = player.getId();
		this.type = type;
		this.time = System.currentTimeMillis();
		this.count = count;
	}

	public BuildRecordVo createVo(){
		BuildRecordVo vo = new BuildRecordVo(this);
		return vo;
	}

}
