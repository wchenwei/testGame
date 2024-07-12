/**  
 * Project Name:SLG_GameFuture.  
 * File Name:PlayerLeadConfig.java  
 * Package Name:com.hm.config.excel  
 * Date:2017年12月29日上午9:25:30  
 * Copyright (c) 2017, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
*/  
  
package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.MoneyTypeTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Description: 物品名
 * @author siyunlong  
 * @date 2018年1月9日 下午4:31:09 
 * @version V1.0
 */
@Config
public class ItemNameConfig extends ExcleConfig {
	private List<MoneyTypeTemplate> moneyTypeList = Lists.newArrayList();
	
	
	@Override
	public void loadConfig() {
		moneyTypeList = ImmutableList.copyOf(loadFile());
	}
	private List<MoneyTypeTemplate> loadFile() {
		return JSONUtil.fromJson(getJson(MoneyTypeTemplate.class), new TypeReference<ArrayList<MoneyTypeTemplate>>(){});
	}
	
	@Override
	public List<String> getDownloadFile() {
		return getConfigName(MoneyTypeTemplate.class);
	}
	public List<MoneyTypeTemplate> getMoneyTypeList() {
		return moneyTypeList;
	}
	public MoneyTypeTemplate getMoneyType(int id) {
		Optional<MoneyTypeTemplate> template = moneyTypeList.stream().filter(t ->t.getId()==id).findFirst();
		if(template==null){
			return null;
		}
		return template.get();
	}
	
	
}
  
