package com.hm.util;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Random;

// ************************************************************************
// math helpers

public class MathUtils
{
	
	// ************************************************************************
	// public methods
	
	// ------------------------------------------------------------------------
	public static int RoundToInt( double value )
	{
		double	absValue		= Math.abs( value );
		int		roundedAbsValue	= ( int ) Math.round( absValue );
		
		if( roundedAbsValue - absValue == 0.5 )
		{
			// mimicking Math.Round from C#: If the fractional component of value is halfway between two integers,
			// one of which is even and the other odd, then the even number is returned.
			roundedAbsValue -= roundedAbsValue % 2;
		}
		
		return ( int )( Math.signum( value ) * roundedAbsValue );
	}
	
	// ------------------------------------------------------------------------
	public static int Clamp( int value, int min, int max )
	{
		return Math.min( Math.max( value, min ), max );
	}
	
	// ------------------------------------------------------------------------
	public static long Clamp( long value, long min, long max )
	{
		return Math.min( Math.max( value, min ), max );
	}
	
	// ------------------------------------------------------------------------
	public static float Clamp( float value, float min, float max )
	{
		return Math.min( Math.max( value, min ), max );
	}
	
	// ------------------------------------------------------------------------
	public static float Clamp01( float value )
	{
		return Clamp( value, 0.0f, 1.0f );
	}
	
	// ------------------------------------------------------------------------
	/*public static < T extends Comparable< T > > T Clamp( T value, T min, T max  )
	{
		if( value.compareTo( min ) < 0 )
		{
			return min;
		}
		if( value.compareTo( max ) > 0 )
		{
			return max;
		}
		return value;
	}*/
	
	// ------------------------------------------------------------------------
	public static boolean BitTest( int flags, int mask )
	{
		return mask == ( flags & mask );
	}
	
	// ------------------------------------------------------------------------
	public static boolean Approximately( float a, float b )
	{
		return Math.abs( b - a ) < Math.max ( 1E-06f * Math.max ( Math.abs ( a ), Math.abs ( b ) ), 1.121039E-44f ); // c# version
	}
	
	// ------------------------------------------------------------------------
	public static boolean Approx0( float x )
	{
		return ( - 1.0e-6f < x ) && ( x < + 1.0e-6f );
	}
	
	// ------------------------------------------------------------------------
	public static float Lerp( float from, float to, float value )
	{
		return from + value * ( to - from );
	}
	
	// ------------------------------------------------------------------------
	/** Returns random number between 0 (inclusive) and <b>max</b> (exclusive) with
	 *  probability distribution using exponential function.
	 *
	 *  @param max upper bound on random number to be returned - must be possitive
	 *  @throws IllegalArgumentException if <b>max</b> is not positive
	 */
	public static int NextIntExponential( int max )
	{
		if ( max < 1 )
		{
			throw new IllegalArgumentException( "Max must be > 1" );
		}
		
		double rand = new Random().nextDouble();
		int result = ( int ) Math.round( Math.exp( rand * Math.log( max ) ) ) - 1;
		
		return Math.max( 0,  result ); // in case of long -> int overflow
	}
	
	public static int parseInt(String s) {
		try {
			return Integer.parseInt(s);
		} catch (Exception e) {
		}
		return 0;
	}
	
	public static double parseDouble(String s) {
		try {
			return Double.parseDouble(s);
		} catch (Exception e) {
		}
		return 0;
	}
	/**
	 * 提供精确加法计算的add方法
	 * 
	 * @param value1
	 *            被加数
	 * @param value2
	 *            加数
	 * @return 两个参数的和
	 */
	public static double add(double value1, double value2) {
		BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
		BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * 提供精确减法运算的sub方法
	 * 
	 * @param value1
	 *            被减数
	 * @param value2
	 *            减数
	 * @return 两个参数的差
	 */
	public static double sub(double value1, double value2) {
		BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
		BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 提供精确乘法运算的mul方法
	 * 
	 * @param value1
	 *            被乘数
	 * @param value2
	 *            乘数
	 * @return 两个参数的积
	 */
	public static double mul(double value1, double value2) {
		BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
		BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
		return b1.multiply(b2).doubleValue();
	}
	
	public static long mul(long v,double rate) {
		BigDecimal b1 = new BigDecimal(v);
		BigDecimal b2 = new BigDecimal(rate);
		return b2.multiply(b1).longValue();
	}
	
	public static double mul(double... dv) {
		if(dv.length < 2) {
			return dv[0];
		}
		BigDecimal v = new BigDecimal(Double.valueOf(dv[0]));
		for (int i = 1; i < dv.length; i++) {
			BigDecimal b = new BigDecimal(Double.valueOf(dv[i]));
			v = v.multiply(b);
		}
		return v.doubleValue();
	}

	private static final int DEF_DIV_SCALE = 10;  
	/**
	* 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
	* 小数点以后10位，以后的数字四舍五入。
	* @param v1 被除数
	* @param v2 除数
	* @return 两个参数的商
	*/
	public static double div(double v1, double v2) {
	   return div(v1, v2, DEF_DIV_SCALE);
	}
	
	public static double div(long v1, double v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	* 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
	* 定精度，以后的数字四舍五入。
	* @param v1 被除数
	* @param v2 除数
	* @param scale 表示表示需要精确到小数点以后几位。
	* @return 两个参数的商
	*/
	public static double div(double v1, double v2, int scale) {
	   if (scale < 0) {
		   scale = DEF_DIV_SCALE;
	   }
	   BigDecimal b1 = new BigDecimal(Double.toString(v1));
	   BigDecimal b2 = new BigDecimal(Double.toString(v2));
	   return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	* 提供精确的小数位四舍五入处理。
	* @param v 需要四舍五入的数字
	* @param scale 小数点后保留几位
	* @return 四舍五入后的结果
	*/
	public static double round(double v, int scale) {
	   if (scale < 0) {
		   scale = DEF_DIV_SCALE;
	   }
	   BigDecimal b = new BigDecimal(Double.toString(v));
	   BigDecimal one = new BigDecimal("1");
	   return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	/**
	 * 百分比随机
	 * @param value 值
	 * @param minPercent 小百分比
	 * @param maxPercent 大百分比
	 * @return
	 */
	public static int percentageRandom(double value,float minPercent,float maxPercent) {
		int maxValue = (int)mul(value, maxPercent);
		int minValue = (int)mul(value, minPercent);
		
		int v = maxValue > minValue?(maxValue-minValue):0;
		if(v > 0) {
			return new Random().nextInt(v)+minValue;
		}
		return minValue;
	}
	/**
	 *对数运算，计算以num2为底，num1的对数 
	 */
	public static double log(double num1,double num2){
		return Math.log(num1)/Math.log(num2);
	}
	
	public static int min(int a,int b,int c) {
		return Math.min(Math.min(a, b), c);
	}
	public static int max(int a,int b,int c) {
		return Math.max(Math.max(a, b), c);
	}
	public static int max(Collection<Integer> colls) {
		int max = 0;
		for (int o : colls) {
			if(o>max) {
				max = o;
			}
		}
		return max;
	}
	//随机N-M的整数(包含start不包含end)
	public static int random(int start,int end){
		return (int)(start+Math.random()*(end-start));
	}
//	public static int randomInt
	
	public static void main(String[] args) {
		System.out.println(MathUtils.random(10, 11));
	}
}
