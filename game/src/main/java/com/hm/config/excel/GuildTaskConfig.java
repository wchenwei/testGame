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
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.action.guild.task.GuildTaskType;
import com.hm.config.excel.templaextra.GuildTaskTemplate;
import com.hm.config.excel.templaextra.GuildTaskTypeTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 部落任务
 * @author siyunlong  
 * @date 2018年12月10日 下午7:59:08 
 * @version V1.0
 */
@Config
public class GuildTaskConfig extends ExcleConfig {
	private GuildTaskTypeTemplate[] types;
	private GuildTaskTemplate[][] typeTasks;
	
	@Override
	public void loadConfig() {
		loadGuildTaskTypeTemplate();
		loadGuildTaskTemplate();
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(GuildTaskTypeTemplate.class,GuildTaskTemplate.class);
	}
	
	public void loadGuildTaskTypeTemplate() {
		List<GuildTaskTypeTemplate> list = ImmutableList.copyOf(JSONUtil.fromJson(getJson(GuildTaskTypeTemplate.class), new TypeReference<ArrayList<GuildTaskTypeTemplate>>(){}));
		int maxType = list.stream().mapToInt(e -> e.getId()).max().orElse(0);
		GuildTaskTypeTemplate[] types = new GuildTaskTypeTemplate[maxType];
		for (GuildTaskTypeTemplate guildTaskTypeTemplate : list) {
			guildTaskTypeTemplate.init();
			types[guildTaskTypeTemplate.getId()-1] = guildTaskTypeTemplate;
		}
		this.types = types;
	}
	
	public void loadGuildTaskTemplate() {
		List<GuildTaskTemplate> list = ImmutableList.copyOf(JSONUtil.fromJson(getJson(GuildTaskTemplate.class), new TypeReference<ArrayList<GuildTaskTemplate>>(){}));
		int maxType = list.stream().mapToInt(e -> e.getTask_type()).max().orElse(0);
		int maxPro = list.stream().mapToInt(e -> e.getLevel()).max().orElse(0);
		GuildTaskTemplate[][] types = new GuildTaskTemplate[maxType][maxPro];
		for (GuildTaskTemplate template : list) {
			template.init();
			types[template.getTask_type()-1][template.getLevel()-1]= template;
		}
		this.typeTasks = types;
	}
	
	public GuildTaskTypeTemplate getGuildTaskTypeTemplate(GuildTaskType taskType) {
		return this.types[taskType.getId()-1];
	}
	public GuildTaskTemplate getGuildTaskTemplate(GuildTaskType taskType,int progress) {
		return this.typeTasks[taskType.getId()-1][progress-1];
	}
	
}






