package com.hm.config;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.templaextra.TrumpTemplate;
import com.hm.util.MathUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Slf4j
@Config
public class TrumpConfig extends ExcleConfig{
	//王牌对决
	private Map<Integer,TrumpTemplate> trumpMap = Maps.newConcurrentMap();
	private int maxLv;
	
	@Override
	public void loadConfig() {
		loadTrumpTemplate();
		this.maxLv = MathUtils.max(trumpMap.keySet());
	}
	@Override
	public List<String> getDownloadFile() {
		return getConfigName(TrumpTemplate.class);
	}
	
	private void loadTrumpTemplate(){
		Map<Integer, TrumpTemplate> missionTypeMap = Maps.newHashMap();
		List<TrumpTemplate> templateList = JSONUtil.fromJson(getJson(TrumpTemplate.class), new TypeReference<ArrayList<TrumpTemplate>>(){});
		for(TrumpTemplate template:templateList){
			template.init();
			missionTypeMap.put(template.getId(),template);
		}
		this.trumpMap = ImmutableMap.copyOf(missionTypeMap);
		log.info("王牌对决配置加载完成");
	}
	
	public int getMaxLv() {
		return maxLv;
	}
	public TrumpTemplate getTrumpTemplate(int id){
		return trumpMap.get(Math.min(this.maxLv, id));
	}
	
	public int randomTankIds(int lv,List<Integer> filterTankIds) {
		return getTrumpTemplate(lv).randomTankIds(filterTankIds);
	}
}
