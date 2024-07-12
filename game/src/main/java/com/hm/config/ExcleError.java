package com.hm.config;

import com.google.common.collect.Lists;

import java.util.List;

public class ExcleError {
	public List<String> excleList = Lists.newArrayList();
	public String errorInfo;
	
	public ExcleError(List<String> excleList, String errorInfo) {
		this.excleList = excleList;
		this.errorInfo = errorInfo;
	}
	
	public String getErrInfo() {
		StringBuffer sb = new StringBuffer();
		sb.append("errorInfo");
		
		return sb.toString();
	}
}
