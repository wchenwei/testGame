package com.hm.model.tank;

import com.google.common.collect.Maps;
import com.hm.config.excel.templaextra.ChipTemplateImpl;

import java.util.Map;

/**
 * 
 * ClassName: TankTech. <br/>  
 * Function: 坦克的科技系统（又名：芯片系统、突破系统）. <br/>  
 * date: 2019年3月6日 上午9:21:40 <br/>  
 * @author zxj  
 * @version
 */
public class TankTech {
	//当前科技升级记录信息(当前科技的位置，当前科技的等级)
	private Map<Integer, Integer> techData = Maps.newHashMap();

	//当前打开的科技层级(第几个科技)，默认打开0层。
	private int openChip = 0;
	
	public int getOpenChip() {
		return openChip;
	}
	/**
	 * updateOpenChip:(更新科技层级信息). <br/>  
	 * @author zxj  
	 */
	public void updateOpenChip() {
		this.openChip = this.openChip+1;
		//清除本层的升级记录，下一层重新来
		techData.clear();
	}
	/**
	 * updateTech:(更新科技信息). <br/>  
	 * @author zxj  
	 * @param position
	 */
	public void updateTech(int position) {
		this.techData.put(position, this.techData.getOrDefault(position, 0)+1);
	}
	/**
	 * getTechLv:(获取科技当前位置的等级). <br/>  
	 * @author zxj
	 * @param position
	 * @return  使用说明
	 */
	public int getTechLv(int position) {
		return this.techData.getOrDefault(position, 0);
	}
	/**
	 * canUpPositoin:(判断是否可以升级当前位置). <br/>  
	 * @author zxj  
	 * @param position
	 * @return  使用说明
	 */
	public boolean canUpPositoin(int position, Map<Integer, ChipTemplateImpl> tech) {
		if(techData.keySet().contains(position)) {
			return true;
		}
		for(int key :techData.keySet()) {
			ChipTemplateImpl template = tech.get(key);
			if(template.nextTechContains(position)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * canUpdateChip:(判断是否可以升级层级). <br/>  
	 * @author zxj  
	 * @param techMap
	 * @return  使用说明
	 */
	public boolean canUpdateChip(Map<Integer, ChipTemplateImpl> techMap) {
		for(int key :techMap.keySet()) {
			//判断所有的，是否都已经包含并且升级到满级了
			if(!techData.containsKey(key) || techData.get(key)!=techMap.get(key).getMax_level()) {
				return false;
			}
		}
		return true;
	}
	//获得正在升级的科技信息
	public Map<Integer, Integer> getTechData() {
		return techData;
	}
}











