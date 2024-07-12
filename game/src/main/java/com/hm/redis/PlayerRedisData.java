package com.hm.redis;

import com.hm.enums.GuildJob;
import com.hm.enums.StatisticsType;
import com.hm.model.guild.Guild;
import com.hm.model.player.BasePlayer;
import com.hm.servercontainer.guild.GuildContainer;
import lombok.Data;

import java.util.Date;

@Data
public class PlayerRedisData {
	private long id;
	private int campJob;
	private int vipLv;
	private int guildId; 
	private int lv;
	private int fbId;
	private int channelId;
	private String name;
	private long combat;
	private long lastOffLineDate;
	private long lastLoginDate;
	private String icon;
	private int frameIcon;//当前头像框
	private int carLv;//车长等级
	private int mlv;//军衔等级
	private int[] equQuality;//装备品质
	private long troopCombat; //竞技场战力
	private int guildJob;
	private int titleId;
	private long rechargeGold;
	private long createtime;
	private long uid;
	private int serverid;

	public PlayerRedisData() {
		super();
	}
	
	public PlayerRedisData(BasePlayer player) {
		this.id = player.getId();
		this.name = player.getName();
		this.lv = player.playerLevel().getLv();
		this.fbId = player.playerMission().getFbId();
		this.channelId = player.getChannelId();
		this.combat = player.getPlayerDynamicData().getCombat();
		this.troopCombat = player.getPlayerDynamicData().getTroopCombat();
		Date lastDate = player.playerBaseInfo().getLastOffLineDate();
		this.lastOffLineDate = null==lastDate?0:lastDate.getTime();
		this.guildId = player.playerGuild().getGuildId(); 
		this.icon = player.playerHead().getIcon(); 
		this.frameIcon = player.playerHead().getFrameIcon();
		this.carLv = player.playerCommander().getCarLv();
		this.mlv = player.playerCommander().getMilitaryLv();
		this.equQuality = player.playerEquip().getEquQuality();
		Date lastLogin = player.playerBaseInfo().getLastLoginDate();
		this.lastLoginDate = null==lastLogin?0:lastLogin.getTime();
		this.titleId = player.playerTitle().getUsingTitleId();
		this.vipLv = player.getPlayerVipInfo().getVipLv();
		this.rechargeGold = player.getPlayerStatistics().getLifeStatistics(StatisticsType.Diamond);

		Guild guild = GuildContainer.of(player.getServerId()).getGuild(player.playerGuild().getGuildId());
		if(null!=guild) {
			GuildJob jobEnum = guild.getGuildMembers().getJob(id);
			guildJob = null==jobEnum?0:jobEnum.getType();
		}
		uid = player.getUid();
		serverid = player.getServerId();
		createtime = player.playerBaseInfo().getCreateDate().getTime();
	}
	
	
}
