
package com.hm.libcore.util.count;
/**
 * Title: Point.java
 * Description:向量计算单位
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 李飞
 * @date 2014年11月19日 上午10:30:28
 * @version 1.0
 */
public class Point {
	public  float x = 0;
	
	public  float y = 0;
	
	public Point(float x, float y){
		this.x = x;
		
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
}

