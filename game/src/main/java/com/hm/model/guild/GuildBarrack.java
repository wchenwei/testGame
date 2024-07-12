package com.hm.model.guild;

import com.google.common.collect.Lists;

import java.util.List;

public class GuildBarrack extends GuildComponent {
	private List<String> troops = Lists.newArrayList();
	//调动过的玩家
	private List<Long> transferIds = Lists.newArrayList();
	
	//添加部队到军营
	public void addTroops(String troopId){
		this.troops.add(troopId);
		SetChanged();
	}
	//撤军
	public void retreat(String troopId){
		this.troops.remove(troopId);
		SetChanged();
	}
	public List<String> getTroops() {
		return troops;
	}
	
	public List<Long> getTransferIds() {
		return transferIds;
	}
	//调动
	public void transfer(long playerId){
		if(!transferIds.contains(playerId)){
			this.transferIds.add(playerId);
			SetChanged();
		}
	}
	public void clearTransferIds(){
		this.transferIds.clear();
		SetChanged();
	}
	public void delTreansferId(long playerId){
		this.transferIds.remove(playerId);
		SetChanged();
	}
	public int getTroopNum() {
		return troops.size();
	}

}
