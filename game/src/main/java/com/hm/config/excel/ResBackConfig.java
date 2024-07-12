package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.libcore.spring.SpringUtil;
import com.hm.action.item.ItemBiz;
import com.hm.config.excel.templaextra.ResBackExpeditionTemplate;
import com.hm.config.excel.templaextra.ResBackFlashTemplate;
import com.hm.config.excel.templaextra.ResBackTemplate;
import com.hm.enums.ResBackType;
import com.hm.model.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
@Config
public class ResBackConfig extends ExcleConfig {
	private Map<Integer,ResBackTemplate> resBackMap = Maps.newConcurrentMap();
	private Map<Integer,ResBackExpeditionTemplate> expeditionMap = Maps.newConcurrentMap();
	private Map<Integer,ResBackFlashTemplate> flashMap = Maps.newConcurrentMap();
	@Override
	public void loadConfig() {
		loadResBackConfig();
		loadResBackExpeditionConfig();
		loadResBackFlashConfig();
	}
	
	public void loadResBackConfig(){
		Map<Integer,ResBackTemplate> resBackMap = Maps.newConcurrentMap();
		List<ResBackTemplate> templateList = JSONUtil.fromJson(getJson(ResBackTemplate.class), new TypeReference<ArrayList<ResBackTemplate>>(){});
		resBackMap = templateList.stream().collect(Collectors.toMap(ResBackTemplate::getId, Function.identity()));
		resBackMap.values().stream().forEach(t -> t.init());
		this.resBackMap = ImmutableMap.copyOf(resBackMap);
	}
	public void loadResBackExpeditionConfig(){
		Map<Integer,ResBackExpeditionTemplate> expeditionMap = Maps.newConcurrentMap();
		List<ResBackExpeditionTemplate> templateList = JSONUtil.fromJson(getJson(ResBackExpeditionTemplate.class), new TypeReference<ArrayList<ResBackExpeditionTemplate>>(){});
		expeditionMap = templateList.stream().collect(Collectors.toMap(ResBackExpeditionTemplate::getId, Function.identity()));
		expeditionMap.values().stream().forEach(t -> t.init());
		this.expeditionMap = ImmutableMap.copyOf(expeditionMap);
	}
	public void loadResBackFlashConfig(){
		Map<Integer,ResBackFlashTemplate> flashMap = Maps.newConcurrentMap();
		List<ResBackFlashTemplate> templateList = JSONUtil.fromJson(getJson(ResBackFlashTemplate.class), new TypeReference<ArrayList<ResBackFlashTemplate>>(){});
		flashMap = templateList.stream().collect(Collectors.toMap(ResBackFlashTemplate::getId, Function.identity()));
		flashMap.values().stream().forEach(t -> t.init());
		this.flashMap = ImmutableMap.copyOf(flashMap);
	}
	
	public List<Items> getResBackExpedition(int sweep){
		ResBackExpeditionTemplate template = expeditionMap.get(sweep);
		if(template==null){
			return null;
		}
		return template.getRewards();
	}
	
	public List<Items> getResBackFlsh(int id){
		ResBackFlashTemplate template = flashMap.get(id);
		if(template==null){
			return Lists.newArrayList();
		}
		return template.getRewards();
	}
	
	public List<Items> getResBackCost(int resBackType,int type){
		ResBackTemplate template = resBackMap.get(resBackType);
		if(template==null){
			return null;
		}
		return type==0?template.getNormalCost():template.getGlodCost();
	}
	
	public List<Items> getResBackCostAll(List<ResBackType> backTypes,int type){
		ItemBiz itemBiz = SpringUtil.getBean(ItemBiz.class);
		List<Items> cost = Lists.newArrayList();
		for(ResBackType backType:backTypes){
			ResBackTemplate template = resBackMap.get(backType.getType());
			if(template!=null){
				cost.addAll(type==0?template.getNormalCost():template.getGlodCost());
			}
		}
		return itemBiz.createItemList(cost);
	}
	
	@Override
	public List<String> getDownloadFile() {
		return getConfigName(ResBackTemplate.class,ResBackExpeditionTemplate.class,ResBackFlashTemplate.class);
	}

}
