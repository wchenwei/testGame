package com.hm.model.guild.tactics;

import com.hm.enums.GuildTacticsType;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.guild.Guild;
import com.hm.model.guild.tactics.vo.BaseGuildTacticsVo;
import com.hm.model.guild.tactics.vo.TheSuckerTaticsVo;
import com.hm.model.player.Player;
import com.hm.util.PubFunc;
import lombok.NoArgsConstructor;

/**
 * @Description: 暗度陈仓
 * @author siyunlong  
 * @date 2019年1月17日 上午10:20:24 
 * @version V1.0
 */
@NoArgsConstructor
public class TheSuckerTatics extends AbstractCityTactics{
	private int reduceBei;//减少倍数
	
	public TheSuckerTatics(int second) {
		super(GuildTacticsType.TheSucker, second);
	}

	@Override
	public boolean useCityTactics(Player player, Guild guild, WorldCity worldCity) {
		guild.getGuildTactics().addGuildTactics(this);
		return true;
	}
	
	@Override
	public void loadLvValue(String value) {
		this.reduceBei = PubFunc.parseInt(value);
	}
	
	public int getReduceBei() {
		return reduceBei;
	}

	@Override
	public BaseGuildTacticsVo createGuildTacticsVo() {
		return new TheSuckerTaticsVo(this);
	}
}
