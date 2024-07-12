package com.hm.model.guild.tactics;

import com.hm.enums.GuildTacticsType;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.guild.Guild;
import com.hm.model.guild.tactics.vo.AirdropTaticsVo;
import com.hm.model.guild.tactics.vo.BaseGuildTacticsVo;
import com.hm.model.player.Player;
import com.hm.util.PubFunc;
import lombok.NoArgsConstructor;

/**
 * @Description: 空投
 * @author siyunlong  
 * @date 2019年1月17日 上午10:12:09 
 * @version V1.0
 */
@NoArgsConstructor
public class AirdropTatics extends AbstractCityTactics{
	private int times;//剩余次数
	private int maxTimes;
	
	public AirdropTatics(int second) {
		super(GuildTacticsType.Airdrop, second);
	}

	@Override
	public boolean useCityTactics(Player player, Guild guild, WorldCity worldCity) {
		guild.getGuildTactics().addGuildTactics(this);
		return true;
	}

	@Override
	public void loadLvValue(String value) {
		this.times = PubFunc.parseInt(value);
		this.maxTimes = times;
	}

	public int getTimes() {
		return times;
	}
	
	public void reduceTimes() {
		this.times --;
	}

	@Override
	public BaseGuildTacticsVo createGuildTacticsVo() {
		return new AirdropTaticsVo(this);
	}

	public int getMaxTimes() {
		return maxTimes;
	}
	
}
