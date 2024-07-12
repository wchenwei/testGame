package com.hm.libcore.soketserver;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**   
 * @Description: 流量统计管理器 
 * @author siyunlong  
 * @date 2017年3月14日 上午9:35:28 
 * @version V1.0   
 */
@Slf4j
public class FlowStatManager {
	private static FlowStatManager instance = new FlowStatManager();
	public static FlowStatManager getInstance() {
		return instance;
	}
	private HashMultiset<Integer> dataMap = HashMultiset.create();
	private HashMultiset<Integer> msgMap = HashMultiset.create();
	private int allSize;
	private int allNum;
	
	private boolean isOpen = true;
	
	public void putData(int id,int size) {
		if(isOpen) {
			dataMap.add(id, size);
			allSize += size;
			allNum++;
			msgMap.add(id);
		}
	}
	public void printInfo() {
		if(isOpen) {
			System.err.println("==============流量统计==================");
			System.err.println(dataMap.toString());
			for (Multiset.Entry<Integer> entry : dataMap.entrySet()) {
				int c = msgMap.count(entry.getElement());
				System.err.println(
						"msgId:"+entry.getElement()
						+"  比例："+div(entry.getCount(), allSize, 4)*100
						+"  个数："+c
						+"  比例："+div(c, allNum, 4)*100
						);
			}
			System.err.println("总消息量："+div(allSize, 1024*1024,4)+"M  总个数："+allNum);
			System.err.println("==============流量统计==================");
		}
	}
	
	public void clearData() {
		this.dataMap.clear();
		this.msgMap.clear();
		this.allNum = 0;
		this.allSize = 0;
	}
	
	public static double div(double v1, double v2, int scale) {
	   if (scale < 0) {
		   scale = 10;
	   }
	   BigDecimal b1 = new BigDecimal(Double.toString(v1));
	   BigDecimal b2 = new BigDecimal(Double.toString(v2));
	   return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	public static void main(String[] args) {
		FlowStatManager manager = FlowStatManager.getInstance();
		manager.putData(1001, 11);
		manager.putData(1001, 12);
		manager.putData(1002, 11);
		manager.printInfo();
	}
}
