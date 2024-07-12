package com.hm.observer;

import lombok.Data;

/**
 * @Description: 观察者排序
 * @author siyunlong  
 * @date 2018年5月24日 下午3:13:09 
 * @version V1.0
 */
@Data
public class SortObserver{
	private IObserver observer;
	private int sort;
	
	public SortObserver(IObserver observer, int sort) {
		this.observer = observer;
		this.sort = sort;
	}

	public IObserver getObserver() {
		return observer;
	}

	public int getSort() {
		return sort;
	}

}
