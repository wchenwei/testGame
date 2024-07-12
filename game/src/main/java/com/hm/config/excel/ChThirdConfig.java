package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.templaextra.ActiveChSmShopTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 游戏cd配置
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author siyunlong  
 * @date 2018年3月7日 下午3:59:53 
 * @version V1.0
 */
@Config
public class ChThirdConfig extends ExcleConfig {
	private Map<Integer, ActiveChSmShopTemplate> map = Maps.newConcurrentMap();
	
	@Override
	public void loadConfig() {
		Map<Integer, ActiveChSmShopTemplate> map = Maps.newConcurrentMap();
		for (ActiveChSmShopTemplate template : loadFile()) {
			template.init();
			map.put(template.getId(), template);
		}
		this.map = ImmutableMap.copyOf(map);
	}
	private List<ActiveChSmShopTemplate> loadFile() {
		return JSONUtil.fromJson(getJson(ActiveChSmShopTemplate.class), new TypeReference<ArrayList<ActiveChSmShopTemplate>>(){});
	}
	
	@Override
	public List<String> getDownloadFile() {
		return getConfigName(ActiveChSmShopTemplate.class);
	}
	
	public ActiveChSmShopTemplate getActiveChSmShopTemplate(int id) {
		return map.get(id);
	} 
}
  
