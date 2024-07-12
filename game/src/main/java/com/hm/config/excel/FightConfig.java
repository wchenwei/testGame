package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.DamageModifyTemplate;
import com.hm.config.excel.temlate.TerrainTemplate;
import com.hm.util.MathUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 战斗修正
 * @author siyunlong  
 * @date 2018年1月9日 下午4:31:09 
 * @version V1.0
 */
@Config
public class FightConfig extends ExcleConfig {
	private double[][] fightModiy = new double[5][5];
	private Map<Integer,TerrainTemplate> terrainMap = Maps.newHashMap();
	
	@Override
	public void loadConfig() {
		double[][] fightModiy = new double[5][5];
		for(DamageModifyTemplate template:loadFile()) {
			fightModiy[template.getAtker_type()][template.getDefender_type()] = MathUtils.round(template.getModify_value(), 2);
		}
		this.fightModiy = fightModiy;
		loadTerrainMap();
	}
	
	public void loadTerrainMap() {
		List<TerrainTemplate> templateList = JSONUtil.fromJson(getJson(TerrainTemplate.class), new TypeReference<ArrayList<TerrainTemplate>>(){});
		this.terrainMap = ImmutableMap.copyOf(templateList.stream().collect(Collectors.toMap(TerrainTemplate::getId, e -> e)));
	}
	
	private List<DamageModifyTemplate> loadFile() {
		return JSONUtil.fromJson(getJson(DamageModifyTemplate.class), new TypeReference<ArrayList<DamageModifyTemplate>>(){});
	}
	
	@Override
	public List<String> getDownloadFile() {
		return getConfigName(DamageModifyTemplate.class,TerrainTemplate.class);
	}
	
	public double getDamageModify(int atkType,int defType) {
		return this.fightModiy[atkType][defType];
	}
	
	public List<TerrainTemplate> getTerrainTemplate(List<Integer> ids,int terrainType) {
		return ids.stream().map(e -> this.terrainMap.get(e))
				.filter(e -> e != null)
				.filter(e -> e.getTerrain() == terrainType)
				.collect(Collectors.toList());
	}
}
  
