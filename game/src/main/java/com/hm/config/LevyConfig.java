package com.hm.config;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.templaextra.LevyExtraTemplate;
import com.hm.enums.TreasuryCollectType;
import com.hm.model.weight.RandomRatio;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Slf4j
@Config
public class LevyConfig extends ExcleConfig{
	private Map<Integer, LevyExtraTemplate> leveyMap = Maps.newHashMap(); 
	private int maxCount;

	@Override
	public void loadConfig() {
		loadLevyConfig();
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(LevyExtraTemplate.class);
	}
	private void loadLevyConfig(){
		Map<Integer, LevyExtraTemplate> leveyMap = Maps.newHashMap();
		List<LevyExtraTemplate> templateList = JSONUtil.fromJson(getJson(LevyExtraTemplate.class), new TypeReference<ArrayList<LevyExtraTemplate>>(){});
		for(LevyExtraTemplate template:templateList){
			template.init();
			leveyMap.put(template.getLevy_count(),template);
			this.maxCount = Math.max(this.maxCount, template.getLevy_count());
		}
		this.leveyMap = ImmutableMap.copyOf(leveyMap);
		log.info("加载官府征收配置完成");
	}
	
	public LevyExtraTemplate getLevy(int count){
		return this.leveyMap.get(count);
	}
	//获取暴击倍数
	public int getCritRate(int count,int type){
		LevyExtraTemplate template = getLevy(count);
		if(template==null){
			return 1;
		}
		RandomRatio ratio = type==TreasuryCollectType.CashLevy.getType()?template.getCashRatio():template.getOilRatio();
		return ratio.randomRatio();
	}
	public int getLevyCashCost(int count){
		LevyExtraTemplate template = getLevy(count);
		if(template==null){
			return 20;
		}
		return template.getCash_cost_gold();
	}
	public int getLevyOilCost(int count){
		LevyExtraTemplate template = getLevy(count);
		if(template==null){
			return 5;
		}
		return template.getOil_cost_gold();
	}
	public int getLevyCost(int type,int count){
		LevyExtraTemplate template = getLevy(Math.min(maxCount,count));
		if(template==null){
			return 20;
		}
		return type==TreasuryCollectType.CashLevy.getType()?template.getCash_cost_gold():template.getOil_cost_gold();
	}
}
