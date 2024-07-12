package com.hm.model.player;

import com.hm.config.excel.templaextra.NpcTroopTemplate;
import com.hm.libcore.db.mongo.DBEntity;
import com.hm.model.guild.Guild;
import com.hm.model.guild.bean.GuildPlayer;
import com.hm.redis.PlayerRedisData;
import com.hm.redis.data.GuildRedisData;
import com.hm.redis.util.RedisUtil;
import com.hm.servercontainer.guild.GuildContainer;
import com.hm.servercontainer.guild.GuildItemContainer;
import lombok.Data;

@Data
public class SimplePlayerVo {
	private long playerId;
	public String name;
	public int lv;
	public String icon;
	public int frameIcon;
	private int carLv;//车长等级
	private int mlv;//军衔等级
	private int[] equQuality;//装备品质
	public int titleId;
	public int vipLv;
	public long combat;
	public int guildId;
	public String gname;
	public String flag;
	public Integer job;
	public Integer npcType;

	public SimplePlayerVo() {
		super();
	}
	
	public void load(PlayerRedisData redisData) {
		this.playerId = redisData.getId();
		this.lv = redisData.getLv();
		this.icon = redisData.getIcon(); 
		this.name = redisData.getName();
		this.frameIcon = redisData.getFrameIcon();
		this.titleId = redisData.getTitleId();
		this.guildId = redisData.getGuildId();
		this.vipLv = redisData.getVipLv();
		this.combat = redisData.getCombat();
		this.carLv = redisData.getCarLv();
		this.mlv = redisData.getMlv();
		this.equQuality = redisData.getEquQuality();
	}
	
	public void load(BasePlayer player) {
		this.playerId = player.getId();
		this.lv = player.playerLevel().getLv();
		this.icon = player.playerHead().getIcon(); 
		this.name = player.getName();
		this.frameIcon = player.playerHead().getFrameIcon(); 
		this.titleId = player.playerTitle().getUsingTitleId();
		this.guildId = player.playerGuild().getGuildId();
		this.vipLv = player.getPlayerVipInfo().getVipLv();
		this.combat = player.getPlayerDynamicData().getCombat();
		this.carLv = player.playerCommander().getCarLv();
		this.mlv = player.playerCommander().getMilitaryLv();
		this.equQuality = player.playerEquip().getEquQuality();

		loadGuild(player);
	}
	
	public void loadGuild(int serverId) {
		if(this.guildId > 0) {
			GuildItemContainer guildItemContainer = GuildContainer.of(serverId);
			if(guildItemContainer ==null) {
				loadGuild(RedisUtil.getGuildRedisData(this.guildId));
				return;
			}
			Guild guild = guildItemContainer.getGuild(this.guildId);
			loadGuild(guild);
		}
	}
	
	public void loadGuild(DBEntity entity) {
		if(this.guildId > 0) {
			loadGuild(entity.getServerId());
		}
	}
	public void loadGuild(Guild guild) {
		if(guild != null) {
			this.gname = guild.getGuildInfo().getGuildName();
			this.flag = guild.getGuildFlag().getFlagName();
			GuildPlayer guildPlayer = guild.getGuildMembers().guildPlayer(this.playerId);
			if(guildPlayer != null) {
				this.job = guildPlayer.getGuildJob();
			}
		}
	}
	
	public void loadGuild(GuildRedisData guild) {
		if(guild != null) {
			this.flag = guild.getFlagName();
		}
	}

	public void loadNpc(NpcTroopTemplate template) {
		this.lv = template.getLevel();
		this.icon = template.getHead_icon()+"";
		this.frameIcon = template.getHead_frame();
		this.carLv = template.getCar_lv();
		this.mlv = template.getMilitary_lv();
		this.equQuality = template.getEquQuality();
	}

}
