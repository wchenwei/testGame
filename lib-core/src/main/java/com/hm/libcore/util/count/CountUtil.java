package com.hm.libcore.util.count;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.math.RandomUtils;

/**
 * Title: CountUtil.java Description: 数学类 Copyright: Copyright (c) 2014 Company:
 * Hammer Studio
 * 
 * @author 李飞
 * @date 2014-7-23
 * @version 1.0
 */
public class CountUtil {

	private static NumberFormat nf = new DecimalFormat("0.00");

	private static NumberFormat nf1 = new DecimalFormat("0.000");

	/**
	 * 获得一定范围内的任意整十数
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int getRandomIntegerTen(int min, int max) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = min; i <= max; i++) {
			if (i % 10 == 0) {
				list.add(i);
			}
		}
		return list.get(getIntegerRandomNumberInRange(0, list.size() - 1));
	}

	/**
	 * 
	 * 中值也称中位数，即数据按升序或者降序排列，
	 * 假如有n个数据，当n为偶数时，中位数为第n/2位数和第(n+2)/2位数的平均数；如果n为奇数，那么中位数为第（n+1）/2为数的值。
	 * 
	 * @param dArr
	 * @return
	 */
	public static double median(double[] dArr) {
		if (dArr == null || dArr.length == 0) {
			return 0;
		}
		Arrays.sort(dArr);
		int n = dArr.length;
		if (dArr.length % 2 == 0) {
			return (dArr[n / 2] + dArr[(n + 2) / 2]) / 2;
		} else {
			return dArr[(n + 1) / 2 - 1];
		}

	}

	public static double format(double d) {
		return Double.parseDouble(nf.format(d));
	}

	public static double format1(double d) {
		return Double.parseDouble(nf1.format(d));
	}

	public static double format(double d, String pattern) {
		return Double.parseDouble(new DecimalFormat(pattern).format(d));
	}

	public static long round(double d) {
		return Math.round(d);
	}

	public static double pow(double d, double b) {
		return Math.pow(d, b);
	}

	/**
	 * 向上取整
	 * 
	 * @param d
	 * @return
	 */
	public static int ceil(double d) {
		return (int) Math.ceil(d);
	}
	
	/**
	 * 向上取整
	 * 
	 * @param d
	 * @return
	 */
	public static long ceilLong(double d) {
		return (long) Math.ceil(d);
	}

	/**
	 * 向下取整
	 * 
	 * @param d
	 * @return
	 */
	public static int floor(double d) {
		return (int) Math.floor(d);
	}

	/**
	 * 向下取整
	 * 
	 * @param d
	 * @return
	 */
	public static long floorlong(double d) {
		return (long) Math.floor(d);
	}

	/**
	 * 取绝对值
	 * 
	 * @param d
	 * @return
	 */
	public static int abs(double d) {
		return (int) Math.abs(d);
	}

	/**
	 * 按成长系数增加整型数值 如(10,0.2)返回12
	 * 
	 * @param val
	 * @param coe
	 * @return
	 */
	public static int change(int val, double coe) {
		return (int) (val * (1 + coe));
	}

	/**
	 * 按成长系数增加双精度型数值 如(0.22, 0.1)返回0.24
	 * 
	 * @param val
	 * @param coe
	 * @return
	 */
	public static double change(double val, double coe) {
		return format(val * (1 + coe));
	}

	/**
	 * 按成长系数减少双精度型数值 如(2, 0.1)返回1.8
	 * 
	 * @param val
	 * @param coe
	 * @return
	 */
	public static double changeMinus(double val, double coe) {
		return format(val * (1 - coe));
	}

	private static Random random = new Random();

	/**
	 * 根据几率计算是否触发
	 * 
	 * @param ch
	 *            几率 如0.1代表10% 0.22代表22%
	 * @return
	 */
	public static boolean hasChance(double ch) {
		return new Random().nextInt(10000) < (int) (ch * 10000) || ch >= 1 ? true
				: false;
	}

	/**
	 * 根据几率计算是否触发
	 * 
	 * @param ch
	 *            几率 如1代表1% 22代表22%
	 * @return
	 */
	public static boolean hasIntegerChance(double ch) {
		return new Random().nextInt(100) < ch ? true : false;
	}
	
	
	/**
	 * 根据几率计算是否触发
	 * 
	 * @param ch 分子
	 * @param  denominator           
	 * 几率 ch/denominator   如果denominator = 1000  1表示千分之一
	 * @return
	 */
	public static boolean hasIntegerChance(int ch,int denominator) {
		return new Random().nextInt(denominator) < ch ? true : false;
	}

	/**
	 * 是否在范围中
	 * 
	 * @param num
	 * @param limit
	 *            上限
	 * @param lower
	 *            下限
	 * @return
	 */
	public static boolean isInRange(int num, int lower, int limit) {
		return lower <= num && num <= limit;
	}

	/**
	 * 获得在一定范围内的整形随机数
	 * 
	 * @param lower
	 * @param limit
	 * @return
	 */
	public static int getIntegerRandomNumberInRange(int lower, int limit) {
		return random.nextInt(limit - lower + 1) + lower;
	}

	/**
	 * 获得在一定范围内但是不包含XX的整形随机数
	 * 
	 * @param lower
	 * @param limit
	 * @param exclude
	 * @return
	 */
	public static int getIntegerRandomNumberInRange(int lower, int limit,
			int exclude) {
		int rs = random.nextInt(limit - lower + 1) + lower;
		if (rs == exclude) {
			return getIntegerRandomNumberInRange(lower, limit, exclude);
		}
		return rs;
	}

	/**
	 * 获得在一定范围内但是不包含某个集合内所有数的整形随机数
	 * 
	 * @param lower
	 * @param limit
	 * @param exclude
	 * @return
	 */
	public static int getIntegerRandomNumberInRange(int lower, int limit,
			List<Integer> exclude) {
		int rs = random.nextInt(limit - lower + 1) + lower;
		if (exclude.contains(rs)) {
			return getIntegerRandomNumberInRange(lower, limit, exclude);
		}
		return rs;
	}

	private static DecimalFormat format = new DecimalFormat("0.0");

	/**
	 * 获得在一定范围内的带1位小数点随机数
	 * 
	 * @param lower
	 * @param limit
	 * @return
	 */
	public static double getDoubleRandomNumberInRange(double lower, double limit) {
		return lower
				+ Double.parseDouble(format.format(random.nextDouble()
						* (limit - lower)));
	}

	private static DecimalFormat format2 = new DecimalFormat("0.00");

	private static DecimalFormat format3 = new DecimalFormat("0.000");

	/**
	 * 获得在一定范围内的带2位小数点随机数
	 * 
	 * @param lower
	 * @param limit
	 * @return
	 */
	public static double getDoubleRandomNumberInRange2(double lower,
			double limit) {
		return lower
				+ Double.parseDouble(format2.format(random.nextDouble()
						* (limit - lower)));
	}

	/**
	 * 获得在一定范围内的带2位小数点随机数
	 * 
	 * @param lower
	 * @param limit
	 * @return
	 */
	public static double getDoubleRandomNumberInRange3(double lower,
			double limit) {
		return lower
				+ Double.parseDouble(format3.format(random.nextDouble()
						* (limit - lower)));
	}

	/**
	 * 返回命中的几率的下标
	 * 
	 * @param chArr
	 *            命中几率数组
	 * @return 如果都没有命中(只有在数组中的元素累加小于1的情况下出现) 则返回-1
	 */
	public static int getChance(double[] chArr) {
		int index = -1;
		int randomNum = getIntegerRandomNumberInRange(1, 100);
		double maxRange = 0;
		double minRange = 1;
		double temp = 0;
		for (int i = 0; i < chArr.length; i++) {
			if (chArr[i] == 0) {
				continue;
			}
			maxRange = temp + chArr[i] * 100;
			if (randomNum >= minRange && randomNum <= maxRange) {
				index = i;
				break;
			}
			temp += chArr[i] * 100;
			minRange = temp + 1;
		}
		return index;
	}

	/**
	 * 随机档次公式 随机档次a+随机档次b+随机档次c+随机档次d+随机档次e=10 随机档次为0-10之间的任意整数，且总和=10
	 * 
	 * @param rangeList
	 * @param totle
	 * @return
	 */
	public static int[] randomGradationsCount(String[] rangeList, int totle) {
		int[] result = new int[rangeList.length];
		for (int i = 0; i < rangeList.length; i++) {
			// 当为最后一个范围时，直接使用总数
			if (i == rangeList.length - 1) {
				result[i] = totle;
				break;
			}
			// 当总数为0时设置为0
			if (totle <= 0) {
				result[i] = 0;
				continue;
			}
			// 取范围随机数
			String range = rangeList[i];
			String[] rangeStrArr = range.split("-");
			int min = Integer.parseInt(rangeStrArr[0]);
			int max = Integer.parseInt(rangeStrArr[1]);
			if (min > max) {
				return null;
			}
			// 最大数大于总数时，总数为最大数
			if (max > totle) {
				max = totle;
				// 最大数小于最小数时，设置为最大数
				if (totle < min) {
					result[i] = max;
					totle = 0;
					continue;
				}
			}
			// 范围随机数
			int ran = RandomUtils.nextInt(max + 1 - min) + min;
			result[i] = ran;
			totle = totle - ran;
		}
		return shuffleArray(result);
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
			int j = RandomUtils.nextInt(objArr.length - i);
			// 得到那个位置的数值
			result[i] = objArr[j];
			// 将最后一个未用的数字放到这里
			objArr[j] = objArr[objArr.length - 1 - i];
		}
		return result;
	}

	/**
	 * 获取范围数组
	 *
	 * @param attr
	 * @return
	 */
	public static int[] getIntArrayByStr(String attr) {
		String[] attrs = attr.split("-");
		int end = Integer.parseInt(attrs[1]);
		int start = Integer.parseInt(attrs[0]);
		int[] result = new int[end - start + 1];
		for (int i = 0; i < end - start + 1; i++) {
			result[i] = start + i;
		}
		return result;
	}

	/**
	 * 平均数
	 *
	 * @param upperLimit
	 * @param lowerLimit
	 * @return
	 */
	public static int getAvgNum(int upperLimit, int lowerLimit) {
		return (upperLimit + lowerLimit) / 2;
	}

	/**
	 * 根据符号比较两个数
	 *
	 * @param leftNum
	 * @param rightNum
	 * @param signType
	 * @return
	 */
	public static boolean compareNumber(double leftNum, double rightNum,
			int signType) {
		switch (signType) {
		case 1:

			return leftNum > rightNum;

		case 2:

			return leftNum < rightNum;

		case 3:

			return leftNum == rightNum;

		case 4:

			return leftNum != rightNum;

		case 5:

			return leftNum >= rightNum;

		case 6:

			return leftNum <= rightNum;

		default:
			return false;
		}
	}

	public static boolean containsStringArray(String[] arr, String val) {
		for (String a : arr) {
			if (a.equals(val)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 数组乱序取其中几个
	 * 
	 * @param count
	 * @return
	 */
	public static int[] getIntegerRandomNumberInRangeArr(int arr, int count) {
		int[] index = new int[arr];
		int[] returnIndex = new int[count];
		for (int i = 0; i < index.length; i++) {
			index[i] = i + 1;
		}
		int[] result = shuffleArray(index);
		for (int i = 0; i < count; i++) {
			returnIndex[i] = result[i];
		}

		return returnIndex;
	}

	/**
	 * 向量点乘
	 * 
	 * @param point
	 * @return
	 */
	public static float DotX(Point point) {
		float result;
		result = point.x * point.x + point.y * point.y;
		return result;
	}

	/**
	 * 数乘 增加的X位置 增加的Y位置
	 * 
	 * @param point
	 * @param size
	 * @return
	 */
	public static Point multiply(Point point, float size) {
		point.x = point.x * size;
		point.y = point.y * size;
		return point;
	}

	/**
	 * 单位化
	 * 
	 * @param point
	 * @return
	 */
	public static Point Nomalize(Point point) {
		float len = length(point);
		if (len == 0)
			return point;
		point.x = point.x / len;
		point.y = point.y / len;
		return point;
	}

	/**
	 * 取模
	 * 
	 * @param point
	 * @return
	 */
	public static float length(Point point) {
		float len;
		len = (float) Math
				.sqrt((double) (point.x * point.x + point.y * point.y));
		return len;
	}

	/**
	 * 向量的加法
	 * 
	 * @param endPoint
	 * @param startPoint
	 * @return
	 */
	public static Point add(Point endPoint, Point startPoint) {
		float x = endPoint.x + startPoint.x;
		float y = endPoint.y + startPoint.y;
		Point point = new Point(x, y);
		return point;
	}

	/**
	 * 向量的减法
	 * 
	 * @param endPoint
	 * @param startPoint
	 * @return
	 */
	public static Point sub(Point endPoint, Point startPoint) {
		float x = endPoint.x - startPoint.x;
		float y = endPoint.y - startPoint.y;
		Point point = new Point(x, y);
		return point;
	}

	/**
	 * 向量的除法
	 * 
	 * @param point
	 * @param divisor
	 * @return
	 */
	public static Point div(Point point, float divisor) {
		point.x = point.x / divisor;
		point.y = point.y / divisor;
		return point;
	}
	/**
	 * 时间转化为金钱
	 * @param second 秒  
	 * 1分钟1金币
	 * @return
	 */
	public static int timeToGold(int second){
		return CountUtil.ceil(second/60d)*1;
	}

	
	public static float getLength(Point startPoint,Point endPoint){
		Point point = sub(endPoint, startPoint);
		return CountUtil.length(point);
	}
	
	public static void main(String[] args) {

//		 Point startPoint = new Point(1,1);
//		 Point endPoint = new Point(2,1);
//		 float len1 = CountUtil.getLength(startPoint, endPoint);
//		 System.out.println(len1);

		// System.out.println(len1);
		// float[] pointArr = new float[6];
		// pointArr[0] = 0;
		// pointArr[1] = 0;
		// pointArr[2] = 0;
		// pointArr[3] = 10;
		// pointArr[4] = 2;
		// pointArr[5] = 0;
		// Point wuti = new Point(0,0);
		// Point point1 = new Point(pointArr[0], pointArr[1]);
		// Point point2 = new Point(pointArr[2], pointArr[3]);
		// Point point3 = new Point(pointArr[4], pointArr[5]);
		// float speed = 0.1f;
		// boolean flag = true;
		// while(flag){
		// try {
		//
		// Thread.sleep(3000);
		// Point calulation = new Point(0,0);
		// calulation = CountUtil.sub(point2, wuti);
		// calulation.setX(point2.getX());
		// calulation.setY(point2.getY());
		// CountUtil.Nomalize(calulation);
		// CountUtil.multiply(calulation, 1f);
		// wuti.x =wuti.x+calulation.getX();
		// wuti.y =wuti.y+ calulation.getY();
		//
		// if(wuti.x==point2.getX()&&wuti.y==point2.getY()){
		// flag = false;
		// System.out.println("stop-------"+wuti.x+"        "+wuti.y);
		// }
		// System.out.println("moving-------"+wuti.x+"        "+wuti.y);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// float len = CountUtil.length(new Point(10, 10));
		// System.out.println(len);
		// Thread thread = new Thread(new SpeedCount());
		// thread.start();
		
		float a = 800f;
		System.out.println((float)800/1000);
	}

}
