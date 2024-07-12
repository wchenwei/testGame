package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_circle")
public class ActiveCircleTemplate {
	private Integer count;
	private Integer get_time;

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	public Integer getGet_time() {
		return get_time;
	}

	public void setGet_time(Integer get_time) {
		this.get_time = get_time;
	}
}
