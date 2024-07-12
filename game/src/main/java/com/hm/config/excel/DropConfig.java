/**  
 * Project Name:SLG_GameFuture.  
 * File Name:PlayerLeadConfig.java  
 * Package Name:com.hm.config.excel  
 * Date:2017年12月29日上午9:25:30  
 * Copyright (c) 2017, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
*/  
  
package com.hm.config.excel;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.*;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.DropTemplate;
import com.hm.config.excel.templaextra.WarModeDropTemplate;
import com.hm.enums.DropModeType;
import com.hm.model.item.Items;
import com.hm.model.player.BasePlayer;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: 掉落配置
 * @author siyunlong  
 * @date 2018年1月9日 下午4:31:09 
 * @version V1.0
 */
@Slf4j
@Config
public class DropConfig extends ExcleConfig {
	private Map<Integer, DropTemplate> map = Maps.newConcurrentMap();
	private ImmutableListMultimap<Integer, WarModeDropTemplate> warModeDropMap;
	
	@Override
	public void loadConfig() {
		loadDrop();
		loadWarModeDrop();
	}
	
	private void loadDrop() {
		Map<Integer, DropTemplate> map = Maps.newConcurrentMap();
		for(DropTemplate dropTemplate:JSONUtil.fromJson(getJson(DropTemplate.class), new TypeReference<ArrayList<DropTemplate>>(){})) {
			dropTemplate.init();
			map.put(dropTemplate.getId(),dropTemplate);
		}
		this.map = ImmutableMap.copyOf(map);
	}
	
	private void loadWarModeDrop() {
		ArrayListMultimap<Integer, WarModeDropTemplate> map = ArrayListMultimap.create();
		for(WarModeDropTemplate dropTemplate:JSONUtil.fromJson(getJson(WarModeDropTemplate.class), new TypeReference<ArrayList<WarModeDropTemplate>>(){})) {
			dropTemplate.init();
			map.put(dropTemplate.getDrop_case(),dropTemplate);
		}
		this.warModeDropMap = ImmutableListMultimap.copyOf(map);
	}
	
	@Override
	public List<String> getDownloadFile() {
		return getConfigName(DropTemplate.class,WarModeDropTemplate.class);
	}
	
	/**
	 * 计算常规战斗掉落
	 * @param player
	 * @return
	 */
	public List<Items> checkWarPlayerDrops(BasePlayer player) {
		List<WarModeDropTemplate> dropList = warModeDropMap.get(DropModeType.WarMode.getType());
		return checkPlayerDrops(player, dropList);
	}
	
	public List<WarModeDropTemplate> getActivityWarModeDropList(DropModeType dropModeType) {
		return warModeDropMap.get(dropModeType.getType());
	}
	
	/**
	 * 计算活动掉落
	 * @param player
	 * @param activityType
	 * @return
	 */
//	public List<Items> checkActivityPlayerDrops(BasePlayer player,ActivityType activityType,int version) {
//		List<WarModeDropTemplate> dropList = warModeDropMap.get(DropModeType.ActivityMode.getType())
//				.stream().filter(e -> e.isFitActivity(activityType, version)).collect(Collectors.toList());
//		return checkPlayerDrops(player, dropList);
//	}
	
	public List<Items> checkPlayerDrops(BasePlayer player,List<WarModeDropTemplate> dropList) {
		List<Items> itemList = Lists.newArrayList();
		for (WarModeDropTemplate warModeDropTemplate : dropList) {
			List<Items> tempList = warModeDropTemplate.dropItemList(player);
			if(CollUtil.isNotEmpty(tempList)) {
				itemList.addAll(tempList);
			}
		}
		return itemList;
	}
	
	public List<Items> randomItem(int dropId) {
		DropTemplate template = getDropById(dropId);
		if(template == null) {
			log.error("DropConfig掉落未找到！！！="+dropId);
			return Lists.newArrayList();
		}
		return template.randomItem();
	}
	public DropTemplate getDropById(int dropId) {
		return map.get(dropId);
	}
}
  
