/**  
 * Project Name:SLG_GameHot.
 * File Name:MailConfig.java  
 * Package Name:com.hm.config.excel  
 * Date:2018年4月10日下午3:03:16  
 * Copyright (c) 2018, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
 */


package com.hm.config.excel;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.*;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.templaextra.RankTemplate;
import com.hm.enums.RankType;
import com.hm.model.item.Items;
import com.hm.model.serverpublic.ServerData;
import com.hm.model.serverpublic.ServerDataManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 排行奖励
 * @author siyunlong  
 * @date 2018年12月10日 下午7:59:08 
 * @version V1.0
 */
@Config
public class RankConfig extends ExcleConfig {
	private Map<Integer, List<RankReward>> rankMap = Maps.newHashMap();
	
	@Override
	public void loadConfig() {
		loadRankTemplate();
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(RankTemplate.class);
	}
	
	private void loadRankTemplate(){
		List<RankTemplate> list = ImmutableList.copyOf(JSONUtil.fromJson(getJson(RankTemplate.class), new TypeReference<ArrayList<RankTemplate>>(){}));
		Table<Integer,Integer,RankReward> rankTable = HashBasedTable.create();
		for(RankTemplate temp:list){
			temp.init();
			RankReward rankReward = rankTable.get(temp.getRank_type(),temp.getServer_lv_down());
			if(rankReward == null) {
				rankReward = new RankReward(temp.getServer_lv_down());
				rankTable.put(temp.getRank_type(),temp.getServer_lv_down(), rankReward);
			}
			rankReward.addRankTemplate(temp);
		}
		rankTable.values().forEach(e -> e.calMaxRank());
		
		Map<Integer, List<RankReward>> rankMap = Maps.newHashMap();
		for (int type : rankTable.rowKeySet()) {
			List<RankReward> tempList = rankTable.row(type).values().stream().sorted(Comparator.comparingInt(RankReward::getServerLv))
			.collect(Collectors.toList());
			rankMap.put(type, tempList);
		}
		this.rankMap = ImmutableMap.copyOf(rankMap);
	}
	
	
	public RankReward getRankReward(RankType type) {
		List<RankReward> rankList = this.rankMap.get(type.getType());
		if(CollUtil.isEmpty(rankList)) {
			return null;
		}
		return rankList.get(0);
	}
	
	//按照服务器等级获取排名奖励
	public RankReward getRankReward(RankType type,int serverId) {
		ServerData serverData = ServerDataManager.getIntance().getServerData(serverId);
		if(serverData == null) {
			return getRankRewardByServerLv(type, 1);
		}
		int serverLv = serverData.getServerStatistics().getServerLv();
		return getRankRewardByServerLv(type, serverLv);
	}
	public RankReward getRankRewardByServerLv(RankType type,int serverLv) {
		List<RankReward> rankList = this.rankMap.get(type.getType());
		if(CollUtil.isEmpty(rankList)) {
			return null;
		}
		if(rankList.size() == 1) {
			return rankList.get(0);
		}
		for (int i = rankList.size()-1; i >= 0; i--) {
			RankReward rankReward = rankList.get(i);
			if(serverLv >= rankReward.getServerLv()) {
				return rankReward;
			}
		}
		return rankList.get(0);
	}
	
	public List<Items> getRewards(RankType rankType,int rank){
		RankReward rankReward = getRankReward(rankType);
		RankTemplate template = rankReward.getRankTemplate(rank);
		if(template==null){
			return Lists.newArrayList();
		}
		return template.getRewardList();
	}
}






