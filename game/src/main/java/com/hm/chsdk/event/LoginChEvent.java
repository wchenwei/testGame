package com.hm.chsdk.event;

import com.hm.chsdk.CHSDKContants;
import com.hm.chsdk.ChSDKConf;
import com.hm.model.player.Player;

public class LoginChEvent extends AbstractChEvent{
	public String pfuid;//玩家账号
	public String cpsid;
	public String cpsn;
	public String cprid;
	public String rn;
	public String ip;
	
	public LoginChEvent(Player player,ChSDKConf chSDKConf) {
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
		this.ip = player.getIp();
	}

	@Override
	public String buildUrl() {
		return CHSDKContants.LoginUrl;
	}
	
	@Override
	public Class buildClass() {
		return LoginChEvent.class;
	}
	
	public static void main(String[] args) {
		
	}
	
}
