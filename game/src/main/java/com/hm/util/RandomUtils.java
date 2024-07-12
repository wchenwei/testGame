package com.hm.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.util.weight.WeightRandom;
import com.hm.libcore.util.weight.WeightRandom.WeightObj;
import com.hm.model.item.Items;
import com.hm.model.item.WeightItem;

import java.util.*;

public class RandomUtils extends RandomUtil{  
	/**
	 * 构造道具随机
	 * @param items
	 * @return
	 */
	public static WeightMeta<Items> buildItemWeightMeta(String items) {
		Map<Items, Integer> rewardMap = Maps.newHashMap();
		for (String item : items.split(",")) {
			WeightItem witem = ItemUtils.str2WeightItem(item, ":");
			rewardMap.put(witem.getItems(), witem.getWeight());
		}
        return RandomUtils.buildWeightMeta(rewardMap);
	}
	
    public static <T> WeightMeta<T> buildWeightMeta(final Map<T, Integer> weightMap) {
        final int size = weightMap.size();
        Object[] nodes = new Object[size];
        int[] weights = new int[size];
        int index = 0;
        int weightAdder = 0;
        for (Map.Entry<T, Integer> each : weightMap.entrySet()) {
        	nodes[index] = each.getKey();
    		weights[index++] = (weightAdder = weightAdder + each.getValue());
        }
        return new WeightMeta<T>((T[]) nodes, weights);
    }
    
    public static <T> WeightRandom<T> buildWeightRandom(final Map<T, Integer> weightMap) {
    	WeightRandom<T> random = WeightRandom.create();
    	for (Map.Entry<T, Integer> each : weightMap.entrySet()) {
    		if(each.getValue() > 0) {
    			random.add(new WeightObj<T>(each.getKey(), each.getValue()));
    		}
        }
    	return random;
    }
    
    public static <T> T randomDelEle(List<T> list) {
    	return list.remove(randomInt(list.size()));
	}
    
    //获取带权重的list
    private static List<WeightObj<Items>> getListWeightObj(List<Items> items, int[] weight) {
  		List<WeightObj<Items>> listWeightObj = new ArrayList<WeightObj<Items>>();
  		for(int i=0; i<items.size(); i++) {
  			listWeightObj.add(new WeightObj<Items>(items.get(i), weight[i]));
  		}
  		return listWeightObj;
  	}
    
    public static Items getRandomItems(List<Items> items, int[] weight) {
    	return new WeightRandom<Items>(RandomUtils.getListWeightObj(items, weight)).next();
    }
    
    //概率满足
    public static boolean randomIsRate(double rate) {
    	return RandomUtil.randomDouble() <= rate;
    }
    
    public static <T> List<T> randomEleList(Collection<T> collection, int count) {
    	if(collection.size() <= count) {
    		return Lists.newArrayList(collection);
    	}
    	return Lists.newArrayList(RandomUtil.randomEleSet(collection, count));
    }
    
    public static <T> List<T> randomEleListForAverage(List<T> collection, int count) {
    	if(collection.size() <= count) {
    		return Lists.newArrayList(collection);
    	}
    	List<T> luckList = Lists.newArrayList();
    	int size = collection.size()/count;
    	if(size <= 1) {
    		return randomEleList(collection,count);
    	}
    	for (int i = 0; i < count; i++) {
			int start = i*size;
			int end = (i+1)*size;
			luckList.add(RandomUtil.randomEle(collection.subList(start, end)));
		}
    	return luckList;
    }
    
    public static <T> List<T> randomRepeatableEleList(List<T> collection, int count) {
    	List<T> list = Lists.newArrayList();
    	for (int i = 0; i < count; i++) {
    		list.add(RandomUtil.randomEle(collection));
		}
    	return list;
    }
    
    public static int randomIntForEnd(int min,int max) {
    	if(max+1 <= min) {
    		return max;
    	}
    	return randomInt(min, max+1);
    }

    /**
     * @description
	 * 		生成不重复的 count个 随机数据
	 * @param seed 随机因子
	 * @param count 生成数量
 	 * @param max  最大值
     * @return java.util.List<T>
     * @author wyp
     * @date 2022/6/24 17:25
     */
	public static List<Integer> randomEleList(long seed, int count, int min, int max) {
		final HashSet<Integer> result = new HashSet<Integer>(count);
		if(max < count){
			// 最大值小于  生成数量
			return Lists.newArrayList();
		}
		Random random = new Random(seed);
		while(result.size() < count){
			int i = random.nextInt(max);
			if(i < min){
				continue;
			}
			result.add(i);
		}
		return Lists.newArrayList(result);
	}
    
    public static void main(String[] args) {
		System.err.println(new DateTime(1563206400000L));
		System.err.println(new DateTime(1563379200000L));
	}
}

