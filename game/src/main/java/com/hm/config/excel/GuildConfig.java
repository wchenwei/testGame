package com.hm.config.excel;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.hm.config.excel.temlate.GuildDonateTemplate;
import com.hm.config.excel.temlate.GuildDonateTemplateImpl;
import com.hm.config.excel.temlate.GuildLevelTemplate;
import com.hm.config.excel.temlate.GuildTecTemplateImpl;
import com.hm.config.excel.templaextra.GuildTacticsTemplateImpl;
import com.hm.enums.GuildTecFunEnum;
import com.hm.libcore.annotation.Config;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Config
public class GuildConfig extends ExcleConfig {
	private Map<Integer, GuildDonateTemplateImpl> guildDonate = Maps.newConcurrentMap();
	private Map<Integer, GuildLevelTemplate> guildLevel = Maps.newConcurrentMap();
	@Getter
	private Map<Integer, GuildTecTemplateImpl> guildTec = Maps.newConcurrentMap();
	private ArrayListMultimap<Integer,GuildTecTemplateImpl> techTypeMap = ArrayListMultimap.create();

	private Map<Integer, GuildTacticsTemplateImpl> guildTacticsTec = Maps.newConcurrentMap();
	private int maxDonateTimes;
	
	@Override
	public void loadConfig() {
		//部落升级消耗产出信息
		this.guildDonate = json2ImmutableMap(GuildDonateTemplateImpl::getId, GuildDonateTemplateImpl.class);
		this.maxDonateTimes = this.guildDonate.values().stream().map(GuildDonateTemplate::getCount).max(Integer::compareTo).orElse(0);
		//部落等级信息
		this.guildLevel = json2ImmutableMap(GuildLevelTemplate::getLevel, GuildLevelTemplate.class);
		//部落科技
		this.guildTec = json2ImmutableMap(GuildTecTemplateImpl::getId, GuildTecTemplateImpl.class);
		ArrayListMultimap<Integer, GuildTecTemplateImpl> techTypeMap = ArrayListMultimap.create();
		for (GuildTecTemplateImpl value : guildTec.values()) {
			techTypeMap.put(value.getTec_func(), value);
		}
		this.techTypeMap = techTypeMap;


		this.guildTacticsTec = json2ImmutableMap(GuildTacticsTemplateImpl::getId, GuildTacticsTemplateImpl.class);

	}

	//获取下一次捐献信息
	public GuildDonateTemplateImpl getNextDonate(long times) {
		return guildDonate.values().stream().filter(t->t.getCount()==times+1).findFirst().orElse(null);
	}
	//根据经验，获取部落等级
	public int getGuildLv(int exp) {
		for(int i =1; i<=guildLevel.size(); i++) {
			GuildLevelTemplate tempLevel = guildLevel.get(i);
			GuildLevelTemplate tempNextLevel = guildLevel.get(i+1);
			//最高等级
			if(null==tempNextLevel) {
				return i;
			}
			if(exp>=tempLevel.getExp_total() && exp<tempNextLevel.getExp_total()) {
				return i;
			}
		}
		return 1;
	}
	//根据部落等级，获取增加的科技点数
	public int getGuildLvPoint(int i) {
		GuildLevelTemplate tempGuildLevel = guildLevel.get(i);
		return null==tempGuildLevel?0:tempGuildLevel.getTec_point();
	}
	//根据部落等级，获取所有的科技点数
	public int getGuildAllPoint(int lv) {
		int allPoint = 0;
		for(int i=1; i<=lv; i++) {
			allPoint+=this.getGuildLvPoint(i);
		}
		return allPoint;
	}
	//根据id，获取部落科技
	public GuildTecTemplateImpl getGuilTec(int id) {
		return this.guildTec.get(id);
	}
	
	public GuildTacticsTemplateImpl getGuildTacticsTemplate(int id) {
		return this.guildTacticsTec.get(id);
	}

	public List<GuildTecTemplateImpl> getGTecTemplates(GuildTecFunEnum type) {
		return this.techTypeMap.get(type.getType());
	}

	//根据部落等级，获取部落的人数
	public int getGuildMemberNum(int lv) {
		return guildLevel.get(lv)==null?10:guildLevel.get(lv).getGuide_member();
	}

	public int getGuildCityNum(int lv){
		GuildLevelTemplate levelTemplate = guildLevel.get(lv);
		if (levelTemplate == null){
			return 0;
		}
		return levelTemplate.getCity_num();
	}

	//根据部落等级，获取部落驻兵最大数
	public int getGuildTroopLimit(int lv) {
		return guildLevel.get(lv)==null?20:guildLevel.get(lv).getArmy_base();
	}
	
	public GuildLevelTemplate getGuildLevelTemplate(int lv) {
		return this.guildLevel.get(lv);
	}

	public int getMaxDonateTimes() {
		return maxDonateTimes;
	}
}









