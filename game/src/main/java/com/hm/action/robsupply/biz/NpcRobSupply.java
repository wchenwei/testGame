package com.hm.action.robsupply.biz;

import com.hm.model.player.SupplyItem;
import lombok.Data;

@Data
public class NpcRobSupply {
	private String id;
	private int npcId;
	private SupplyItem supplyItem;
	public NpcRobSupply(String id, int npcId, SupplyItem supplyItem) {
		super();
		this.id = id;
		this.npcId = npcId;
		this.supplyItem = supplyItem;
	}
	
}
