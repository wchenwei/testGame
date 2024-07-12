package com.hm.config;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.temlate.PlayerHeadTemplate;
import com.hm.config.excel.templaextra.PlayerHeadExtraTemplate;
import com.hm.model.item.Items;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Slf4j
@Config
public class PlayerHeadConfig extends ExcleConfig{
	private Map<Integer, PlayerHeadExtraTemplate> headMap = Maps.newHashMap(); 

	@Override
	public void loadConfig() {
		loadHeadConfig();
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(PlayerHeadTemplate.class);
	}
	private void loadHeadConfig(){
		Map<Integer, PlayerHeadExtraTemplate> headMap = Maps.newHashMap();
		List<PlayerHeadExtraTemplate> templateList = JSONUtil.fromJson(getJson(PlayerHeadExtraTemplate.class), new TypeReference<ArrayList<PlayerHeadExtraTemplate>>(){});
		for(PlayerHeadExtraTemplate template:templateList){
			template.init();
			headMap.put(template.getId(),template);
		}
		this.headMap = ImmutableMap.copyOf(headMap);
		log.info("加载头像配置完成");
	}
	
	public PlayerHeadExtraTemplate getHead(int id){
		return this.headMap.get(id);
	}
	//获取解锁头像的花费
	public Items getUnlockHeadCost(int id){
		PlayerHeadExtraTemplate template = getHead(id);
		if(template==null){
			return null;
		}
		return template.getCostItem();
	}
}
