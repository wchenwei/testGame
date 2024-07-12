package com.hm.model.guild.tactics;

import com.hm.enums.GuildTacticsType;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.guild.Guild;
import com.hm.model.player.Player;
import lombok.NoArgsConstructor;

/**
 * @Description: 城池战术
 * @author siyunlong  
 * @date 2019年1月17日 上午9:47:49 
 * @version V1.0
 */
@NoArgsConstructor
public abstract class AbstractCityTactics extends AbstractGuildTactics{
	private int cityId;
	
	public AbstractCityTactics(GuildTacticsType guildTacticsType, int second) {
		super(guildTacticsType, second);
	}
	
	//对城镇使用战术
	public abstract boolean useCityTactics(Player player,Guild guild,WorldCity worldCity);
	
	@Override
	public boolean useTactics(Player player,Guild guild,Object... args) {
		WorldCity worldCity = (WorldCity)args[0];
		this.cityId = worldCity.getId();
		return useCityTactics(player,guild,worldCity);
	}

	public int getCityId() {
		return cityId;
	}


}
