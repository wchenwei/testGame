package com.hm.war.sg.unit;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Maps;
import com.hm.enums.TankAttrType;
import com.hm.util.MathUtils;

import java.util.Map;

public class UnitAttr {
	private transient Map<Integer,Double> attrMap = Maps.newConcurrentMap();
	
	public UnitAttr() {
		super();
	}
	public UnitAttr(Map<Integer, Double> attrMap) {
		this.attrMap = attrMap;
	}


	public long getLongValue(TankAttrType type) {
		return attrMap.getOrDefault(type.getType(), 0d).longValue();
	}
	
	public double getDoubleValue(TankAttrType type) {
		return attrMap.getOrDefault(type.getType(), 0d);
	}
	
	public void addAttr(TankAttrType type,double value) {
		this.attrMap.put(type.getType(), getDoubleValue(type)+value);
	}
	public void putAttr(TankAttrType type,double value) {
		this.attrMap.put(type.getType(), value);
	}
	
	public void addAttrMap(Map<Integer,Double> addMap) {
		if(CollUtil.isEmpty(addMap)) {
			return;
		}
		for (Map.Entry<Integer,Double> entry : addMap.entrySet()) {
			int type = entry.getKey();
			this.attrMap.put(type, attrMap.getOrDefault(type, 0d)+entry.getValue());
		}
	}
	public void addAttrMapForEnum(Map<TankAttrType,Double> addMap) {
		for (Map.Entry<TankAttrType,Double> entry : addMap.entrySet()) {
			addAttr(entry.getKey(), entry.getValue());
		}
	}
	
	public void addAttrMap(UnitAttr unitAttr) {
		addAttrMap(unitAttr.attrMap);
	}
	
	//计算属性加成
	public void calAttrRate(Map<Integer,Double> attrRateMap) {
		for (Map.Entry<Integer,Double> entry : attrRateMap.entrySet()) {
			int attId = entry.getKey();
			if(this.attrMap.containsKey(attId)) {
				double value = this.attrMap.getOrDefault(attId, 0d)*(1+entry.getValue());
				this.attrMap.put(attId, value);
			}
		}
	}
	
	public void calAttrRate(double rate) {
		for (Map.Entry<Integer,Double> entry : this.attrMap.entrySet()) {
			this.attrMap.put(entry.getKey(), entry.getValue()*rate);
		}
	}
	
	public Map<Integer, Double> getAttrMap() {
		return attrMap;
	}
	public UnitAttr clone() {
		Map<Integer,Double> cloneMap = Maps.newHashMap(this.attrMap);
		return new UnitAttr(cloneMap);
	}
	public static UnitAttr create(Map<Integer,Double> attrMap) {
		Map<Integer,Double> cloneMap = Maps.newHashMap(attrMap);
		return new UnitAttr(cloneMap);
	}
	
	/**
	 * 战斗前重新构建攻击cd属性
	 */
	public void rebuildAtkCd() {
		double atkCd = getDoubleValue(TankAttrType.AtkCd);
		if(atkCd <= 0) atkCd = 20;
		putAttr(TankAttrType.AtkCd, MathUtils.div(1, atkCd));
	}
	
	public int getTureAtkCd() {
		double atkCd = getDoubleValue(TankAttrType.AtkCd);
		if(atkCd <= 0) atkCd = MathUtils.div(1, 50);
		int resultCD = (int)MathUtils.div(1, atkCd);
		//最大60 最小5
		resultCD = Math.max(Math.min(60, resultCD), 5);
		return resultCD;
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Integer id : attrMap.keySet()) {
			sb.append(TankAttrType.getType(id).getDesc() + ":" + attrMap.get(id) + ",");
		}
		return sb.toString();
	}
}
