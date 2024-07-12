package com.hm.config;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.templaextra.TitleTemplate;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Config
public class TitleConfig extends ExcleConfig{
	private Map<Integer,TitleTemplate> map = Maps.newConcurrentMap();

	@Override
	public void loadConfig() {
		Map<Integer,TitleTemplate> map = Maps.newConcurrentMap();
		for(TitleTemplate template:loadFile()){
			template.init();
			map.put(template.getId(), template);
		}
		this.map = ImmutableMap.copyOf(map);
	}

	private List<TitleTemplate> loadFile() {
		return JSONUtil.fromJson(getJson(TitleTemplate.class), new TypeReference<ArrayList<TitleTemplate>>(){});
	}

	public TitleTemplate getTitleTemplate(int id){
		return map.get(id);
	}
	
	public List<TitleTemplate> getTitlesByType(int type){
		return loadFile().stream().filter(t -> t.getType()==type).collect(Collectors.toList());
	}
}
