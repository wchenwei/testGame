package com.hm.model.player;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Maps;
import com.hm.libcore.msg.JsonMsg;
import com.hm.config.GameConstants;

import java.util.Map;

/**
 * @Description: 玩家勋章信息
 * @author siyunlong  
 * @date 2019年4月29日 下午6:25:02 
 * @version V1.0
 */
public class PlayerMedal extends PlayerDataContext {
	//当前佩戴的勋章
	private int medialId;
	//玩家勋章价值
	private long medialValue;
	//当前玩家的勋章等级
	private int lv = 1;
	private int subLv;//子等级

	// 合成的特殊勋章 id - 属性 - 属性值
	private Map<Integer, Map<Integer, Integer>> specialMedalMap = Maps.newConcurrentMap();

	public int getMedialId() {
		return medialId;
	}

	public void setMedialId(int medialId) {
		this.medialId = medialId;
		SetChanged();
	}

	public int getLv() {
		return lv;
	}

	public void setLv(int lv) {
		this.lv = lv;
		this.subLv = 0;
		SetChanged();
	}
	
	public int getSubLv() {
		return subLv;
	}

	public void subLvUp() {
		this.subLv ++;
		SetChanged();
	}

	public long getMedialValue() {
		return medialValue;
	}

	public void setMedialValue(long medialValue) {
		this.medialValue = medialValue;
		SetChanged();
	}

	public Map<Integer, Map<Integer,Integer>> getSpecialMedalMap() {
		return specialMedalMap;
	}


	public boolean isSpecialMedalExist(int id){
		return specialMedalMap.containsKey(id);
	}

	public int getSpecialMedalLv(int id){
		Map<Integer, Integer> map = specialMedalMap.get(id);
		if (CollUtil.isNotEmpty(map)){
			return map.getOrDefault(GameConstants.Special_Model_Lv,0);
		}
		return 0;
	}

	public int getSpecialMedalStar(int id){
		Map<Integer, Integer> map = specialMedalMap.get(id);
		if (CollUtil.isNotEmpty(map)){
			return map.getOrDefault(GameConstants.Special_Model_Star,0);
		}
		return 0;
	}

	public int getCurMedalStar(){
		if(this.isSpecialMedalExist(medialId)){
			return getSpecialMedalStar(medialId);
		}
		return 0;
	}


	public void upSpecialMedalLv(int id){
		Map<Integer, Integer> map = specialMedalMap.get(id);
		if (CollUtil.isNotEmpty(map)){
			map.put(GameConstants.Special_Model_Lv, map.getOrDefault(GameConstants.Special_Model_Lv,0)+1);
			SetChanged();
		}
	}

	public void upSpecialMedalStar(int id){
		Map<Integer, Integer> map = specialMedalMap.get(id);
		if (CollUtil.isNotEmpty(map)){
			map.put(GameConstants.Special_Model_Star, map.getOrDefault(GameConstants.Special_Model_Star,0)+1);
			SetChanged();
		}
	}

	/**
	 * 添加特殊勋章 0星 1级
	 * @param id
	 */
	public void addSpecialMedal(int id){
		Map<Integer, Integer> map = Maps.newHashMap();
		map.put(GameConstants.Special_Model_Star, 0);
		map.put(GameConstants.Special_Model_Lv, 1);
		specialMedalMap.put(id, map);
		SetChanged();
	}

	@Override
	protected void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerMedal", this);
	}
}
