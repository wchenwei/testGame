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
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.KfSeasonRankTemplate;
import com.hm.config.excel.templaextra.KfLevelRewardSeasonTemplate;
import com.hm.config.excel.templaextra.KfPeaceShopTemplateImpl;
import com.hm.config.excel.templaextra.KfPlayerTaskTemplate;
import com.hm.config.excel.templaextra.KfSeasonShopTemplateImpl;
import com.hm.model.item.Items;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description: 跨服配置
 * @author siyunlong  
 * @date 2018年12月10日 下午7:59:08 
 * @version V1.0
 */
@Config
public class KfConfig extends ExcleConfig {
	private KfLevelRewardSeasonTemplate[][] seasonRewards = new KfLevelRewardSeasonTemplate[1][1];
	private Map<Integer,KfSeasonShopTemplateImpl> shopMap = Maps.newConcurrentMap();
	private Set<Integer> giftIds = new HashSet<Integer>();
	private int maxSeason;
    private Map<Integer, KfPlayerTaskTemplate> kftaskMap = Maps.newHashMap();

	private Map<Integer, KfPeaceShopTemplateImpl> kwWorldWarShopMap = Maps.newConcurrentMap();
	private Set<Integer> kwWorldWarGiftIds = new HashSet<Integer>();
	//赛季奖励
	private KFSeasonServerReward[] seasonServerRewards = new KFSeasonServerReward[1];
	
	@Override
	public void loadConfig() {
//		loadSeasonTemplate();
//		loadSeasonServerRewards();
//		loadShopConfig();
//		loadKfTaskConfig();
//		loadKwWorldWarShopConfig();
	}

	private void loadShopConfig() {
		List<KfSeasonShopTemplateImpl> templates = JSONUtil.fromJson(getJson(KfSeasonShopTemplateImpl.class), new TypeReference<ArrayList<KfSeasonShopTemplateImpl>>(){});
		templates.forEach(t ->t.init());
		this.shopMap = templates.stream().collect(Collectors.toMap(KfSeasonShopTemplateImpl::getId, Function.identity()));
		this.giftIds = templates.stream().filter(t ->t.getRecharge_gift()>0).map(t ->t.getRecharge_gift()).collect(Collectors.toSet());
	}

    private void loadKfTaskConfig() {
        List<KfPlayerTaskTemplate> templates = JSONUtil.fromJson(getJson(KfPlayerTaskTemplate.class), new TypeReference<ArrayList<KfPlayerTaskTemplate>>() {
        });
//		templates.forEach(t ->t.init());
        this.kftaskMap = templates.stream().collect(Collectors.toMap(KfPlayerTaskTemplate::getTask_id, Function.identity()));
    }
	private void loadKwWorldWarShopConfig() {
		List<KfPeaceShopTemplateImpl> templates = JSONUtil.fromJson(getJson(KfPeaceShopTemplateImpl.class), new TypeReference<ArrayList<KfPeaceShopTemplateImpl>>(){});
		templates.forEach(t ->t.init());
		this.kwWorldWarShopMap = templates.stream().collect(Collectors.toMap(KfPeaceShopTemplateImpl::getId, Function.identity()));
		this.kwWorldWarGiftIds = templates.stream().filter(t ->t.getRecharge_gift()>0).map(t ->t.getRecharge_gift()).collect(Collectors.toSet());
	}

	@Override
	public List<String> getDownloadFile() {
        return getConfigName(KfLevelRewardSeasonTemplate.class, KfSeasonRankTemplate.class,
				KfSeasonShopTemplateImpl.class, KfPlayerTaskTemplate.class, KfPeaceShopTemplateImpl.class);
	}
	
	
	private void loadSeasonTemplate(){
		List<KfLevelRewardSeasonTemplate> list = ImmutableList.copyOf(JSONUtil.fromJson(getJson(KfLevelRewardSeasonTemplate.class), new TypeReference<ArrayList<KfLevelRewardSeasonTemplate>>(){}));
		this.maxSeason = list.stream().mapToInt(e -> e.getSeason()).max().orElse(0);
		int maxtype = list.stream().mapToInt(e -> e.getStage()).max().orElse(0);
		KfLevelRewardSeasonTemplate[][] seasonRewards = new KfLevelRewardSeasonTemplate[maxSeason][maxtype];
		for (KfLevelRewardSeasonTemplate temp : list) {
			temp.init();
			seasonRewards[temp.getSeason()-1][temp.getStage()-1] =  temp;
		}
		this.seasonRewards = seasonRewards;
	}
	
	private void loadSeasonServerRewards(){
		List<KfSeasonRankTemplate> list = JSONUtil.fromJson(getJson(KfSeasonRankTemplate.class), new TypeReference<ArrayList<KfSeasonRankTemplate>>(){});
		int maxSeason = list.stream().mapToInt(e -> e.getStage()).max().orElse(0);
		KFSeasonServerReward[] seasonServerRewards = new KFSeasonServerReward[maxSeason];
		for (KfSeasonRankTemplate template : list) {
			KFSeasonServerReward reward = seasonServerRewards[template.getStage()-1];
			if(reward == null) {
				reward = new KFSeasonServerReward();
				seasonServerRewards[template.getStage()-1] = reward;
			}
			reward.addTemplate(template);
		}
		this.seasonServerRewards = seasonServerRewards;
	}
	
	
	public List<Items> getKfLevelRewardSeason(int snum,int type) {
		int index = (snum-1)%maxSeason;
		KfLevelRewardSeasonTemplate template = this.seasonRewards[index][type-1];
		return template.getRewardList();
	}
	
	/**
	 * 获取服务器赛季奖励
	 * @param seasonId
	 * @param rank
	 * @return
	 */
	public List<Items> getKfSeasonServerReward(int seasonId,int rank) {
		int index = (seasonId-1)%this.seasonServerRewards.length;
		KFSeasonServerReward serverReward = this.seasonServerRewards[index];
		return serverReward.getItemList(rank);
	}
	
	public KfSeasonShopTemplateImpl getShopTemplate(int id) {
		return shopMap.get(id);
	}

	public int getGoodsId(int serverLv,int type, int rechargeId) {
		KfSeasonShopTemplateImpl template = shopMap.values().stream().filter(t ->t.isFit(serverLv,type)&&t.getRecharge_gift()==rechargeId).findFirst().orElse(null);
		if(template==null) {
			return -1;
		}
		return template.getId();
	}
	
	public boolean isShopGiftId(int giftId) {
		return giftIds.contains(giftId);
	}

	public boolean isKfWorldWarShopGiftId(int giftId) {
		return kwWorldWarGiftIds.contains(giftId);
	}
	public KfPeaceShopTemplateImpl getKwWorldWarShopTemplate(int id) {
		return kwWorldWarShopMap.get(id);
	}
	public int getKwWorldWarGoodsId(int serverLv, int rechargeId) {
		KfPeaceShopTemplateImpl template = kwWorldWarShopMap.values().stream().filter(t ->t.isFit(serverLv)&&t.getRecharge_gift()==rechargeId).findFirst().orElse(null);
		if(template==null) {
			return -1;
		}
		return template.getId();
	}
    public KfPlayerTaskTemplate getKfPlayerTaskTemplate(int taskId) {
        return this.kftaskMap.get(taskId);
    }
}






