package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.CdTemplate;

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
public class GameCdConfig extends ExcleConfig {
	private Map<Integer, CdTemplate> map = Maps.newConcurrentMap();
	
	@Override
	public void loadConfig() {
		Map<Integer, CdTemplate> map = Maps.newConcurrentMap();
		for (CdTemplate template : loadFile()) {
			map.put(template.getId(), template);
		}
		this.map = ImmutableMap.copyOf(map);
	}
	private List<CdTemplate> loadFile() {
		return JSONUtil.fromJson(getJson(CdTemplate.class), new TypeReference<ArrayList<CdTemplate>>(){});
	}
	
	@Override
	public List<String> getDownloadFile() {
		return getConfigName(CdTemplate.class);
	}
	
	public CdTemplate getCdTemplate(int type) {
		return map.get(type);
	} 
}
  
