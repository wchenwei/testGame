package com.hm.action.cityworld.vo;

import com.hm.action.guild.vo.GuildSimpleVo;
import com.hm.enums.CityStatusType;
import com.hm.model.cityworld.WorldCity;
import lombok.Data;

@Data
public class WorldCityVo {
	private int id;
	private int guildId;
	private String guildName;
	private boolean hasFight;
	private String flag;
	private int flagId;
	private Long roadblockTime;//
	private Long noAirTime;//不能空头的时间

	public WorldCityVo(WorldCity worldCity) {
		this.id = worldCity.getId();
		this.guildId = worldCity.getCityBelong().getGuildId();
		this.hasFight = worldCity.hasFight();
		long tempTime = worldCity.getCityStatus().getCityStatus(CityStatusType.Roadblock);
		if(tempTime > 0) {
			this.roadblockTime = tempTime;
		}
		long tempTime2 = worldCity.getCityStatus().getCityStatus(CityStatusType.NoAirdrop);
		if(tempTime2 > 0) {
			this.noAirTime = tempTime2;
		}

	}

	public WorldCityVo(GuildSimpleVo guildSimple) {
		this.guildId = guildSimple.getId();
		this.flag = guildSimple.getFlag();
		this.flagId = guildSimple.getFlagId();
	}
	
	
}
