
package com.hm.libcore.util.count;

import java.util.ArrayList;
import java.util.List;

/**
 * Title: SpeedCount.java
 * Description:
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 李飞
 * @date 2014年11月21日 上午9:37:44
 * @version 1.0
 */
public class SpeedCount implements Runnable{

	@Override
	public void run() {
		long costTime = 0;
		Move move = new Move();
		List<Point> pointArr = new ArrayList<Point>();
		pointArr.add(new Point(0,10));
		pointArr.add(new Point(3,10));
		pointArr.add(new Point(3,7));
		pointArr.add(new Point(6,10));
		pointArr.add(new Point(6,3));
		Point wuti = new Point(0, 0);
		while(true){
			long startTime = System.currentTimeMillis();
			wuti = move.move(pointArr,wuti, costTime);
			long endTime = System.currentTimeMillis();
			System.out.println("物体坐标"+wuti.getX()+"               "+wuti.getY());
			
			costTime = endTime - startTime;
			try {
				if(30-costTime>0){
					Thread.sleep(30-costTime);
					costTime = 30;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
//			System.out.println(endTime);
			
			System.out.println("大while循环的时间-----"+costTime);
			
			
//			System.out.println("---------------"+costTime);
			
			
		}
		
	}

}

