package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.*;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.TankJingzhuStarTemplate;
import com.hm.config.excel.templaextra.TankJingzhuWashTempImpl;
import com.hm.model.tank.TankAttr;
import com.hm.util.RandomUtils;
import com.hm.util.WeightMeta;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 坦克精铸配置
 * @ClassName:  TankJingzhuConfig   
 * @Description:
 * @author: zxj
 * @date:   2020年7月9日 上午9:47:20
 */
@Slf4j
@Config
public class TankJingzhuConfig extends ExcleConfig{
	//id, TankJingzhuWashTempImpl
	private Map<Integer, TankJingzhuWashTempImpl> tankJingzhuWashMap = Maps.newHashMap(); 
	//位置，品质，随机权重
	private Table<Integer, Integer, WeightMeta<Integer>> tankJingzhuWashWeight =  HashBasedTable.create();
	//lv, TankJingzhuStarTemplate
	private Map<Integer, TankJingzhuStarTemplate> tankJingzhuStarMap = Maps.newHashMap(); 
	//lv, 总经验
	private Map<Integer, Integer> tankJingzhuStarExpMap = Maps.newHashMap();
	
	private int maxStartLv = 0;
	private int maxExp = 0;

	public int getMaxExp() {
		return maxExp;
	}

	@Override
	public void loadConfig() {
		loadTankJingzhuWash();
		loadJingzhuStar();
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(TankJingzhuWashTempImpl.class, TankJingzhuStarTemplate.class);
	}

	private void loadTankJingzhuWash() {
		Map<Integer, TankJingzhuWashTempImpl> tempJingzhuWashMap = Maps.newHashMap();
        List<TankJingzhuWashTempImpl> templateList= JSONUtil.fromJson(getJson(TankJingzhuWashTempImpl.class), new TypeReference<ArrayList<TankJingzhuWashTempImpl>>() {});
        Table<Integer, Integer, Map<Integer, Integer>> tempJingzhuWashWeightMap =  HashBasedTable.create();
        templateList.forEach(e -> {
        	e.init();
        	tempJingzhuWashMap.put(e.getId(), e);
        	Map<Integer, Integer> tempMap = tempJingzhuWashWeightMap.get(e.getPosition(), e.getQuality());
        	if(tempMap==null) {
        		tempMap = Maps.newHashMap();
        	}
        	tempMap.put(e.getId(), e.getWeight());
        	tempJingzhuWashWeightMap.put(e.getPosition(), e.getQuality(), tempMap);
        });
        Table<Integer, Integer, WeightMeta<Integer>> tempJingzhuWashWeight =  HashBasedTable.create();
        tempJingzhuWashWeightMap.rowKeySet().forEach(e->{
        	tempJingzhuWashWeightMap.row(e).keySet().forEach(g->{
        		Map<Integer, Integer> tempMap = tempJingzhuWashWeightMap.get(e, g);
        		tempJingzhuWashWeight.put(e, g, RandomUtils.buildWeightMeta(tempMap));
        	});
        });
		this.tankJingzhuWashMap = ImmutableMap.copyOf(tempJingzhuWashMap);
		this.tankJingzhuWashWeight=ImmutableTable.copyOf(tempJingzhuWashWeight);
		log.info("坦克精铸，随机库配置加载完成");
	}
	
	private void loadJingzhuStar() {
		Map<Integer, TankJingzhuStarTemplate> tempTankJingzhuStarMap = Maps.newHashMap();
		Map<Integer, Integer> tempTankJingzhuStarExpMap = Maps.newHashMap();
        List<TankJingzhuStarTemplate> templateList= JSONUtil.fromJson(getJson(TankJingzhuStarTemplate.class), new TypeReference<ArrayList<TankJingzhuStarTemplate>>() {});
        templateList.forEach(e -> {
        	tempTankJingzhuStarMap.put(e.getStar_level(), e);
        	tempTankJingzhuStarExpMap.put(e.getStar_level(), e.getExp_total());
        });
		this.tankJingzhuStarMap = ImmutableMap.copyOf(tempTankJingzhuStarMap);
		this.tankJingzhuStarExpMap = ImmutableMap.copyOf(tempTankJingzhuStarExpMap);
		maxStartLv = Collections.max(tankJingzhuStarExpMap.keySet());
		maxExp = Collections.max(tankJingzhuStarExpMap.values());
		
		log.info("坦克精铸，等级配置加载完成");
	}

	public List<Integer> getRandomData(int position, List<Integer> listQuality) {
		List<Integer> data = Lists.newArrayList();
		listQuality.forEach(d->{
			if(0==d) {
				data.add(0);
			}else {
				data.add(tankJingzhuWashWeight.get(position, d).random());
			}
		});
		return data;
	}

	public int getStartLv(long exp) {
		for(int i=0; i<maxStartLv; i++) {
			int lvTotalExp = tankJingzhuStarExpMap.get(i);
			if(exp>=maxExp) {
				return maxStartLv;
			}else if(exp>=lvTotalExp && exp<tankJingzhuStarExpMap.get(i+1)) {
				return i;
			}
		}
		return 0;
	}
	
	public int getMaxLv() {
		return maxStartLv;
	}
	
	public float getJingzhuStart(int lv) {
		return this.tankJingzhuStarMap.getOrDefault(lv, new TankJingzhuStarTemplate()).getBuff();
	}
	
	public TankAttr getTankAttr(int[] ids, float addTimes) {
		TankAttr tankAttr = new TankAttr();
		for(int id: ids) {
			if(id!=0) {
				TankJingzhuWashTempImpl washTemp = tankJingzhuWashMap.get(id);
				tankAttr.addAttr(washTemp.getTankAttr(addTimes));
			}
		}
		return tankAttr;
	}
	
	public int getRandomWashId(int position, int type, int oldId, CommValueConfig commValueConfig) {
		TankJingzhuWashTempImpl washTemplate = tankJingzhuWashMap.get(oldId);
		int quality = 1;
		if(null==washTemplate) {
			quality = 1;
		}else {
			quality = commValueConfig.getTankJingzhuRandomQuality(type, position, washTemplate.getQuality());
		}
		WeightMeta<Integer> randomWeight = tankJingzhuWashWeight.get(position, quality);
		if(null==randomWeight) {
			return 1;
		}
		Integer result = randomWeight.random();
		return null==result?0:result;
	}
}








