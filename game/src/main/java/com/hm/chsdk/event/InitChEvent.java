package com.hm.chsdk.event;

import com.hm.chsdk.CHSDKContants;
import com.hm.chsdk.ChSDKConf;
import com.hm.model.player.Player;

public class InitChEvent extends AbstractChEvent{
	public String idfa;//IOS设备信息(IOS必填)
	public String mt;//设备类型
	public String imei;//安卓设备信息(安卓必填)
	public String ip;

	public InitChEvent(Player player,ChSDKConf chSDKConf) {
		super(player,chSDKConf);
	}
	
	@Override
	public void buildPlayerClient(Player player) {
		super.buildPlayerClient(player);
		this.idfa = player.playerTemp().getClientParm("idfa", "");
		this.mt = player.playerTemp().getClientParm("mt", "");
		this.ip = player.getIp();
//		this.imei = player.playerTemp().getClientParm("imei", "");
	}

	@Override
	public String buildUrl() {
		return CHSDKContants.InitUrl;
	}
	
	@Override
	public Class buildClass() {
		return InitChEvent.class;
	}
	
}
