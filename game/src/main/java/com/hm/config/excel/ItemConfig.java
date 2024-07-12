/**  
 * Project Name:SLG_GameFuture.  
 * File Name:ItemConfig.java  
 * Package Name:com.hm.config.excel  
 * Date:2017年12月29日上午11:39:27  
 * Copyright (c) 2017, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
*/  
  
package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.temlate.ItemTemplate;
import com.hm.enums.CommonValueType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**  
 * ClassName:ItemConfig <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason:   TODO ADD REASON. <br/>  
 * Date:     2017年12月29日 上午11:39:27 <br/>  
 * @author   zqh  
 * @version  1.1  
 * @since    
 */
@Config
public class ItemConfig extends ExcleConfig {
	Map<Integer, ItemTemplate> map = Maps.newConcurrentMap();
	
	public ItemTemplate getItemTemplateById(int itemId) {
		return map.get(itemId);
	}
	
	@Override
	public void loadConfig() {
		Map<Integer, ItemTemplate> map = Maps.newConcurrentMap();
		for(ItemTemplate template:loadFile()) {
			template.init();
			map.put(template.getId(),template);
		}
		this.map = ImmutableMap.copyOf(map);
	}
	private List<ItemTemplate> loadFile() {
		return JSONUtil.fromJson(getJson(ItemTemplate.class), new TypeReference<ArrayList<ItemTemplate>>(){});
	}
	
	@Override
	public List<String> getDownloadFile() {
		return getConfigName(ItemTemplate.class);
	}
	
	public List<ItemTemplate> getItemTemplateList() {
		return Lists.newArrayList(map.values());
	}
	
	public int getEffect(CommonValueType type) {
		CommValueConfig commonValue=  SpringUtil.getBean("CommValueConfig");
		return commonValue.getCommValue(type);
	}
}
  



