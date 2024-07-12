package com.hm.model.guild.tactics;

import com.hm.libcore.spring.SpringUtil;
import com.hm.action.cityworld.biz.WorldBiz;
import com.hm.enums.CityStatusType;
import com.hm.enums.GuildTacticsType;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.guild.Guild;
import com.hm.model.guild.tactics.vo.BaseGuildTacticsVo;
import com.hm.model.guild.tactics.vo.NoAirdropTaticsVo;
import com.hm.model.player.Player;
import lombok.NoArgsConstructor;

/**
 * @Description: 禁止空投
 * @author siyunlong  
 * @date 2019年1月17日 上午10:12:09 
 * @version V1.0
 */
@NoArgsConstructor
public class NoAirdropTatics extends AbstractCityTactics{
	public NoAirdropTatics(int second) {
		super(GuildTacticsType.NoAirdrop, second);
	}

	@Override
	public boolean useCityTactics(Player player, Guild guild, WorldCity worldCity) {
		guild.getGuildTactics().addGuildTactics(this);
		
		worldCity.getCityStatus().putCityStatus(CityStatusType.NoAirdrop, getEndTime());
		worldCity.saveDB();
		//更新城池变化
		WorldBiz worldBiz = SpringUtil.getBean(WorldBiz.class);
		worldBiz.broadWorldCityUpdate(worldCity);
		
		return true;
	}

	@Override
	public void loadLvValue(String value) {
	}

	@Override
	public BaseGuildTacticsVo createGuildTacticsVo() {
		return new NoAirdropTaticsVo(this);
	}
}
