package com.hm.war.sg;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Lists;
import com.hm.util.RandomUtils;
import com.hm.war.sg.unit.Unit;

import java.util.*;
import java.util.stream.Collectors;

public class ChoiceUtils {
	/**
	 * 根据位置获取同一排的位置
	 * @param index
	 * @return
	 */
	public static List<Integer> getLinesByIndex(int index) {
		int lineNum = index/3+1;
		return getLinesByLineNum(lineNum);
	}

	public static int getLine(int index) {
		return index/3+1;
	}

	public static List<Integer> getColsByIndex(int index) {
		int lineNum = index%3+1;
		return getColsByColNum(lineNum);
	}

	public static int[][] NearIndex = {
			{1,3},
			{0,2,4},
			{1,5},

			{0,4,6},
			{1,3,5,7},
			{2,4,8},

			{3,7},
			{4,6,8},
			{5,7},
	};
	
	public static List<Integer> getLinesByLineNum(int lineNum) {
		switch (lineNum) {
			case 1:
				return Lists.newArrayList(0,1,2);
			case 2:
				return Lists.newArrayList(3,4,5);
			case 3:
				return Lists.newArrayList(6,7,8);
		}
		return Lists.newArrayList();
	}
	public static List<Integer> getColsByColNum(int colNum) {
		switch (colNum) {
			case 1:
				return Lists.newArrayList(0,3,6);
			case 2:
				return Lists.newArrayList(1,4,7);
			case 3:
				return Lists.newArrayList(2,5,8);
		}
		return Lists.newArrayList();
	}

	
	/**
	 * 从位置里随机一个
	 * @param indexList
	 * @param targetMap
	 * @return
	 */
	public static Unit randomChoiceUnit(List<Integer> indexList, Map<Integer,Unit> targetMap) {
		List<Unit> lineUnits = indexList.stream().map(e -> targetMap.get(e))
				.filter(Objects::nonNull).collect(Collectors.toList());
		if(!lineUnits.isEmpty()) {
			return RandomUtil.randomEle(lineUnits);//随机一个
		}
		return null;
	}
	
	/**
	 * 普攻额外随机多个目标
	 * @param defList
	 * @param excludeList
	 * @param num
	 * @return
	 */
	public static List<Unit> randomExtraAtkTarget(List<Unit> defList,List<Unit> excludeList,int count) {
		//移除当前目标
		defList.removeAll(excludeList);
		//随机剩余多个
		return RandomUtils.randomEleList(defList, count);
	}
	
	public static List<Unit> listChoiceUnits(List<Integer> indexList,Map<Integer,Unit> targetMap) {
		return indexList.stream().map(e -> targetMap.get(e))
				.filter(Objects::nonNull).collect(Collectors.toList());
	}
	public static List<Unit> listUnitsByIndexs(List<Integer> indexList,List<Unit> unitList) {
		return unitList.stream().filter(e -> indexList.contains(e.getIndex())).collect(Collectors.toList());
	}
	
	//血量比最高的战车
	public static List<Unit> getHpRateUnitMax(List<Unit> unitList) {
		if(CollUtil.isEmpty(unitList)) {
			return Lists.newArrayList();
		}
		Optional<Unit> max = unitList.stream()
                .collect(Collectors.maxBy(Comparator.comparingDouble(Unit::getHpRate)));
		return Lists.newArrayList(max.get());
	}
	//血量比排序
	public static List<Unit> getHpRateUnit(List<Unit> unitList,boolean isTop) {
		if(CollUtil.isEmpty(unitList)) {
			return Lists.newArrayList();
		}
		List<Unit> tempList = unitList.stream().sorted(Comparator.comparingDouble(Unit::getHpRate))
				.collect(Collectors.toList());
		if(isTop) {
			Collections.reverse(tempList);
		}
		return tempList;
	}
	//血量最低的战车
	public static List<Unit> getHpUnit(List<Unit> unitList,boolean isTop) {
		if(CollUtil.isEmpty(unitList)) {
			return Lists.newArrayList();
		}
		List<Unit> tempList = unitList.stream().sorted(Comparator.comparingDouble(Unit::getHp))
				.collect(Collectors.toList());
		if(isTop) {
			Collections.reverse(tempList);
		}
		return tempList;
	}
	//最前排战车
	public static List<Unit> getFirstRowUnit(List<Unit> unitList) {
		if(CollUtil.isEmpty(unitList)) {
			return Lists.newArrayList();
		}
		Unit min = unitList.stream()
                .collect(Collectors.minBy(Comparator.comparingDouble(Unit::getIndex))).get();
		return listUnitsByIndexs(getLinesByIndex(min.getIndex()), unitList);
	}
	
	//最后排战车
	public static List<Unit> getLastRowUnit(List<Unit> unitList) {
		if(CollUtil.isEmpty(unitList)) {
			return Lists.newArrayList();
		}
		Unit min = unitList.stream()
                .collect(Collectors.maxBy(Comparator.comparingDouble(Unit::getIndex))).get();
		return listUnitsByIndexs(getLinesByIndex(min.getIndex()), unitList);
	}
	
	//获取某阵营的所有战车
	public static List<Unit> getUnitByCountry(List<Unit> unitList,int country) {
		if(CollUtil.isEmpty(unitList)) {
			return Lists.newArrayList();
		}
		return unitList.stream().filter(e -> e.getSetting().getCountry() == country).collect(Collectors.toList());
	}
	
	public static List<Unit> getUnitByTank(List<Unit> unitList,int type) {
		if(CollUtil.isEmpty(unitList)) {
			return Lists.newArrayList();
		}
		return unitList.stream().filter(e -> e.getSetting().getAmyType() == type).collect(Collectors.toList());
	}
	
	/**
	 * 获取攻击力最高
	 * @param unitList
	 * @return
	 */
	public static List<Unit> getMaxAtkUnit(List<Unit> unitList) {
		if(CollUtil.isEmpty(unitList)) {
			return Lists.newArrayList();
		}
		Optional<Unit> max = unitList.stream()
                .collect(Collectors.maxBy(Comparator.comparingLong(Unit::getBaseAtk)));
		return Lists.newArrayList(max.get());
	}
	
	public static void main(String[] args) {
		List<String> list = Lists.newArrayList("aa","bbb","a");
		Optional<String> max = list.stream()
                .collect(Collectors.maxBy(Comparator.comparingInt(String::length)));
		System.err.println(max.orElse("11"));
	}
}
