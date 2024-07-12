package com.hm.config;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.*;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.temlate.Active77GiftTemplate;
import com.hm.config.excel.templaextra.ActiveValentineMovieTemplate;
import com.hm.config.excel.templaextra.ActiveValentineRewardOnceTemplate;
import com.hm.config.excel.templaextra.ActiveValentineShopTemplate;
import com.hm.config.excel.templaextra.ActiveValentineTemplate;
import com.hm.model.item.Items;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
@Config
public class ValentineDayConfig extends ExcleConfig {
	private Table<Integer,Integer,ActiveValentineTemplate> valentineTable = HashBasedTable.create();
	private Table<Integer,Integer,ActiveValentineMovieTemplate> movieTable = HashBasedTable.create();
	private Map<Integer,ActiveValentineMovieTemplate> movieMap = Maps.newConcurrentMap();
	private Map<Integer,ActiveValentineShopTemplate> shopMap = Maps.newConcurrentMap();
	private Map<Integer,ActiveValentineRewardOnceTemplate> onceRewards = Maps.newConcurrentMap();
	private Map<Integer,Active77GiftTemplate> giftMap = Maps.newConcurrentMap();
	private Table<Integer, Integer,Active77GiftTemplate> giftIds = HashBasedTable.create();

	@Override
	public void loadConfig() {
//		loadValentineMain();
//		loadValentineMovie();
//		loadValentineShop();
//		loadOnceRewardConfig();
//		loadGiftMapConfig();
	}

	private void loadGiftMapConfig() {
		List<Active77GiftTemplate> list = JSONUtil.fromJson(getJson(Active77GiftTemplate.class), new TypeReference<List<Active77GiftTemplate>>(){});
		Map<Integer,Active77GiftTemplate> giftMap = list.stream().collect(Collectors.toMap(Active77GiftTemplate::getId, e->e));
		this.giftMap = ImmutableMap.copyOf(giftMap);
		Table<Integer,Integer,Active77GiftTemplate> giftIdsTemp = HashBasedTable.create();
		list.forEach(e->{
			giftIdsTemp.put(e.getStage(),e.getRecharge_id(), e);
		});
		this.giftIds = ImmutableTable.copyOf(giftIdsTemp);
	}

	private void loadValentineMain() {
		Table<Integer,Integer,ActiveValentineTemplate> valentineTable = HashBasedTable.create();
		for(ActiveValentineTemplate template:JSONUtil.fromJson(getJson(ActiveValentineTemplate.class), new TypeReference<List<ActiveValentineTemplate>>(){})){
			template.init();
			valentineTable.put(template.getRound(),template.getId(), template);
		}
		this.valentineTable = ImmutableTable.copyOf(valentineTable);
	}

	private void loadValentineMovie() {
		Table<Integer,Integer,ActiveValentineMovieTemplate> movieTable = HashBasedTable.create();
		Map<Integer,ActiveValentineMovieTemplate> movieMap = Maps.newConcurrentMap();
		for(ActiveValentineMovieTemplate template:JSONUtil.fromJson(getJson(ActiveValentineMovieTemplate.class), new TypeReference<List<ActiveValentineMovieTemplate>>(){})){
			template.init();
			movieMap.put(template.getId(), template);
			movieTable.put(template.getMovie(),template.getType(), template);
		}
		this.movieMap = ImmutableMap.copyOf(movieMap);
		this.movieTable = ImmutableTable.copyOf(movieTable);
	}

	private void loadValentineShop() {
		Map<Integer,ActiveValentineShopTemplate> shopMap = Maps.newConcurrentMap();
		for(ActiveValentineShopTemplate template:JSONUtil.fromJson(getJson(ActiveValentineShopTemplate.class), new TypeReference<List<ActiveValentineShopTemplate>>(){})){
			template.init();
			shopMap.put(template.getId(), template);
		}
		this.shopMap = ImmutableMap.copyOf(shopMap);
	}
	
	private void loadOnceRewardConfig() {
		Map<Integer,ActiveValentineRewardOnceTemplate> onceRewards = Maps.newConcurrentMap();
		for(ActiveValentineRewardOnceTemplate template:JSONUtil.fromJson(getJson(ActiveValentineRewardOnceTemplate.class), new TypeReference<List<ActiveValentineRewardOnceTemplate>>(){})){
			template.init();
			onceRewards.put(template.getId(), template);
		}
		this.onceRewards = ImmutableMap.copyOf(onceRewards);
	}

	@Override
	public List<String> getDownloadFile() {
		 return getConfigName(ActiveValentineTemplate.class,ActiveValentineMovieTemplate.class,ActiveValentineShopTemplate.class,ActiveValentineRewardOnceTemplate.class,Active77GiftTemplate.class);
	}
	
	public ActiveValentineTemplate getValentine(int version,int id){
		return valentineTable.get(version, id);
	}

	public int getNextId(int version, int id) {
		ActiveValentineTemplate template = valentineTable.get(version, id);
		if(template!=null){
			return template.getNext_level();
		}
		return valentineTable.values().stream().filter(t->t.getRound()==version).mapToInt(t ->t.getId()).min().getAsInt();
	}

	public ActiveValentineMovieTemplate getCostTemplate(int id,int choice,int lv) {
		Optional<ActiveValentineMovieTemplate> optional = this.movieMap.values().stream().filter(t ->t.isFit(id,choice,lv)).findFirst();
		if(optional.isPresent()){
			return optional.get();
		}
		return null;
	}
	
	public ActiveValentineShopTemplate getShopTemplate(int id) {
		return this.shopMap.get(id);
	}
	/**
	 * 获取一次性奖励(战斗,福利,幸运)
	 * @param type
	 * @param lv
	 * @return
	 */
	public List<Items> getOnceRewards(int type,int lv){
		Optional<ActiveValentineRewardOnceTemplate> optional = this.onceRewards.values().stream().filter(t ->t.isFit(type,lv)).findFirst();
		if(optional.isPresent()){
			return optional.get().getRewards();
		}
		return Lists.newArrayList();
	}
	
	public boolean contains(int version, int giftid){
		return this.giftIds.contains(version, giftid);
	}

}
