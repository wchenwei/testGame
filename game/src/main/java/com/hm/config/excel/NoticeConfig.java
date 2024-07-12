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
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.templaextra.NoticeTemplate;
import com.hm.observer.ObservableEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Config
public class NoticeConfig extends ExcleConfig {
	private Map<Integer, NoticeTemplate> noticeMap = Maps.newHashMap();
	

	@Override
	public void loadConfig() {
		loadNoticeTemplate();
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(NoticeTemplate.class);
	}
	
	private void loadNoticeTemplate(){
		List<NoticeTemplate> list = ImmutableList.copyOf(JSONUtil.fromJson(getJson(NoticeTemplate.class), new TypeReference<ArrayList<NoticeTemplate>>(){}));
		Map<Integer, NoticeTemplate> techRewardMap = Maps.newConcurrentMap();
		for(NoticeTemplate temp:list){
			temp.init();
			techRewardMap.put(temp.getObId(), temp);
		}
		this.noticeMap = ImmutableMap.copyOf(techRewardMap);
	}
	
	
	public NoticeTemplate getNoticeTemplate(ObservableEnum observableEnum){
		return noticeMap.get(observableEnum.getEnumId());
	}

	public List<ObservableEnum> getAllObservableEnum() {
		return this.noticeMap.keySet().stream().map(e -> ObservableEnum.getObservableEnum(e))
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}
}






