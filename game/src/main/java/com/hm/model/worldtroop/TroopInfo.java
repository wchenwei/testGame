package com.hm.model.worldtroop;

import com.hm.model.player.Player;

/**
 * @Description: 部队信息
 * @author siyunlong  
 * @date 2018年11月3日 上午3:34:23 
 * @version V1.0
 */
public class TroopInfo extends TroopComponent{
	private int index;
	private int formationId;
	
	
	public void createLoadPlayer(Player player) {
		this.index = player.playerTroops().getTroopIndex(super.Context().getId());
		SetChanged();
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
		SetChanged();
	}
	public void bindFormation(int id) {
		this.formationId = id;
		SetChanged();
	}
	public int getFormationId() {
		return formationId;
	}
	
	
}
