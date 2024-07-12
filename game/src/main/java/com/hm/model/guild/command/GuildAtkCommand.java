package com.hm.model.guild.command;

import com.hm.enums.GuildCommandType;
import lombok.NoArgsConstructor;

/**
 * @Description: 进攻
 * @author siyunlong  
 * @date 2019年3月15日 下午9:05:35 
 * @version V1.0
 */
@NoArgsConstructor
public class GuildAtkCommand extends AbstractGuildCommand{
	private int cityId;
	
	public GuildAtkCommand(int cityId) {
		super(GuildCommandType.CommandAtk);
		this.cityId = cityId;
	}
	
	@Override
	public boolean isFitCityId(int cityId) {
		return this.cityId == cityId;
	}

	public int getCityId() {
		return cityId;
	}
	
	
}
