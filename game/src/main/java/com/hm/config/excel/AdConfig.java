package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.templaextra.AdTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Config
public class AdConfig extends ExcleConfig {
	private Map<Integer, AdTemplate> adMap = Maps.newHashMap();

	@Override
	public void loadConfig() {
		loadMailTemplate();
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(AdTemplate.class);
	}
	
	private void loadMailTemplate(){
		Map<Integer, AdTemplate> adMap = Maps.newHashMap();
		for(AdTemplate temp:JSONUtil.fromJson(getJson(AdTemplate.class), new TypeReference<ArrayList<AdTemplate>>(){})){
			temp.init();
			adMap.put(temp.getId(), temp);
		}
		this.adMap = ImmutableMap.copyOf(adMap);
	}
	public AdTemplate getTemplate(int id) {
		return adMap.get(id);
	}
	
	
}






