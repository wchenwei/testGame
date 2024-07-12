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
import com.google.common.collect.*;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.ActiveWeekGiftTemplate;
import com.hm.config.excel.temlate.ActiveWeekRewardTemplate;
import com.hm.config.excel.templaextra.ActiveWeekShopTemplateImpl;
import com.hm.config.excel.templaextra.ActivityWeekRewardTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description: 周目标配置
 * @author xjt
 * @date 2023年6月29日10:27:40
 * @version V1.0
 */
@Config
public class ActivityWeekTargetConfig extends ExcleConfig {
	private Map<Integer, ActiveWeekGiftTemplate> weekGifts = Maps.newConcurrentMap();
	private Map<Integer, ActivityWeekRewardTemplate> rewardMaps = Maps.newConcurrentMap();
	private List<Integer> giftIds = Lists.newArrayList();
	private Map<Integer, ActiveWeekShopTemplateImpl> shopMaps = Maps.newConcurrentMap();

	@Override
	public void loadConfig() {
//		List<ActiveWeekGiftTemplate> giftList = JSONUtil.fromJson(getJson(ActiveWeekGiftTemplate.class), new TypeReference<ArrayList<ActiveWeekGiftTemplate>>(){});
//		this.weekGifts = ImmutableMap.copyOf(giftList.stream().collect(Collectors.toMap(ActiveWeekGiftTemplate::getId, Function.identity())));
//		this.giftIds = giftList.stream().map(t->t.getRecharge_gift_id()).collect(Collectors.toList());
//		List<ActivityWeekRewardTemplate> rewardList = JSONUtil.fromJson(getJson(ActivityWeekRewardTemplate.class), new TypeReference<ArrayList<ActivityWeekRewardTemplate>>(){});
//		rewardList.forEach(t->t.init());
//		this.rewardMaps = ImmutableMap.copyOf(rewardList.stream().collect(Collectors.toMap(ActivityWeekRewardTemplate::getId, Function.identity())));
//
//		List<ActiveWeekShopTemplateImpl> shopList = JSONUtil.fromJson(getJson(ActiveWeekShopTemplateImpl.class), new TypeReference<ArrayList<ActiveWeekShopTemplateImpl>>(){});
//		shopList.forEach(ActiveWeekShopTemplateImpl::init);
//		this.shopMaps = ImmutableMap.copyOf(shopList.stream().collect(Collectors.toMap(ActiveWeekShopTemplateImpl::getId, Function.identity())));
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(ActiveWeekGiftTemplate.class,ActiveWeekRewardTemplate.class, ActiveWeekShopTemplateImpl.class);
	}

	public ActiveWeekGiftTemplate getWeekGiftTemplate(int id){
		return weekGifts.get(id);
	}

	public ActivityWeekRewardTemplate getRewardTemplate(int id){
		return rewardMaps.get(id);
	}

	public List<Integer> getGiftIds(){
		return giftIds;
	}

	public ActiveWeekShopTemplateImpl getShopCfg(int id) {
		return shopMaps.get(id);
	}
}






