package com.hm.leaderboards;

import com.hm.config.excel.templaextra.NpcTroopTemplate;
import com.hm.model.player.BasePlayer;
import com.hm.redis.PlayerRedisData;
import lombok.Data;

@Data
public class PlayerRankData{
	private String id;
	private long playerId;
	public String name;
	public String icon;
	public int frameIcon;
	public long combat;
	private boolean isOnline;//是否在线
	
	public PlayerRankData() {
		super();
	}
	
	public PlayerRankData(PlayerRedisData redisData) {
		if(redisData == null) {
			return;
		}
		this.id = redisData.getId()+"";
		this.load(redisData);
	}

	public void load(PlayerRedisData redisData) {
		this.playerId = redisData.getId();
		this.icon = redisData.getIcon();
		this.name = redisData.getName();
		this.frameIcon = redisData.getFrameIcon();
		this.combat = redisData.getCombat();
	}

	public void load(BasePlayer player) {
		this.playerId = player.getId();
		this.icon = player.playerHead().getIcon();
		this.name = player.getName();
		this.frameIcon = player.playerHead().getFrameIcon();
		this.combat = player.getPlayerDynamicData().getCombat();
	}


	public void loadNpc(NpcTroopTemplate template) {
		this.icon = template.getHead_icon()+"";
		this.frameIcon = template.getHead_frame();
	}
}
