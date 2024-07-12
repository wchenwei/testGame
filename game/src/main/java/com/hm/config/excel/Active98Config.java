package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.Active6yearRechargeDoubleTemplate;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Config ( "Active98Config" )
public class Active98Config extends ExcleConfig{
	//计费的id，加数据
	private Map<Integer,Active6yearRechargeDoubleTemplate> rechargeDoubleMap = Maps.newConcurrentMap();

	@Override
	public void loadConfig() {
//		loadRechargeDoubleConfig();
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(Active6yearRechargeDoubleTemplate.class);
	}
	private void loadRechargeDoubleConfig() {
        List<Active6yearRechargeDoubleTemplate> list = JSONUtil.fromJson(getJson(Active6yearRechargeDoubleTemplate.class), new TypeReference<List<Active6yearRechargeDoubleTemplate>>() {});
        Map<Integer, Active6yearRechargeDoubleTemplate> tempMap = list.stream().collect(Collectors.toMap(Active6yearRechargeDoubleTemplate::getRecharge_id, Function.identity()));
        rechargeDoubleMap = ImmutableMap.copyOf(tempMap);
    }
	
	public boolean containRechargeId(int rechargeId) {
		return rechargeDoubleMap.containsKey(rechargeId);
	}
}





