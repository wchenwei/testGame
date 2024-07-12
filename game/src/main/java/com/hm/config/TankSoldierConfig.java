package com.hm.config;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.*;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.templaextra.TankSoldierCircleExtraTemplate;
import com.hm.config.excel.templaextra.TankSoldierLevelTemplate;
import com.hm.model.tank.TankAttr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Date 2020年7月7日09:42:28
 * @author xjt
 *
 */
@Config
public class TankSoldierConfig extends ExcleConfig{
	private Table<Integer,Integer,TankSoldierCircleExtraTemplate> cricleTable = HashBasedTable.create();
	private Map<Integer,Integer> cricleMaxLvs = Maps.newConcurrentMap();
	private Map<Integer,TankSoldierLevelTemplate> lvs = Maps.newConcurrentMap();
	private int lvMax;
	@Override
	public void loadConfig() {
		loadCircleConifg();
		loadLvUpConfig();
	}
	public void loadLvUpConfig(){
		Map<Integer,TankSoldierLevelTemplate> lvs = Maps.newConcurrentMap();
		for(TankSoldierLevelTemplate template:JSONUtil.fromJson(getJson(TankSoldierLevelTemplate.class), new TypeReference<ArrayList<TankSoldierLevelTemplate>>(){})){
			template.init();
			lvs.put(template.getId(), template);
			this.lvMax = Math.max(this.lvMax, template.getId());
		}
		this.lvs = ImmutableMap.copyOf(lvs);
	}
	
	public void loadCircleConifg(){
		Map<Integer,Integer> cricleMaxLvs = Maps.newConcurrentMap();
		Table<Integer,Integer,TankSoldierCircleExtraTemplate> cricleTable = HashBasedTable.create();
		for(TankSoldierCircleExtraTemplate template:JSONUtil.fromJson(getJson(TankSoldierCircleExtraTemplate.class), new TypeReference<ArrayList<TankSoldierCircleExtraTemplate>>(){})){
			template.init();
			cricleMaxLvs.put(template.getType(), template.getLevel());
			cricleTable.put(template.getType(),template.getLevel(), template);
		}
		this.cricleTable = ImmutableTable.copyOf(cricleTable);
		this.cricleMaxLvs = ImmutableMap.copyOf(cricleMaxLvs);
	}
	

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(TankSoldierCircleExtraTemplate.class,TankSoldierLevelTemplate.class);
	}
	
	
	public int getMaxCricleLv(int type) {
		return cricleMaxLvs.getOrDefault(type, 0);
	}

	public TankSoldierCircleExtraTemplate getCircle(int type,int lv){
		return cricleTable.get(type,lv);
	}
	//根据奇兵等级获取加成比例
	public double getAddRate(int lv){
		TankSoldierLevelTemplate template = getLvTemplate(lv);
		if(template==null){
			return 0;
		}
		return template.getAttri_rate();
	}
	
	public TankAttr getAddRate(Map<Integer,Integer> cricles){
		TankAttr tankAttr = new TankAttr();
		if(cricles.size()<=0){
			return tankAttr;
		}
		cricles.forEach((k,v)->{
			TankSoldierCircleExtraTemplate template = getCircle(k, v);
			if(template!=null){
				tankAttr.addAttr(template.getAttrMap());
			}
		});
		return tankAttr;
	}
	
	public int getMaxLv(){
		return lvMax;
	}
	public TankSoldierLevelTemplate getLvTemplate(int lv){
		return this.lvs.get(lv);
	}
}
