package com.hm.model.player;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.MasteryConfig;
import com.hm.config.excel.templaextra.MasteryLineTemplate;

import java.util.List;
import java.util.Map;

public class Mastery{
	private int type;//1-防御 2-进攻 3-支援 4-辅助
	private int[] lvs = new int[]{0,0,0,0,0,0,0,0,0};//等级
	private Map<Integer,Integer> cricleMap = Maps.newConcurrentMap();
	
	public Mastery() {
		super();
	}
	public Mastery(int type) {
		super();
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
	public void lvUp(int index) {
		this.lvs[index-1] = this.lvs[index-1]+1;
	}
	public int[] getLvs(){
		return this.lvs;
	}
	
	public Map<Integer, Integer> getCricleMap() {
		return cricleMap;
	}
	//该部位是否能够升级
	public boolean isCanLvUp(int index){
		int lv = lvs[index-1];//该部位的等级
		MasteryConfig masterConfig = SpringUtil.getBean(MasteryConfig.class);
		//要升级必须至少有一条线上的其余两个点的等级和本部位相差不超过1级
		ListMultimap<Integer, List<Integer>> map = masterConfig.getConnect();
		List<List<Integer>> allLines = map.get(index);
		return allLines.stream().anyMatch(list ->{
			int firstLv = lvs[list.get(0)-1];
			int secondLv = lvs[list.get(1)-1];
			if(firstLv>0&&secondLv>0&&lv<=Math.min(firstLv, secondLv)){
				return true;
			}
			return lv-Math.min(firstLv, secondLv)<1;
		});
	}
	
	public int getLv(int index) {
		return this.lvs[index-1];
	}
	public void calCircleLv() {
		this.cricleMap.clear();
		MasteryConfig masteryConfig = SpringUtil.getBean(MasteryConfig.class);
		for(MasteryLineTemplate template :masteryConfig.getLines()){
			List<Integer> points = template.getLines();
			int lineLv = getLineLv(points);
			if(lineLv>0){
				this.cricleMap.put(template.getId(), lineLv);
			}
		}
	}

	public boolean isChanged(){
		MasteryConfig masteryConfig = SpringUtil.getBean(MasteryConfig.class);
		for(MasteryLineTemplate template :masteryConfig.getLines()){
			List<Integer> points = template.getLines();
			int lineLv = getLineLv(points);
			if(lineLv>0 && cricleMap.getOrDefault(template.getId(),0) != lineLv){
				return true;
			}
		}
		return false;
	}

	
	public int getLineLv(List<Integer> points){
		List<Integer> pointsLv = Lists.newArrayList();
		points.forEach(index -> pointsLv.add(lvs[index-1]));
		int lineLv = CollUtil.min(pointsLv);
		// 全连接 5/10/15/20
		if (points.size() > 3){
			lineLv = lineLv / 5 * 5;
		}
		return lineLv;
	}
}
