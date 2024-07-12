package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.ActiveConsumeGoldTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ClassName: ActiveConsumeGoldConfig. <br/>  
 * Function: 开采金矿活动配置. <br/>  
 * date: 2019年6月14日 上午11:16:07 <br/>  
 * @author zxj  
 * @version
 */
@Config ( "ActiveConsumeGoldConfig" )
public class ActiveConsumeGoldConfig extends ExcleConfig{
	Map<Integer, ActiveConsumeGoldTemplate> consumeGoldMap = Maps.newConcurrentMap();
	@Override
	public void loadConfig() {
//		loadActiveConsumeGold();
	}
	@Override
	public List<String> getDownloadFile() {
		return getConfigName(ActiveConsumeGoldTemplate.class);
	}
	private void loadActiveConsumeGold() {
		Map<Integer, ActiveConsumeGoldTemplate> tempConsumeGoldMap = Maps.newConcurrentMap();
		for(ActiveConsumeGoldTemplate template :JSONUtil.fromJson(getJson(ActiveConsumeGoldTemplate.class),
				new TypeReference<ArrayList<ActiveConsumeGoldTemplate>>() {
				})) {
			tempConsumeGoldMap.put(template.getIndex(), template);
		}
		this.consumeGoldMap = ImmutableMap.copyOf(tempConsumeGoldMap);
	}
	
	public ActiveConsumeGoldTemplate getConsumeGold(int id) {
		return this.consumeGoldMap.getOrDefault(id, null);
	}
}



