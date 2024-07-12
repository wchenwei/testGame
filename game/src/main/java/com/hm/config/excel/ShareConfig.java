package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.templaextra.ShareTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Config
public class ShareConfig extends ExcleConfig {
	private Map<Integer, ShareTemplate> map = Maps.newHashMap();

	@Override
	public void loadConfig() {
		loadMailTemplate();
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(ShareTemplate.class);
	}
	
	private void loadMailTemplate(){
		Map<Integer, ShareTemplate> adMap = Maps.newHashMap();
		for(ShareTemplate temp:JSONUtil.fromJson(getJson(ShareTemplate.class), new TypeReference<ArrayList<ShareTemplate>>(){})){
			temp.init();
			adMap.put(temp.getId(), temp);
		}
		this.map = ImmutableMap.copyOf(adMap);
	}
	public ShareTemplate getTemplate(int id) {
		return map.getOrDefault(id, null);
	}
}






