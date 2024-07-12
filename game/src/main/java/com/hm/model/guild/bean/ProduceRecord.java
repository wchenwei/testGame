package com.hm.model.guild.bean;

import com.hm.model.player.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProduceRecord {
	private long playerId;
	private long time;
	
	public ProduceRecord(Player player){
		this.playerId = player.getId();
		this.time = System.currentTimeMillis();
	}
	
	public ProduceRecordVo createVo(){
		return new ProduceRecordVo(this);
	}

}
