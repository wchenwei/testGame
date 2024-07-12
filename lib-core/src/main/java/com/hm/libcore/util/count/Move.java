
package com.hm.libcore.util.count;

import java.util.List;

/**
 * Title: Move.java
 * Description:
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 李飞
 * @date 2014年11月21日 上午10:16:16
 * @version 1.0
 */
public class Move {
	
	public Point move(List<Point> pointArr,Point wuti,long costTime){
		float moveSize = (float) (100f*costTime*0.001);//当前时间内移动距离
		int index = 0;
		for(int i = 0;i<pointArr.size();i++){
			float wuti2endPointLen =0f; //当前物体到目标距离
			Point endPoint1 = pointArr.get(index);//获取第一个结束点
			Point newPoint = new Point(0,0);
			newPoint = CountUtil.sub(endPoint1, wuti);
			wuti2endPointLen = CountUtil.length(newPoint);
			if(moveSize>wuti2endPointLen){
				if(pointArr.size()-1>i){
					moveSize = moveSize - wuti2endPointLen;
					wuti = endPoint1;
					index+=1;
					continue;
				}else{
					wuti = endPoint1;
					index+=1;
					break;
				}
			}else{
				CountUtil.Nomalize(newPoint);
				CountUtil.multiply(newPoint, moveSize);
				wuti.setX(wuti.x +newPoint.getX());
				wuti.setY(wuti.y +newPoint.getY());
				break;
			}
		}
		
		for(int i = 0;i<index;i++){
			pointArr.remove(0);
		}
		index =0;
		return wuti;
//			
//			newPoint.setX(endPoint1.getX());
//			newPoint.setY(endPoint1.getY());
//			CountUtil.Nomalize(newPoint);
//			System.out.println("移动小循环方法时间--------"+costTime);
//			CountUtil.multiply(newPoint, 0.001f*costTime);
//			System.out.println("物体移动前的位置-----------"+wuti.getX()+"          "+wuti.getY());
//			wuti.setX(wuti.x +newPoint.getX());
//			wuti.setY(wuti.y +newPoint.getY());
//			System.out.println("移动后的物体位置-----"+wuti.getX()+"                "+wuti.getY());
//			//已经走到终点
//			if(wuti.x>=pointArr.get(pointArr.size()-1).getX()&&wuti.y>=pointArr.get(pointArr.size()-1).getY()){
//				System.out.println("已经走到终点");
//				wuti.x = pointArr.get(pointArr.size()-1).getX();
//				wuti.y = pointArr.get(pointArr.size()-1).getY();
//				System.out.println(wuti.x+"                "+wuti.y);
//				System.out.println("move end-------------------");
//				break;
//			}
//			//第一个节点都还没到
//			if(wuti.x<=endPoint1.getX()&&wuti.y<=endPoint1.getY()){
//				System.out.println("第一个节点都还没到");
//				break;
//			}
//			//第一点到了 还有N个点
//			if(wuti.x>=endPoint1.getX()&&wuti.y>=endPoint1.getY()){
//				System.out.println("还有下一个点");
//				wuti.setX(endPoint1.getX());
//				wuti.setY(endPoint1.getY());
//				pointArr.remove(i);
//			}
//		}
////		for(Integer index:pointindex){
////			pointArr.remove(index);
////		}
	}
	
	private void moveCore(List<Point> pointArr,int i,Point wuti,long costTime){
		Point endPoint1 = pointArr.get(i);//获取第一个结束点
		Point newPoint = new Point(0,0);
		newPoint = CountUtil.sub(endPoint1, wuti);
		newPoint.setX(endPoint1.getX());
		newPoint.setY(endPoint1.getY());
		CountUtil.Nomalize(newPoint);
		CountUtil.multiply(newPoint, 1f*costTime);
		wuti.x =wuti.x +newPoint.getX();
		wuti.y =wuti.y +newPoint.getY();
		if(wuti.x>=pointArr.get(pointArr.size()-1).getX()&&wuti.y>=pointArr.get(pointArr.size()-1).getY()){
			System.out.println("move end-------------------");
		}
	}
	
	

}

