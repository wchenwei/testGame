package com.hm.util;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.enums.TankAttrType;

import java.util.List;
import java.util.Map;

public class TankAttrUtils {
	public static Map<Integer, Double> str2TankAttrIntMap(String source, String sp1, String sp2) {
		Map<Integer, Double> attrMap = Maps.newHashMap();
		try {
			for(String str :source.split(sp1)) {
				int attrId = Integer.parseInt(str.split(sp2)[0]);
				double value = Double.parseDouble(str.split(sp2)[1]);
				attrMap.merge(attrId, value, (x,y)->(x+y));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return attrMap;
	}
	
	public static Map<TankAttrType, Double> str2TankAttrMap(String source, String sp1, String sp2) {
		Map<TankAttrType, Double> attrMap = Maps.newHashMap();
		try {
			for(String str :source.split(sp1)) {
				int attrId = Integer.parseInt(str.split(sp2)[0]);
				double value = Double.parseDouble(str.split(sp2)[1]);
				attrMap.merge(TankAttrType.getType(attrId), value, (x,y)->(x+y));
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("解析坦克属性加成出错!"+source);
		}
		return attrMap;
	}
	
	public static Map<TankAttrType, Double> str2TankAttrMap(String source, String sp1) {
		Map<TankAttrType, Double> attrMap = Maps.newHashMap();
		try {
			int attrId = Integer.parseInt(source.split(sp1)[0]);
			double value = Double.parseDouble(source.split(sp1)[1]);
			attrMap.put(TankAttrType.getType(attrId), value);
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("解析坦克属性加成出错!");
		}
		return attrMap;
	}
	public static List<Map<TankAttrType, Double>> str2TankAttrList(String source, String sp1, String sp2) {
		List<Map<TankAttrType, Double>> attrList = Lists.newArrayList();
		try {
			for(String str :source.split(sp1)) {
				int attrId = Integer.parseInt(str.split(sp2)[0]);
				double value = Double.parseDouble(str.split(sp2)[1]);
				Map<TankAttrType, Double> map = Maps.newHashMap();
				map.put(TankAttrType.getType(attrId), value);
				attrList.add(map);
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("解析坦克属性加成出错!");
		}
		return attrList;
	}

	public static Map<Integer, Double> tankAttrToIntMap(Map<TankAttrType, Double> tankAttrTypeMap) {
		Map<Integer, Double> attrMap = Maps.newHashMap();
		if (CollUtil.isEmpty(tankAttrTypeMap)){
			return attrMap;
		}
		tankAttrTypeMap.entrySet().forEach(e -> attrMap.put(e.getKey().getType(), e.getValue()));
		return attrMap;
	}
}
