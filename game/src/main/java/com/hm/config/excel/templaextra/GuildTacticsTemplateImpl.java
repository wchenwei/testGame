package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.ConfigInit;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.util.string.StringUtil;
import com.hm.action.cityworld.biz.WorldDeclareWarBiz;
import com.hm.config.excel.temlate.GuildTacticsTemplate;
import com.hm.enums.GuildJob;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.guild.Guild;
import com.hm.model.player.Player;

import java.util.List;

@FileConfig("guild_tactics")
public class GuildTacticsTemplateImpl extends GuildTacticsTemplate{
	public String[] lvValues;
	public List<Integer> jobList = Lists.newArrayList();
	public List<Integer> cityTypeList = Lists.newArrayList();

	@ConfigInit
	public void init() {
		this.lvValues = getValue().split("_");
		this.jobList = StringUtil.splitStr2IntegerList(getUser(), ",");
		this.cityTypeList = StringUtil.splitStr2IntegerList(getCity(), ",");
	}
	
	public String getLvValue(int lv) {
		if(lv > this.lvValues.length) {
			return this.lvValues[this.lvValues.length-1];
		}
		return this.lvValues[lv - 1];
	}
	
	public boolean isCanUse(GuildJob jobEnum) {
		return jobList.contains(jobEnum.getType());
	}
	
	private int getCityType(Player player,Guild guild,WorldCity worldCity) {
		WorldDeclareWarBiz worldDeclareWarBiz = SpringUtil.getBean(WorldDeclareWarBiz.class);
		//敌对或者自己的城池
		if(worldDeclareWarBiz.isEnemyGuild(player, worldCity)) {
			return 1;
		}
		if(worldCity.getBelongGuildId() == guild.getId()) {
			return 2;
		}
		return 0;
	}
	
	public boolean isCanFitCity(Player player,Guild guild,WorldCity worldCity) {
		if(cityTypeList.isEmpty()) {
			return true;
		}
		return this.cityTypeList.contains(getCityType(player, guild, worldCity));
	}
	
	public boolean isCanFitCity(int cityType) {
		if(cityTypeList.isEmpty()) {
			return true;
		}
		return this.cityTypeList.contains(cityType);
	}
}
