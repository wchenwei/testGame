/**
 * 
 */
package com.hm.util;

import com.hm.libcore.util.count.CountUtil;
import com.hm.libcore.util.date.DateUtil;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Title: SLGCountUtil.java
 * Description:通用计算
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 叶亮
 * @date 2015年5月25日 上午9:56:40
 * @version 1.0
 */
public class SLGCountUtil {

	public static int getSpeedUpCostSecond(int sumSecond) {
		int costTime = CountUtil.ceil(sumSecond/60d);
		int costGold = costTime*1;
		return costGold;
	}
	
	public static int getSpeedUpCost(long endTime){
		int sumSecond = DateUtil.getRemainTime(endTime);
		return getSpeedUpCostSecond(sumSecond);
	}
	
	
	public static int[] getIntegerRandomNumberInRangeArr(int[] arr, int count) {
		int[] returnIndex = new int[count];
		int[] result = shuffleArray(arr);
		for (int i = 0; i < count; i++) {
			returnIndex[i] = result[i];
		}
		return returnIndex;
	}
	
	/**
	 * 数组洗牌
	 * 
	 * @param objArr
	 * @return
	 */
	private static int[] shuffleArray(int[] objArr) {
		int[] result = new int[objArr.length];

		for (int i = 0; i < objArr.length; i++) {
			// 得到一个位置
			int j = RandomUtils.randomInt(objArr.length - i);
			// 得到那个位置的数值
			result[i] = objArr[j];
			// 将最后一个未用的数字放到这里
			objArr[j] = objArr[objArr.length - 1 - i];
		}
		return result;
	}
	
	
	
	public static void main(String[] args) {
		int x = 10;
		
		int y = 23;
		
		
		int point1x = x-2;
		int point1y = y-5;
		
		int point2x = x+2;
		int point2y = y-5;
		int point3x = x-2;
		int point3y = y+5;
		
		int point4x = x+2;
		int point4y = y+5;
		
		
	}
	private static NumberFormat nf = new DecimalFormat("0.00000");
	
	public static double format(double d){
		return Double.parseDouble(nf.format(d));
	}
	
//	public static void main(String[] args) {
//		Queue<Integer> integers = new ConcurrentLinkedQueue<Integer>();
//		integers.add(1);
//		integers.add(2);
//		integers.add(3);
//		integers.add(4);
//		integers.add(5);
//		integers.add(6);
//		integers.add(7);
//		integers.add(8);
//		
//		int index = CountUtil.getIntegerRandomNumberInRange(0, integers.size()-1);
//		
//		int id = (int) integers.toArray()[index];
//		
//		integers.remove(id);
//		
//		int index1 = CountUtil.getIntegerRandomNumberInRange(0, integers.size()-1);
//		
//		int id1 = (int) integers.toArray()[index1];
//		
//		integers.remove(id1);
//		
//		int index2 = CountUtil.getIntegerRandomNumberInRange(0, integers.size()-1);
//		
//		int id2 = (int) integers.toArray()[index2];
//		
//		integers.remove(id2);
//	}
}
