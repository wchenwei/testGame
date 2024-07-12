package com.hm.config.excel;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.*;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.action.activity.ActivityBiz;
import com.hm.config.CityConfig;
import com.hm.config.GameConstants;
import com.hm.config.excel.templaextra.SupplyTaskSetting;
import com.hm.config.excel.templaextra.SupplyTemplate;
import com.hm.enums.ActivityType;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import com.hm.model.player.SupplyItem;
import com.hm.util.RandomUtils;
import com.hm.util.WeightMeta;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 补给掠夺配置
 * @author siyunlong  
 * @date 2019年1月24日 下午3:42:56 
 * @version V1.0
 */
@Config
public class SupplyConfig extends ExcleConfig {
	@Resource
	private CityConfig cityConfig;
	@Resource
    private ActivityBiz activityBiz;
	
//	private List<SupplyTemplate> supplyTemplateList;
	private ListMultimap<Integer, SupplyTemplate> activitySupplyMap = ArrayListMultimap.create();
	 
	@Override
	public void loadConfig() {
		loadSupplyTemplate();
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(SupplyTemplate.class);
	}
	
	private void loadSupplyTemplate(){
		ListMultimap<Integer, SupplyTemplate> activitySupplyMap = ArrayListMultimap.create();
		List<SupplyTemplate> list = ImmutableList.copyOf(JSONUtil.fromJson(getJson(SupplyTemplate.class), new TypeReference<ArrayList<SupplyTemplate>>(){}));
		list.forEach(e -> e.init());
		list.forEach(e -> activitySupplyMap.put(e.getSub_type(), e));
//		this.supplyTemplateList = ImmutableList.copyOf(list);
		this.activitySupplyMap = ImmutableListMultimap.copyOf(activitySupplyMap);
	}
	
	public Map<SupplyTemplate,Integer> getSupplyTemplateMap(Player player,boolean isNpc) {
		int playerLv = player.playerLevel().getLv();
		Map<SupplyTemplate,Integer> resultMap = Maps.newHashMap();
		for (int activityId : activitySupplyMap.keySet()) {
			if(acitvityIsOpen(player, activityId)) {
				for (SupplyTemplate supplyTemplate : activitySupplyMap.get(activityId)) {
					if(supplyTemplate.isFit(playerLv)) {
						if(isNpc) {
							if(supplyTemplate.isFitNpc()) {
								resultMap.put(supplyTemplate, supplyTemplate.getRate());
							}
						}else{
							resultMap.put(supplyTemplate, supplyTemplate.getRate());
						}
					}
				}
			}
		}
		return resultMap;
	}
    /**
     * 检查活动任务是否开启
     * @param player
     * @param activityId
     * @return
     */
    public boolean acitvityIsOpen(BasePlayer player,int activityId) {
    	if(activityId <= 0) {
    		return true;
    	}
    	ActivityType activityType = ActivityType.getActivityType(activityId);
    	if(activityType == null) {
    		return false;
    	}
    	return activityBiz.checkActivityIsOpen(player, activityType);
    }
	
	public List<SupplyItem> randomSupplyItem(Player player,int size) {
		Map<SupplyTemplate,Integer> randomMap = getSupplyTemplateMap(player, false);
		//已经随机到的城市 
		List<Integer> curCitys = player.playerRobSupply().getSupplyList().stream().map(e -> e.getCityId()).collect(Collectors.toList());
		
		WeightMeta<SupplyTemplate> weightMeta = RandomUtils.buildWeightMeta(randomMap);
		List<SupplyItem> supplyItemList = Lists.newArrayList();
		for (int i = 0; i < size; i++) {
			SupplyTaskSetting supplyTaskSetting = weightMeta.random().randomSupplyTaskSetting();
			SupplyItem supplyItem = new SupplyItem();
			supplyItem.setItem(supplyTaskSetting.buildItems());
			supplyItem.setStar(supplyTaskSetting.getStar());
			List<Integer> tempList = cityConfig.getBerlinCityList(supplyTaskSetting.getWaySize());
			List<Integer> randomList = Lists.newArrayList(tempList);
			randomList.removeAll(curCitys);
			if(CollUtil.isEmpty(randomList)) {
				randomList = tempList;
			}
			int luckCity = RandomUtils.randomEle(randomList);
			supplyItem.setCityId(luckCity);
			supplyItem.setWayList(cityConfig.getShotWay(GameConstants.PeaceId, luckCity));
			supplyItem.loadPlayer(player);
			supplyItemList.add(supplyItem);
			curCitys.add(luckCity);
		}
		return supplyItemList;
	}
	
	public List<SupplyItem> randomSupplyNpcItem(Player player,int size) {
		Map<SupplyTemplate,Integer> randomMap = getSupplyTemplateMap(player, true);
		
		WeightMeta<SupplyTemplate> weightMeta = RandomUtils.buildWeightMeta(randomMap);
		List<SupplyItem> supplyItemList = Lists.newArrayList();
		for (int i = 0; i < size; i++) {
			SupplyTaskSetting supplyTaskSetting = weightMeta.random().randomNpcTaskSetting();
			SupplyItem supplyItem = new SupplyItem();
			supplyItem.setItem(supplyTaskSetting.buildItems());
			supplyItem.setStar(supplyTaskSetting.getStar());
			supplyItemList.add(supplyItem);
		}
		return supplyItemList;
	}
	
}






