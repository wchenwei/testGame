package com.hm.model.page;

import com.google.common.collect.Lists;

import java.util.List;

public class PageBean<T> {
	
	
	
	private long total;
	private List<T> data = Lists.newArrayList();
	public PageBean() {
		super();
	}
	public PageBean(long total, List<T> data) {
		super();
		this.total = total;
		this.data = data;
	}
	public long getTotal() {
		return total;
	}
	public List<T> getData() {
		return data;
	}
	public void setData(List<T> data) {
		this.data = data;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	

}
