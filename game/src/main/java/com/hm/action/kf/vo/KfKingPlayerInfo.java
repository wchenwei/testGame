package com.hm.action.kf.vo;

import com.hm.model.activity.kfactivity.KfSignPlayer;
import com.hm.redis.PlayerRedisData;
import com.hm.redis.util.RedisUtil;

public class KfKingPlayerInfo {
    public long playerId;
    public int index;
    public String name;
    public String icon;
    public int frameIcon;
    public int vipLv;
    public long combat;
	
	public KfKingPlayerInfo(KfSignPlayer signPlayer,int index) {
		super();
		this.playerId = signPlayer.getId();
		this.index = index;
		loadKingInfo();
	}
	public KfKingPlayerInfo(long playerId) {
		super();
		this.playerId = playerId;
		loadKingInfo();
	}
	
	public void loadKingInfo() {
		PlayerRedisData playerInfo = RedisUtil.getPlayerRedisData(this.playerId);
		if(playerInfo != null) {
			this.name = playerInfo.getName();
			this.icon = playerInfo.getIcon();
			this.frameIcon = playerInfo.getFrameIcon();
			this.vipLv = playerInfo.getVipLv();
			this.combat = playerInfo.getCombat();
		}
	}
}
