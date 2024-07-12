package com.hm.config;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.config.excel.temlate.PolicyExpLimitTemplate;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.ExcleConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Config
public class ServerPowerConfig extends ExcleConfig{
	private Map<Integer, PolicyExpLimitTemplate> plicyExpMap = Maps.newConcurrentMap();
	@Override
	public void loadConfig() {
		loadPlicyExpConfig();
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(PolicyExpLimitTemplate.class);
	}
	
	private void loadPlicyExpConfig(){
		Map<Integer,PolicyExpLimitTemplate> plicyExpMap = Maps.newConcurrentMap();
		List<PolicyExpLimitTemplate> templateList = JSONUtil.fromJson(getJson(PolicyExpLimitTemplate.class), new TypeReference<ArrayList<PolicyExpLimitTemplate>>(){});
		plicyExpMap = templateList.stream().collect(Collectors.toMap(PolicyExpLimitTemplate::getLevel, e->e));
		this.plicyExpMap = ImmutableMap.copyOf(plicyExpMap);
	}
	
	public PolicyExpLimitTemplate getPolicyExp(int lv){
		return plicyExpMap.get(lv);
	}
	
	public long getExpLimit(int lv){
		PolicyExpLimitTemplate template = getPolicyExp(lv);
		return template==null?0:template.getExp_limit();
	}

}
