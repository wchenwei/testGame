package com.hm.chsdk.event;

import com.hm.chsdk.CHSDKContants;
import com.hm.chsdk.ChSDKConf;
import com.hm.model.player.Player;

import java.util.Map;

public class RechargeChEvent extends AbstractChEvent{
	public String pfuid;//玩家账号
	public String cpsid;
	public String cpsn;
	public String cprid;
	public String rn;
	public String rl;
	public String cpon;//CP订单号
	public String amt;//订单金额(分)
	public String chon;//渠道订单号
	public String idfa;//
	
	public RechargeChEvent(Player player,ChSDKConf chSDKConf,Map<String, String> params) {
		super(player,chSDKConf);
		this.cpon = params.getOrDefault("orderid", "test1");
		this.amt = params.getOrDefault("rmb","1");
		this.chon = params.getOrDefault("suporderid", "test2");
	}
	
	@Override
	public void buildPlayerClient(Player player) {
		super.buildPlayerClient(player);
		this.cpsid = player.getServerId()+"";
		this.cpsn = player.getCreateServerId()+"服";
		this.cprid = player.getId()+"";
		this.rn = player.getName();
		this.rl = player.playerLevel().getLv()+"";
		this.pfuid = player.getUid()+"";
		this.idfa = player.playerTemp().getClientParm("idfa", "");
	}

	@Override
	public String buildUrl() {
		return CHSDKContants.RechargeUrl;
	}
	
	@Override
	public Class buildClass() {
		return RechargeChEvent.class;
	}
	
	public static void main(String[] args) {
		
	}
	
}
