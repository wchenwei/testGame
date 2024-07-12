package com.hm.model.guild.bean;

import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.GuildFactoryConfig;
import com.hm.config.excel.templaextra.GuildFactoryBuildExtraTemplate;
import com.hm.enums.GuildJob;
import com.hm.model.player.Player;
import lombok.Data;

@Data
public class GuildPlayer {
	private long playerId;	//用户id
	private int guildJob;	//官职
	private long contr;		//部落贡献
	private long dayContr;	//当天的新增的部落贡献值
	private int part;//当天配件贡献值
	private int armsStrengthCount;//当天武器强化次数
	private long combat;//战力
	
	public GuildPlayer() {}

	public GuildPlayer(Player player) {
		this.playerId = player.getId();
		this.guildJob = 0;
		this.contr = 0;
		this.dayContr = 0;
		loadPlayerInfo(player);
	}
	
	public GuildPlayer(Player player, GuildJob job) {
		this.playerId = player.getId();
		this.guildJob = job.getType();
		this.contr = 0;
		loadPlayerInfo(player);
	}

	public void loadPlayerInfo(Player player) {
		this.combat = player.getCombat();
	}

	
	public void contrAdd(long num) {
		this.contr += num;
	}
	
	public void dayContrAdd(long num) {
		this.dayContr += num;
	}
	
	public void clearDayContrAdd() {
		this.dayContr = 0;
	}
	public void resetDay(){
		clearDayContrAdd();
		clearDayBuild();
	}
	public void clearDayBuild(){
		this.armsStrengthCount=0;
		this.part = 0;
	}
	
	
	public void build(int type, int count){
		GuildFactoryConfig guildFactoryConfig = SpringUtil.getBean(GuildFactoryConfig.class);
		GuildFactoryBuildExtraTemplate template = guildFactoryConfig.getBuildTemplate(type);
		if(template!=null){
			this.part+=(template.getPart_add()*count);
		}
	}
	public void strength(){
		this.armsStrengthCount++;
	}
}





