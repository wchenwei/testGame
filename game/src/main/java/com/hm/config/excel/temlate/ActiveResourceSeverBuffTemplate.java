package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_resource_sever_buff")
public class ActiveResourceSeverBuffTemplate {
	private Integer id;
	private Integer sever_lv_down;
	private Integer sever_lv_up;
	private Float rate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getSever_lv_down() {
		return sever_lv_down;
	}

	public void setSever_lv_down(Integer sever_lv_down) {
		this.sever_lv_down = sever_lv_down;
	}
	public Integer getSever_lv_up() {
		return sever_lv_up;
	}

	public void setSever_lv_up(Integer sever_lv_up) {
		this.sever_lv_up = sever_lv_up;
	}
	public Float getRate() {
		return rate;
	}

	public void setRate(Float rate) {
		this.rate = rate;
	}
}
