package com.hm.config.excel;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.hm.config.excel.temlate.ActiveSpring7RoadTemplate;
import com.hm.config.excel.templaextra.ActiveSpring7CircleImpl;
import com.hm.config.excel.templaextra.ActiveSpring7ProgressImpl;
import com.hm.config.excel.templaextra.ActiveSpring7RoadImpl;
import com.hm.config.excel.templaextra.ActiveSpring7ShopImpl;
import com.hm.libcore.annotation.Config;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Config
public class ActiveSpringConfig extends ExcleConfig {
	
	//七日，每天的转盘(天数，id，数据)
	private Table<Integer, Integer, ActiveSpring7CircleImpl> tableCircle = HashBasedTable.create();
	
	//七日，每天的路线（id，数据）
	private Map<Integer, ActiveSpring7RoadImpl> mapRoad = Maps.newConcurrentMap();
	//七日，每天的路线(天数，id，数据)
	private Table<Integer, Integer, ActiveSpring7RoadImpl> tableRoad = HashBasedTable.create();
	//带任务数据的road
	private List<ActiveSpring7RoadImpl> roadTaskList = Lists.newArrayList();
	//七日，每天的路线（giftid，数据）
	private Map<Integer, ActiveSpring7RoadImpl> mapGiftRoad = Maps.newConcurrentMap();
	
	//商店表（id，数据）
	private Map<Integer, ActiveSpring7ShopImpl> mapShop = Maps.newConcurrentMap();
	//商店表(类型，id，数据)
	private Table<Integer, Integer, ActiveSpring7ShopImpl> tableShop = HashBasedTable.create();
	private Map<Integer, ActiveSpring7ShopImpl> giftShop = Maps.newConcurrentMap();
	
	//每日进度奖励（id，数据）
	private Map<Integer, ActiveSpring7ProgressImpl> mapProgress = Maps.newConcurrentMap();

	@Override
	public void loadConfig() {
		/*//七日，每天的转盘
        List<ActiveSpring7CircleImpl> tempListCircle = JSONUtil.fromJson(getJson(ActiveSpring7CircleImpl.class), 
        		new TypeReference<List<ActiveSpring7CircleImpl>>() {});
        
        Table<Integer, Integer, ActiveSpring7CircleImpl> tempTableCircle = HashBasedTable.create();
        tempListCircle.forEach(e -> {
        	e.init();
        	tempTableCircle.put(e.getDay(), e.getId(), e);
        });
        tableCircle = ImmutableTable.copyOf(tempTableCircle);
        
        //七日，每天的路线
        List<ActiveSpring7RoadImpl> tempListRoad = JSONUtil.fromJson(getJson(ActiveSpring7RoadImpl.class), 
        		new TypeReference<List<ActiveSpring7RoadImpl>>() {});
        
        Map<Integer, ActiveSpring7RoadImpl> tempMapRoad = Maps.newConcurrentMap();
        Table<Integer, Integer, ActiveSpring7RoadImpl> tempTableRoad = HashBasedTable.create();
        List<ActiveSpring7RoadImpl> tempRoadTaskList = Lists.newArrayList();
        Map<Integer, ActiveSpring7RoadImpl> tempMapGiftRoad = Maps.newConcurrentMap();
        tempListRoad.forEach(e -> {
        	e.init();
        	tempMapRoad.put(e.getId(), e);
        	tempTableRoad.put(e.getDay(), e.getId(), e);
        	if(e.getShop_type()==0) {
        		tempRoadTaskList.add(e);
        	}
        	if(e.getType()==9) {
        		tempMapGiftRoad.put(e.getShop_type(), e);
        	}
        });
        mapRoad = ImmutableMap.copyOf(tempMapRoad);
        tableRoad = ImmutableTable.copyOf(tempTableRoad);
        roadTaskList = ImmutableList.copyOf(tempRoadTaskList);
        mapGiftRoad = ImmutableMap.copyOf(tempMapGiftRoad);
        
        //商店表
        List<ActiveSpring7ShopImpl> tempListShop = JSONUtil.fromJson(getJson(ActiveSpring7ShopImpl.class), 
        		new TypeReference<List<ActiveSpring7ShopImpl>>() {});
        
        Map<Integer, ActiveSpring7ShopImpl> tempMapShop = Maps.newConcurrentMap();
        Table<Integer, Integer, ActiveSpring7ShopImpl> tempTableShop = HashBasedTable.create();
        Map<Integer, ActiveSpring7ShopImpl> tempMapGiftShop = Maps.newConcurrentMap();
        tempListShop.forEach(e -> {
        	e.init();
        	tempMapShop.put(e.getId(), e);
        	tempTableShop.put(e.getType(), e.getId(), e);
        	if(e.getRecharge_id()>0) {
        		tempMapGiftShop.put(e.getRecharge_id(), e);
        	}
        });
        mapShop = ImmutableMap.copyOf(tempMapShop);
        tableShop = ImmutableTable.copyOf(tempTableShop);
        giftShop = ImmutableMap.copyOf(tempMapGiftShop);
        
        
        //每日进度奖励
        List<ActiveSpring7ProgressImpl> tempListProgress = JSONUtil.fromJson(getJson(ActiveSpring7ProgressImpl.class), 
        		new TypeReference<List<ActiveSpring7ProgressImpl>>() {});
        
        Map<Integer, ActiveSpring7ProgressImpl> tempMapProgress = Maps.newConcurrentMap();
        tempListProgress.forEach(e -> {
        	e.init();
        	tempMapProgress.put(e.getId(), e);
        });
        mapProgress = ImmutableMap.copyOf(tempMapProgress);*/
	}
	@Override
	public List<String> getDownloadFile() {
		return getConfigName(ActiveSpring7CircleImpl.class, 
				ActiveSpring7RoadImpl.class,
				ActiveSpring7ShopImpl.class,
				ActiveSpring7ProgressImpl.class);
	}

	public ActiveSpring7RoadImpl getMapRoadById(int id) {
		return mapRoad.get(id);
	}
	
	//获取比当前路径小的id列表
	public List<Integer> getRoadMapList(int days, int id) {
		return tableRoad.row(days).keySet().stream().filter(e->e<id).collect(Collectors.toList());
	}
	
	public ActiveSpring7ShopImpl getShop(int type, int lv) {
		List<ActiveSpring7ShopImpl> data = this.getShopList(type, lv);
		if(!CollectionUtil.isEmpty(data)) {
			return data.get(0);
		}
		return null;
	}
	public List<ActiveSpring7ShopImpl> getShopList(int type, int lv) {
		Map<Integer, ActiveSpring7ShopImpl> shopMap = tableShop.row(type);
		List<ActiveSpring7ShopImpl> data = shopMap.values().stream().filter(e->{
			return lv>=e.getPlayer_lv_down() && lv<=e.getPlayer_lv_up();
		}).collect(Collectors.toList());
		if(!CollectionUtil.isEmpty(data)) {
			return data;
		}
		return null;
	}
	public ActiveSpring7ShopImpl getShopById(int shopId) {
		return mapShop.get(shopId);
	}
	
	public ActiveSpring7ProgressImpl getMapProgressById(int id, int lv) {
		ActiveSpring7ProgressImpl progress = mapProgress.get(id);
		if(lv>=progress.getPlayer_lv_down() && lv<=progress.getPlayer_lv_up()) {
			return progress;
		}
		return null;
	}
	

	public int getMaxProgress(int days) {
		return tableRoad.row(days).keySet().stream().mapToInt(e->e).max().orElse(-1);
	}
	public ActiveSpring7RoadTemplate getRoadByTaskId(int taskId) {
		List<ActiveSpring7RoadTemplate> list = roadTaskList.stream().filter(e->{
			return e.isContains(taskId);
		}).collect(Collectors.toList());
		if(CollectionUtil.isEmpty(list)) {
			return null;
		}
		return list.get(0);
	}
	
	public ActiveSpring7ShopImpl getShopIdByGiftId(int giftId) {
		return giftShop.get(giftId);
	}
	
	public ActiveSpring7RoadImpl getRoadByShopType(int shoptype) {
		return mapGiftRoad.get(shoptype);
	}
}







