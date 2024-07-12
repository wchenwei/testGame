package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.*;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.CityWarSkillTemplate;
import com.hm.config.excel.templaextra.CityWarSkillBoxTemplateImpl;
import com.hm.config.excel.templaextra.CityWarSkillUpgradeTemplateImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 城战技能配置信息
 * @ClassName:  CityWarSkillConfig   
 * @Description:
 * @author: zxj
 * @date:   2020年6月2日 上午11:54:57
 */
@Config
public class CityWarSkillConfig extends ExcleConfig{
	
	private Map<Integer, CityWarSkillBoxTemplateImpl> cityWarSkillBox = Maps.newConcurrentMap();
	private Map<Integer, CityWarSkillUpgradeTemplateImpl> cityWarSkillUpgrade = Maps.newConcurrentMap();
	private Map<Integer, CityWarSkillTemplate> cityWarSkill = Maps.newConcurrentMap();
	private Table<Integer, Integer, CityWarSkillUpgradeTemplateImpl> skillUpgradeTable = HashBasedTable.create();

	@Override
	public void loadConfig() {
		loadCityWarSkillBox();
		loadCityWarSkillUpgrade();
		loadCityWarSkill();
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(CityWarSkillBoxTemplateImpl.class,
				CityWarSkillUpgradeTemplateImpl.class,
				CityWarSkillTemplate.class);
	}
	
	private void loadCityWarSkillBox() {
		List<CityWarSkillBoxTemplateImpl> templateList = JSONUtil.fromJson(getJson(CityWarSkillBoxTemplateImpl.class),
				new TypeReference<ArrayList<CityWarSkillBoxTemplateImpl>>() {
				});
		Map<Integer, CityWarSkillBoxTemplateImpl> tempCityWarSkillBox = Maps.newConcurrentMap();
		templateList.forEach(temp -> {
			temp.init();
			tempCityWarSkillBox.put(temp.getId(), temp);
		});
		this.cityWarSkillBox = ImmutableMap.copyOf(tempCityWarSkillBox);
	}
	
	private void loadCityWarSkillUpgrade() {
		List<CityWarSkillUpgradeTemplateImpl> templateList = JSONUtil.fromJson(getJson(CityWarSkillUpgradeTemplateImpl.class),
				new TypeReference<ArrayList<CityWarSkillUpgradeTemplateImpl>>() {
				});
		Table<Integer, Integer, CityWarSkillUpgradeTemplateImpl> tempSkillUpgradeTable = HashBasedTable.create();
		Map<Integer, CityWarSkillUpgradeTemplateImpl> tempCityWarSkillUpgrade = Maps.newConcurrentMap();
		templateList.forEach(temp -> {
			temp.init();
			tempCityWarSkillUpgrade.put(temp.getId(), temp);
			tempSkillUpgradeTable.put(temp.getSkill_id(), temp.getSkill_lv(), temp);
		});
		this.cityWarSkillUpgrade = ImmutableMap.copyOf(tempCityWarSkillUpgrade);
		this.skillUpgradeTable = ImmutableTable.copyOf(tempSkillUpgradeTable);
	}
	
	private void loadCityWarSkill() {
		List<CityWarSkillTemplate> templateList = JSONUtil.fromJson(getJson(CityWarSkillTemplate.class),
				new TypeReference<ArrayList<CityWarSkillTemplate>>() {
				});
		Map<Integer, CityWarSkillTemplate> tempCityWarSkill = Maps.newConcurrentMap();
		templateList.forEach(temp -> {
			tempCityWarSkill.put(temp.getId(), temp);
		});
		this.cityWarSkill = ImmutableMap.copyOf(tempCityWarSkill);
	}
	
	public CityWarSkillBoxTemplateImpl getCityWarSkillBox(int id) {
		return cityWarSkillBox.get(id);
	}
	
	public CityWarSkillUpgradeTemplateImpl getSkillUpgradeTemplate(int id) {
		return cityWarSkillUpgrade.get(id);
	}

	public CityWarSkillUpgradeTemplateImpl getSkillUpgradeTemplate(int skillId, int lv) {
		return skillUpgradeTable.get(skillId, lv);
	}
	
	public List<CityWarSkillTemplate> getSkillTemplate() {
		return cityWarSkill.values().stream().collect(Collectors.toList());
	}
}




