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
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.TankRecycleTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: tank重铸
 * @author siyunlong  
 * @date 2019年9月2日 下午2:12:12 
 * @version V1.0
 */
@Config
public class TankRecasetConfig extends ExcleConfig {
	private Table<Integer, Integer,TankRecycleTemplate> tankTable = HashBasedTable.create();

	@Override
	public void loadConfig() {
		loadRecaset();
	}
	
	private void loadRecaset() {
		Table<Integer, Integer,TankRecycleTemplate> tankMap = HashBasedTable.create();
		for (TankRecycleTemplate template : loadPushConfig()) {
			tankMap.put(template.getType(), template.getLevel(), template);
		}
		this.tankTable = ImmutableTable.copyOf(tankMap);
	}
	
	private List<TankRecycleTemplate> loadPushConfig() {
		return JSONUtil.fromJson(getJson(TankRecycleTemplate.class), new TypeReference<ArrayList<TankRecycleTemplate>>(){});
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(TankRecycleTemplate.class);
	}

	public TankRecycleTemplate getTankRecycleTemplate(int type,int lv) {
		return tankTable.get(type, lv);
	}
}


