/**  
 * Project Name:SLG_GameHot.
 * File Name:MailConfig.java  
 * Package Name:com.hm.config.excel  
 * Date:2018年4月10日下午3:03:16  
 * Copyright (c) 2018, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
 */


package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.hm.config.excel.temlate.ActiveSmashEggShowTemplate;
import com.hm.config.excel.templaextra.ActiveJzEggTemplate;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.model.item.Items;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: 排行奖励
 * @author siyunlong  
 * @date 2018年12月10日 下午7:59:08 
 * @version V1.0
 */
@Config
public class ActiveJzEggsConfig extends ExcleConfig {
	@Resource
	private LanguageCnTemplateConfig langeConfig;
	
	private ActiveJzEggTemplate[] templates = null;
	private Map<Integer, ActiveSmashEggShowTemplate> itemMap = Maps.newConcurrentMap();
	
	@Override
	public void loadConfig() {
//		loadActiveJzEggTemplate();
//		loadItemTemplate();
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(ActiveJzEggTemplate.class,ActiveSmashEggShowTemplate.class);
	}
	
	private void loadActiveJzEggTemplate(){
		List<ActiveJzEggTemplate> list = ImmutableList.copyOf(JSONUtil.fromJson(getJson(ActiveJzEggTemplate.class), new TypeReference<ArrayList<ActiveJzEggTemplate>>(){}));
		ActiveJzEggTemplate[] templates = new ActiveJzEggTemplate[list.size()];
		for(ActiveJzEggTemplate temp:list){
			temp.init();
			templates[temp.getActive_id()-1] = temp;
		}
		this.templates = templates;
	}
	
	private void loadItemTemplate(){
		List<ActiveSmashEggShowTemplate> list = ImmutableList.copyOf(JSONUtil.fromJson(getJson(ActiveSmashEggShowTemplate.class), new TypeReference<ArrayList<ActiveSmashEggShowTemplate>>(){}));
		for(ActiveSmashEggShowTemplate temp:list){
			this.itemMap.put(temp.getId(), temp);
		}
	}
	
	public ActiveJzEggTemplate getActiveJzEggTemplate(int day) {
		return templates[day-1];
	}
	
	public Items randomRandom(int day) {
		ActiveJzEggTemplate template = getActiveJzEggTemplate(day);
		return template.getRandomItems().random();
	}

	
	public String getTrueItemName(int itemId) {
		ActiveSmashEggShowTemplate temp = this.itemMap.get(itemId);
		if(temp == null) {
			return "未知";
		}
		return langeConfig.getValue(temp.getTitle());
	}
}






