/**  
 * Project Name:SLG_GameHot.
 * File Name:LanguageCnTemplateConfig.java  
 * Package Name:com.hm.config.excel  
 * Date:2018年4月2日下午6:39:18  
 * Copyright (c) 2018, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
 */


package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.LanguageCnTemplate;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**  
 * ClassName: LanguageCnTemplateConfig. <br/>  
 * Function: 公用的配置. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2018年4月2日 下午6:39:18 <br/>  
 *  
 * @author zxj  
 * @version   
 */
@Config
public class LanguageCnTemplateConfig extends ExcleConfig{
	Map<String, String> map = Maps.newHashMap();
	
	@Override
	public void loadConfig() {
		Map<String,String> tempMap = Maps.newConcurrentMap();
		for(LanguageCnTemplate temp:loadFile()){
			tempMap.put(temp.getKey(), temp.getValue());
		}
		this.map = ImmutableMap.copyOf(tempMap);
		
	}
	
	private List<LanguageCnTemplate> loadFile() {
		return JSONUtil.fromJson(getJson(LanguageCnTemplate.class), new TypeReference<ArrayList<LanguageCnTemplate>>(){});
	}
	
	public String getValue(String key) {
		String value = map.get(key);
		if(value == null) {
			return key;
		}
		return value;
	}


	@Override
	public List<String> getDownloadFile() {
		return getConfigName(LanguageCnTemplate.class);
	}
}















