package com.hm.config;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.*;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.temlate.ActiveOneyearCakeTemplate;
import com.hm.config.excel.temlate.ActiveOneyearGiftTemplate;
import com.hm.config.excel.templaextra.*;
import com.hm.model.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 周年庆配置文件
 * @author xjt
 * @Date 2020年3月4日09:57:31
 */
@Config
public class AnniversaryConfig extends ExcleConfig{
	private Map<Integer,AnniversarySignTemplate> signMap = Maps.newConcurrentMap();
	private Map<Integer,AnniversaryRewardKfTemplate> kfRewardMap = Maps.newConcurrentMap();
	private Table<Integer,Integer,AnniversaryCakeTemplate> cakeTable = HashBasedTable.create();
	private Map<Integer,ActiveOneyearGiftTemplate> giftMap = Maps.newConcurrentMap();
	private List<Integer> giftIds = Lists.newArrayList();
	private Map<Integer,AnniversaryRewardTemplate> cakeRewardMap = Maps.newConcurrentMap();//提交材料的奖励
	private Map<Integer,AnniversaryBoxTemplate> boxs = Maps.newConcurrentMap();
	private Map<Integer,AnniversaryRewardFireworkTemplate> fires = Maps.newConcurrentMap();
	@Override
	public void loadConfig() {
//		loadSignConfig();
//		loadKfRewardConfig();
//		loadCakeConfig();
//		loadGiftConfig();
//		loadRewardConfig();
//		loadBoxConfig();
//		loadFireworkRewardConfig();
		
	}
	
	private void loadFireworkRewardConfig() {
		List<AnniversaryRewardFireworkTemplate> list = JSONUtil.fromJson(getJson(AnniversaryRewardFireworkTemplate.class), new TypeReference<ArrayList<AnniversaryRewardFireworkTemplate>>(){});
		list.forEach(t -> t.init());
		Map<Integer,AnniversaryRewardFireworkTemplate> fires = list.stream().collect(Collectors.toMap(AnniversaryRewardFireworkTemplate::getId, Function.identity()));
		this.fires = ImmutableMap.copyOf(fires);
	}

	private void loadBoxConfig() {
		List<AnniversaryBoxTemplate> list = JSONUtil.fromJson(getJson(AnniversaryBoxTemplate.class), new TypeReference<ArrayList<AnniversaryBoxTemplate>>(){});
		list.forEach(t -> t.init());
		Map<Integer,AnniversaryBoxTemplate> boxs = list.stream().collect(Collectors.toMap(AnniversaryBoxTemplate::getId, Function.identity()));
		this.boxs = ImmutableMap.copyOf(boxs);
	}

	private void loadRewardConfig() {
		List<AnniversaryRewardTemplate> list = JSONUtil.fromJson(getJson(AnniversaryRewardTemplate.class), new TypeReference<ArrayList<AnniversaryRewardTemplate>>(){});
		list.forEach(t -> t.init());
		Map<Integer,AnniversaryRewardTemplate> rewardMap = list.stream().collect(Collectors.toMap(AnniversaryRewardTemplate::getId, Function.identity()));
		this.cakeRewardMap = ImmutableMap.copyOf(rewardMap);
		
	}

	private void loadGiftConfig() {
		List<ActiveOneyearGiftTemplate> list = JSONUtil.fromJson(getJson(ActiveOneyearGiftTemplate.class), new TypeReference<ArrayList<ActiveOneyearGiftTemplate>>(){});
		//list.forEach(t -> t.init());
		Map<Integer,ActiveOneyearGiftTemplate> giftMap = list.stream().collect(Collectors.toMap(ActiveOneyearGiftTemplate::getId, Function.identity()));
		this.giftIds = ImmutableList.copyOf(list.stream().map(t ->t.getRecharge_id()).distinct().collect(Collectors.toList()));
		this.giftMap = ImmutableMap.copyOf(giftMap);
	}

	private void loadCakeConfig() {
		Table<Integer,Integer,AnniversaryCakeTemplate> cakeMap = HashBasedTable.create();
		List<AnniversaryCakeTemplate> list = JSONUtil.fromJson(getJson(AnniversaryCakeTemplate.class), new TypeReference<ArrayList<AnniversaryCakeTemplate>>(){});
		for(AnniversaryCakeTemplate template:list){
			template.init();
			cakeTable.put(template.getLevel(), template.getLocation(), template);
		}
		this.cakeTable = ImmutableTable.copyOf(cakeTable);
	}

	private void loadKfRewardConfig() {
		List<AnniversaryRewardKfTemplate> list = JSONUtil.fromJson(getJson(AnniversaryRewardKfTemplate.class), new TypeReference<ArrayList<AnniversaryRewardKfTemplate>>(){});
		list.forEach(t -> t.init());
		Map<Integer,AnniversaryRewardKfTemplate> kfRewardMap = list.stream().collect(Collectors.toMap(AnniversaryRewardKfTemplate::getId, Function.identity()));
		this.kfRewardMap = ImmutableMap.copyOf(kfRewardMap);
	}

	private void loadSignConfig() {
		List<AnniversarySignTemplate> list = JSONUtil.fromJson(getJson(AnniversarySignTemplate.class), new TypeReference<ArrayList<AnniversarySignTemplate>>(){});
		list.forEach(t -> t.init());
		Map<Integer,AnniversarySignTemplate> signMap = list.stream().collect(Collectors.toMap(AnniversarySignTemplate::getId, Function.identity()));
		this.signMap = ImmutableMap.copyOf(signMap);
	}

	

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(AnniversarySignTemplate.class,AnniversaryRewardKfTemplate.class
				,ActiveOneyearCakeTemplate.class,ActiveOneyearGiftTemplate.class,AnniversaryRewardTemplate.class
				,AnniversaryBoxTemplate.class,AnniversaryRewardFireworkTemplate.class);
	}
	
	public AnniversarySignTemplate getSign(int id){
		return signMap.get(id);
	}
	
	public List<Integer> getGifts(){
		return giftIds;
	}
	//根据玩家等级和天数找出对应的template
	public AnniversarySignTemplate getSign(int playerLv, int day) {
		Optional<AnniversarySignTemplate> optional = signMap.values().stream().filter(t ->playerLv>=t.getLv_down()&&playerLv<=t.getLv_up()&&day==t.getDay()).findFirst();
		if(optional.isPresent()){
			return optional.get();
		}
		return null;
	}
	
	//获取任务进度箱子
	public AnniversaryBoxTemplate getBox(int id){
		return this.boxs.get(id);
	}
	
	//获取任务进度箱子
	public AnniversaryCakeTemplate getCake(int level,int index){
		return this.cakeTable.get(level,index);
	}
	
	//获取蛋糕层
	public AnniversaryRewardTemplate getCakeLayerTemplate(int layer,int playerLv){
		Optional<AnniversaryRewardTemplate> optional =  this.cakeRewardMap.values().stream().filter(t ->t.isFit(layer,playerLv)).findFirst();
		if(optional.isPresent()){
			return optional.get();
		}
		return null;
	}
	
	//获取蛋糕层
	public AnniversaryRewardTemplate getCakeLayerTemplate(int id){
		return cakeRewardMap.get(id);
	}

	public AnniversaryRewardKfTemplate getKfBox(int id) {
		return kfRewardMap.get(id);
	}

	public List<Items> getFireRewards(int playerLv) {
		Optional<AnniversaryRewardFireworkTemplate> optional =  this.fires.values().stream().filter(t ->t.isFit(playerLv)).findFirst();
		if(optional.isPresent()){
			return optional.get().getRewards();
		}
		return Lists.newArrayList();
	}
	
	
	
}
