package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.templaextra.ActiveTankBeStrongerTemplate;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Config
public class TankBeStrongerConfig extends ExcleConfig {
	private Map<Integer,ActiveTankBeStrongerTemplate> map = Maps.newConcurrentMap();

	@Override
	public void loadConfig() {
		Map<Integer,ActiveTankBeStrongerTemplate> map = Maps.newConcurrentMap();
		List<ActiveTankBeStrongerTemplate> list = JSONUtil.fromJson(getJson(ActiveTankBeStrongerTemplate.class), new TypeReference<List<ActiveTankBeStrongerTemplate>>() {});
		list.forEach(t->t.init());
		map = list.stream().collect(Collectors.toMap(ActiveTankBeStrongerTemplate::getId, Function.identity()));
		this.map = ImmutableMap.copyOf(map);
	}
	
	@Override
	public List<String> getDownloadFile() {
		return getConfigName(ActiveTankBeStrongerTemplate.class);
	}
	
	public ActiveTankBeStrongerTemplate getTemplate(int id){
		return this.map.get(id);
	}

	public List<Integer> getRewardIds() {
		return Lists.newArrayList(this.map.keySet());
	}

}
