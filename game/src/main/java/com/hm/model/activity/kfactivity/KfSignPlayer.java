package com.hm.model.activity.kfactivity;

import com.hm.action.troop.client.ClientTroop;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KfSignPlayer {
	private long id;
	//index:tankId,index:tankId,index:tankId#airId
	private ClientTroop troopInfo;
	
	public KfSignPlayer(long id, ClientTroop clientTroop) {
		super();
		this.id = id;
		this.troopInfo = clientTroop;
	}
	
	
}
