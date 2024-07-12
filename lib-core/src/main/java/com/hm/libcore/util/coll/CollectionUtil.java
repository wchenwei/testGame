package com.hm.libcore.util.coll;

import java.util.Collection;
import java.util.Map;

import cn.hutool.core.collection.CollUtil;

public class CollectionUtil {
	/**
	 * 判断两个集合是否有交集
	 * @param coll1
	 * @param coll2
	 * @return
	 */
	public static <T> boolean haveIntersection(Collection<T> coll1, Collection<T> coll2) {
		return coll1.stream().anyMatch(e -> coll2.contains(e));
	}
	
	public static void mapAddMap(Map<Integer,Double> map1,Map<Integer,Double> map2) {
		if(CollUtil.isEmpty(map2)) {
			return;
		}
		for (Map.Entry<Integer,Double> entry : map2.entrySet()) {
			int type = entry.getKey();
			map1.put(type, map1.getOrDefault(type, 0d)+entry.getValue());
		}
	}
	
	public static <T> boolean isSame(Collection<T> coll1,Collection<T> coll2) {
		if(coll1 == null || coll2 == null || coll1.size() != coll2.size()) {
			return false;
		}
		for (T t : coll1) {
			if(!coll2.contains(t)) {
				return false;
			}
		}
		return true;
	}
}
