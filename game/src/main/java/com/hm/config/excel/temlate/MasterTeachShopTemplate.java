package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("master_teach_shop")
public class MasterTeachShopTemplate {
	private Integer id;
	private String item;
	private Integer change_level;
	private String change_cost;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}
	public Integer getChange_level() {
		return change_level;
	}

	public void setChange_level(Integer change_level) {
		this.change_level = change_level;
	}
	public String getChange_cost() {
		return change_cost;
	}

	public void setChange_cost(String change_cost) {
		this.change_cost = change_cost;
	}
}
