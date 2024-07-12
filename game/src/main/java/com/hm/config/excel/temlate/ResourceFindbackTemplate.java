package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("resource_findback")
public class ResourceFindbackTemplate {
	private Integer id;
	private String name;
	private String cost_normal;
	private String cost_gold;

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
	public String getCost_normal() {
		return cost_normal;
	}

	public void setCost_normal(String cost_normal) {
		this.cost_normal = cost_normal;
	}
	public String getCost_gold() {
		return cost_gold;
	}

	public void setCost_gold(String cost_gold) {
		this.cost_gold = cost_gold;
	}
}
