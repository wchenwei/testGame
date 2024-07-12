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
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Table;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.templaextra.ActivityHonorRankTemplate;
import com.hm.config.excel.templaextra.HonorRankReward;
import com.hm.enums.RankType;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 排行奖励
 * @author siyunlong  
 * @date 2018年12月10日 下午7:59:08 
 * @version V1.0
 */
@Config
public class ActivityHonorRankConfig extends ExcleConfig {
	private Table<Integer, Integer, HonorRankReward> rankTable = HashBasedTable.create();
	
	@Override
	public void loadConfig() {
//		loadActivityHonorRankTemplate();
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(ActivityHonorRankTemplate.class);
	}
	
	private void loadActivityHonorRankTemplate(){
		List<ActivityHonorRankTemplate> list = ImmutableList.copyOf(JSONUtil.fromJson(getJson(ActivityHonorRankTemplate.class), new TypeReference<ArrayList<ActivityHonorRankTemplate>>(){}));
		Table<Integer, Integer, HonorRankReward> rankTable = HashBasedTable.create();
		for(ActivityHonorRankTemplate temp:list){
			temp.init();
			HonorRankReward HonorRankReward = rankTable.get(temp.getRank_type(),temp.getStage());
			if(HonorRankReward == null) {
				HonorRankReward = new HonorRankReward();
				rankTable.put(temp.getRank_type(),temp.getStage(), HonorRankReward);
			}
			HonorRankReward.addRankTemplate(temp);
		}
		rankTable.values().forEach(e -> e.calMaxRank());
		this.rankTable = rankTable;
	}
	
	
	public HonorRankReward getHonorRankReward(RankType type,int stage) {
		return this.rankTable.get(type.getType(),stage);
	}
}






