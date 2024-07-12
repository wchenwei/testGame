package com.hm.model.guild.tactics;

import com.hm.enums.GuildTacticsType;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.guild.Guild;
import com.hm.model.guild.tactics.vo.ArtillerySupportTacticsVo;
import com.hm.model.guild.tactics.vo.BaseGuildTacticsVo;
import com.hm.model.player.Player;
import com.hm.util.PubFunc;
import lombok.NoArgsConstructor;

/**
 * @Description: 炮火支援
 * @author siyunlong  
 * @date 2019年1月17日 上午9:48:21 
 * @version V1.0
 */
@NoArgsConstructor
public class ArtillerySupportTactics extends AbstractCityTactics{
	private int skillId;
	
	public ArtillerySupportTactics(int second) {
		super(GuildTacticsType.ArtillerySupport, second);
	}
	
	@Override
	public boolean useCityTactics(Player player, Guild guild,WorldCity worldCity) {
		guild.getGuildTactics().addGuildTactics(this);
		return true;
	}
	
	@Override
	public void loadLvValue(String value) {
		this.skillId = PubFunc.parseInt(value);
	}

	public int getSkillId() {
		return skillId;
	}
	
	@Override
	public BaseGuildTacticsVo createGuildTacticsVo() {
		return new ArtillerySupportTacticsVo(this);
	}
}
