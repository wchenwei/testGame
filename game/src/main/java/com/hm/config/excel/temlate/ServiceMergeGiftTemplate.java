package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("service_merge_gift")
public class ServiceMergeGiftTemplate {
	private Integer id;
	private String name;
	private Integer recharge_id;
	private Integer limit;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public Integer getRecharge_id() {
		return recharge_id;
	}

	public void setRecharge_id(Integer recharge_id) {
		this.recharge_id = recharge_id;
	}
	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}
}
