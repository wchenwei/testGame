package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_81_search")
public class Active81SearchTemplate {
	private Integer id;
	private Integer collect_time_once;
	private String drop;
	private Integer weight_gold;
	private Integer weight_free;
	private Integer refresh_limit;
	private String drop_last;
	private Integer rare;
	private String txt;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCollect_time_once() {
		return collect_time_once;
	}

	public void setCollect_time_once(Integer collect_time_once) {
		this.collect_time_once = collect_time_once;
	}
	public String getDrop() {
		return drop;
	}

	public void setDrop(String drop) {
		this.drop = drop;
	}
	public Integer getWeight_gold() {
		return weight_gold;
	}

	public void setWeight_gold(Integer weight_gold) {
		this.weight_gold = weight_gold;
	}
	public Integer getWeight_free() {
		return weight_free;
	}

	public void setWeight_free(Integer weight_free) {
		this.weight_free = weight_free;
	}
	public Integer getRefresh_limit() {
		return refresh_limit;
	}

	public void setRefresh_limit(Integer refresh_limit) {
		this.refresh_limit = refresh_limit;
	}
	public String getDrop_last() {
		return drop_last;
	}

	public void setDrop_last(String drop_last) {
		this.drop_last = drop_last;
	}
	public Integer getRare() {
		return rare;
	}

	public void setRare(Integer rare) {
		this.rare = rare;
	}
	public String getTxt() {
		return txt;
	}

	public void setTxt(String txt) {
		this.txt = txt;
	}
}
