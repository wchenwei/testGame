package com.hm.model.guild;

import com.google.common.collect.Lists;
import com.hm.enums.GuildTacticsType;
import com.hm.model.guild.tactics.AbstractCityTactics;
import com.hm.model.guild.tactics.vo.BaseGuildTacticsVo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 部落战术
 * @author siyunlong  
 * @date 2019年1月17日 上午9:59:57 
 * @version V1.0
 */
public class GuildTactics extends GuildComponent{
	private ArrayList<AbstractCityTactics> tacticsList = new ArrayList<>();
	
	public AbstractCityTactics getGuildTactics(GuildTacticsType type,int cityId) {
		AbstractCityTactics temp = this.tacticsList.stream().filter(e -> e.getCityId() == cityId)
								 .filter(e -> e.getType() == type).findFirst().orElse(null);
		if(temp != null && !temp.isRun()) {
			removeGuildTactics(temp);
			return null;
		}
		return temp;
	}

	public boolean haveGuildTactics(int cityId) {
		return this.tacticsList.stream().anyMatch(e -> e.getCityId() == cityId);
	}
	
	public void removeGuildTactics(AbstractCityTactics tactics) {
		if(tactics != null) {
			this.tacticsList.remove(tactics);
			SetChanged();
		}
	}
	
	public void removeGuildTactics(GuildTacticsType type,int cityId) {
		AbstractCityTactics temp = getGuildTactics(type, cityId);
		if(temp != null) {
			removeGuildTactics(temp);
		}
	}
	
	public void addGuildTactics(AbstractCityTactics guildTactics) {
		this.tacticsList.add(guildTactics);
		SetChanged();
	}
	
	public List<BaseGuildTacticsVo> createGuildTacticsVo() {
		List<BaseGuildTacticsVo> resultList = Lists.newArrayList();
		for (int i = this.tacticsList.size()-1; i >= 0; i--) {
			AbstractCityTactics temp = this.tacticsList.get(i);
			if(!temp.isRun()) {
				tacticsList.remove(i);
				SetChanged();
				continue;
			}
			resultList.add(temp.createGuildTacticsVo());
		}
		return resultList;
	}
	public boolean isEmpty() {
		return this.tacticsList.isEmpty();
	}
}
