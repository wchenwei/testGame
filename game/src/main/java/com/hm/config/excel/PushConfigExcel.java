/**  
 * Project Name:SLG_GameHot.
 * File Name:PushConfig.java  
 * Package Name:com.hm.config.excel  
 * Date:2018年9月29日上午9:28:55  
 * Copyright (c) 2018, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
 */


package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.action.push.util.AndroidPushUtil;
import com.hm.config.excel.temlate.PushConfigTemplate;
import com.hm.config.excel.temlate.PushMessageTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**  
 * ClassName: PushConfigExcel. <br/>  
 * Function: 推送配置类. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2018年9月29日 上午9:28:55 <br/>  
 *  
 * @author zxj  
 * @version   
 */
@Config
public class PushConfigExcel extends ExcleConfig {
	private Map<Integer, PushConfigTemplate> channelMap = Maps.newHashMap();
	private List<PushConfigTemplate> channelList = Lists.newArrayList();
	private Map<Integer, PushMessageTemplate> messageMap = Maps.newHashMap();

	@Override
	public void loadConfig() {
		loadChannel();
		loadMessage();
	}
	
	private void loadChannel() {
		Map<Integer, PushConfigTemplate> tempMap = Maps.newHashMap();
		this.channelList = loadPushConfig();
		for(PushConfigTemplate config : this.channelList) {
			tempMap.put(config.getChannelId(), config);
		}
		this.channelMap = ImmutableMap.copyOf(tempMap);
		
		AndroidPushUtil.initApp(this.channelList);
	}
	private void loadMessage() {
		Map<Integer, PushMessageTemplate> tempMap = Maps.newHashMap();
		for(PushMessageTemplate config : loadMessageConfig()) {
			tempMap.put(config.getId(), config);
		}
		this.messageMap = ImmutableMap.copyOf(tempMap);
	}
	
	private List<PushConfigTemplate> loadPushConfig() {
		return JSONUtil.fromJson(getJson(PushConfigTemplate.class), new TypeReference<ArrayList<PushConfigTemplate>>(){});
	}
	private List<PushMessageTemplate> loadMessageConfig() {
		return JSONUtil.fromJson(getJson(PushMessageTemplate.class), new TypeReference<ArrayList<PushMessageTemplate>>(){});
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(PushConfigTemplate.class,PushMessageTemplate.class);
	}

	public Map<Integer, PushConfigTemplate> getMap() {
		return channelMap;
	}
	
	public PushMessageTemplate getPushMessageTemplate(int id) {
		return this.messageMap.get(id);
	}

}


