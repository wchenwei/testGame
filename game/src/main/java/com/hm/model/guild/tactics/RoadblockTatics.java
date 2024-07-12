package com.hm.model.guild.tactics;

import com.hm.libcore.spring.SpringUtil;
import com.hm.action.cityworld.biz.WorldBiz;
import com.hm.enums.CityStatusType;
import com.hm.enums.GuildTacticsType;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.guild.Guild;
import com.hm.model.guild.tactics.vo.BaseGuildTacticsVo;
import com.hm.model.guild.tactics.vo.RoadblockTaticsVo;
import com.hm.model.player.Player;
import com.hm.util.PubFunc;
import lombok.NoArgsConstructor;

/**
 * @Description: 路障战术
 * @author siyunlong  
 * @date 2019年1月17日 上午10:20:24 
 * @version V1.0
 */
@NoArgsConstructor
public class RoadblockTatics extends AbstractCityTactics{
	private int skillId;
	
	public RoadblockTatics(int second) {
		super(GuildTacticsType.Roadblock, second);
	}

	@Override
	public boolean useCityTactics(Player player, Guild guild, WorldCity worldCity) {
		guild.getGuildTactics().addGuildTactics(this);
		worldCity.getCityStatus().putCityStatus(CityStatusType.Roadblock, getEndTime());
		worldCity.saveDB();
		//更新城池变化
		WorldBiz worldBiz = SpringUtil.getBean(WorldBiz.class);
		worldBiz.broadWorldCityUpdate(worldCity);
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
		return new RoadblockTaticsVo(this);
	}
}
