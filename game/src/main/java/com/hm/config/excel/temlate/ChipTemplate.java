package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("chip")
public class ChipTemplate {
	private Integer id;
	private Integer treeid;
	private Integer type;
	private Integer position;
	private Integer default_lock;
	private String next_tec;
	private Integer max_level;
	private String value;
	private Integer fragment;
	private String bill;
	private Integer level;
	private Integer star;
	private String name;
	private String icon;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getTreeid() {
		return treeid;
	}

	public void setTreeid(Integer treeid) {
		this.treeid = treeid;
	}
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}
	public Integer getDefault_lock() {
		return default_lock;
	}

	public void setDefault_lock(Integer default_lock) {
		this.default_lock = default_lock;
	}
	public String getNext_tec() {
		return next_tec;
	}

	public void setNext_tec(String next_tec) {
		this.next_tec = next_tec;
	}
	public Integer getMax_level() {
		return max_level;
	}

	public void setMax_level(Integer max_level) {
		this.max_level = max_level;
	}
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	public Integer getFragment() {
		return fragment;
	}

	public void setFragment(Integer fragment) {
		this.fragment = fragment;
	}
	public String getBill() {
		return bill;
	}

	public void setBill(String bill) {
		this.bill = bill;
	}
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getStar() {
		return star;
	}

	public void setStar(Integer star) {
		this.star = star;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
}
