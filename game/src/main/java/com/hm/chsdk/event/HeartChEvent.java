package com.hm.chsdk.event;

import com.hm.chsdk.CHSDKContants;
import com.hm.chsdk.ChSDKConf;
import com.hm.model.player.Player;

public class HeartChEvent extends AbstractChEvent{
	public String pfuid;//玩家账号
	public String cpsid;
	public String cpsn;
	public String cprid;
	public String rn;
	
	public HeartChEvent(Player player,ChSDKConf chSDKConf) {
		super(player,chSDKConf);
	}
	
	@Override
	public void buildPlayerClient(Player player) {
		super.buildPlayerClient(player);
		this.cpsid = player.getServerId()+"";
		this.cpsn = player.getCreateServerId()+"服";
		this.cprid = player.getId()+"";
		this.rn = player.getName();
		this.pfuid = player.getUid()+"";
	}

	@Override
	public String buildUrl() {
		return CHSDKContants.HeartUrl;
	}
	@Override
	public Class buildClass() {
		return HeartChEvent.class;
	}
	
}
