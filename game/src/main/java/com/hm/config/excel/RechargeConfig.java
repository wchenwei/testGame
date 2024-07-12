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
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.RechargePriceNewTemplate;
import com.hm.config.excel.templaextra.ActiveDailyRechargePointsTemplateImpl;
import com.hm.config.excel.templaextra.RechargeGiftTempImpl;
import com.hm.enums.RechargeType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: 商城配置
 * @author siyunlong  
 * @date 2018年1月9日 下午4:31:09 
 * @version V1.0
 */
@Config
public class RechargeConfig extends ExcleConfig {
	
	private Map<Integer, RechargePriceNewTemplate> rechargeMap = Maps.newHashMap();
	
	private Map<Integer, RechargeGiftTempImpl> rechargeGiftMap = Maps.newHashMap();
	//周卡 月卡
	private Map<Integer, RechargeGiftTempImpl> rechargeTypeGiftMap = Maps.newHashMap();

	// 每日必买礼包累计多少点，可领取对应的奖励
	private Map<Integer, ActiveDailyRechargePointsTemplateImpl> pointMap = Maps.newConcurrentMap();
	
	@Override
	public void loadConfig() {
		Map<Integer, RechargePriceNewTemplate> tempRechargeNew = Maps.newHashMap();
		for(RechargePriceNewTemplate template:loadRechargeNewFile()) {
			tempRechargeNew.put(template.getId(),template);
		}
		this.rechargeMap = ImmutableMap.copyOf(tempRechargeNew);
		
		Map<Integer, RechargeGiftTempImpl> tempRechargeGift = Maps.newHashMap();
		for(RechargeGiftTempImpl template:loadRechargeGiftFile()) {
			tempRechargeGift.put(template.getId(),template);
			if(RechargeType.isDayCard(template.getType())) {
				rechargeTypeGiftMap.put(template.getType(), template);
			}
		}
		this.rechargeGiftMap = ImmutableMap.copyOf(tempRechargeGift);

		Map<Integer, ActiveDailyRechargePointsTemplateImpl> tempPointMap = Maps.newHashMap();
		for(ActiveDailyRechargePointsTemplateImpl template:loadPointFile()) {
			template.init();
			tempPointMap.put(template.getId(),template);
		}
		this.pointMap = ImmutableMap.copyOf(tempPointMap);
	}

	private List<RechargePriceNewTemplate> loadRechargeNewFile() {
		return JSONUtil.fromJson(getJson(RechargePriceNewTemplate.class), new TypeReference<ArrayList<RechargePriceNewTemplate>>(){});
	}
	
	private List<RechargeGiftTempImpl> loadRechargeGiftFile() {
		return JSONUtil.fromJson(getJson(RechargeGiftTempImpl.class), new TypeReference<ArrayList<RechargeGiftTempImpl>>(){});
	}

	private List<ActiveDailyRechargePointsTemplateImpl> loadPointFile() {
		return JSONUtil.fromJson(getJson(ActiveDailyRechargePointsTemplateImpl.class), new TypeReference<ArrayList<ActiveDailyRechargePointsTemplateImpl>>(){});
	}
	@Override
	public List<String> getDownloadFile() {
		return getConfigName(RechargePriceNewTemplate.class,
				RechargeGiftTempImpl.class, ActiveDailyRechargePointsTemplateImpl.class);
	}
	/*public RechargeTemplate1 getTemplate(int id) {
		return map.get(id);
	}*/
	
	public RechargePriceNewTemplate getTemplate(int id) {
		return rechargeMap.get(id);
	}
	
	public RechargeGiftTempImpl getTemplateByType(int type) {
		return rechargeTypeGiftMap.get(type);
	}
	
	
	public RechargeGiftTempImpl getRechargeGift(int id) {
		return rechargeGiftMap.get(id);
	}
	public List<RechargeGiftTempImpl> getRechargeTemplateList() {
		return Lists.newArrayList(rechargeGiftMap.values());
	}

	public ActiveDailyRechargePointsTemplateImpl getPointCfg(int id) {
		return pointMap.getOrDefault(id, null);
	}
}
  
