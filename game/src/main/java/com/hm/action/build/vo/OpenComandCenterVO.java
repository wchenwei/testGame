package com.hm.action.build.vo;

import com.google.common.collect.Maps;
import com.hm.model.player.CurrencyKind;

import java.util.Map;

public class OpenComandCenterVO {
	private Map<CurrencyKind,Integer> baseRes = Maps.newConcurrentMap();//基础产量
	private Map<CurrencyKind,Double> technologyAdd = Maps.newConcurrentMap();//科技加成
	private Map<CurrencyKind,Double> activityAdd = Maps.newConcurrentMap();//活动加成
	private Map<CurrencyKind,Double> workerAdd = Maps.newConcurrentMap();//内政官加成
	private Map<CurrencyKind,Double> seasonAddMap = Maps.newConcurrentMap();//季节官加成
	public Map<CurrencyKind, Integer> getBaseRes() {
		return baseRes;
	}
	public void setBaseRes(Map<CurrencyKind, Integer> baseRes) {
		this.baseRes = baseRes;
	}
	public Map<CurrencyKind, Double> getTechnologyAdd() {
		return technologyAdd;
	}
	public void setTechnologyAdd(Map<CurrencyKind, Double> technologyAdd) {
		this.technologyAdd = technologyAdd;
	}
	public Map<CurrencyKind, Double> getActivityAdd() {
		return activityAdd;
	}
	public void setActivityAdd(Map<CurrencyKind, Double> activityAdd) {
		this.activityAdd = activityAdd;
	}
	public Map<CurrencyKind, Double> getSeasonAddMap() {
		return seasonAddMap;
	}
	public void setSeasonAddMap(Map<CurrencyKind, Double> seasonAddMap) {
		this.seasonAddMap = seasonAddMap;
	}
	public Map<CurrencyKind, Double> getWorkerAdd() {
		return workerAdd;
	}
	public void setWorkerAdd(Map<CurrencyKind, Double> workerAdd) {
		this.workerAdd = workerAdd;
	}
	
	
	
	
}
