package com.hm.model.tank;

import com.google.common.collect.Maps;
import com.hm.enums.TankAttrType;
import com.hm.util.MathUtils;

import java.util.Map;

/**
 * 坦克属性
 * ClassName: TankAttr. <br/>    
 * date: 2018年10月20日 下午1:42:44 <br/>  
 *  
 * @author yanpeng  
 * @version
 */
public class TankAttr {
	private Map<TankAttrType,Double> attrMap = Maps.newConcurrentMap();
	
	public TankAttr() {
		super();
	}

	public TankAttr(Map<TankAttrType, Double> attrMap) {
		this.attrMap = attrMap;
	}

	public void addAttr(TankAttrType type,double value) {
		this.attrMap.put(type, getAttrValue(type)+value);
	}
	
	public void addAttr(TankAttr tankAttr){
		if(tankAttr==null){
			return;
		}
		addAttr(tankAttr.getAttrMap());
	}
	
	public void addAttr(Map<TankAttrType, Double> addMap) {
		addMap.forEach((key,value)->this.attrMap.merge(key, value, (x,y)->(x+y)));
	}
	
	public void addAttr(Map<TankAttrType, Double> addMap,double rate) {
		addMap.forEach((key, value) ->{
			double mul = MathUtils.mul(value, rate);
			this.attrMap.merge(key, mul, Double::sum);
		});
	}
	
	public void addAttr(int attrId,double value){
		addAttr(TankAttrType.getType(attrId), value);
	}
	
	public double getAttrValue(TankAttrType type) {
		return attrMap.getOrDefault(type, 0d);
	}
	public double getAttrValue(int attrId){
		return getAttrValue(TankAttrType.getType(attrId));
	}
	
	public Map<TankAttrType,Double> getAttrMap(){
		return this.attrMap; 
	}
}
