package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("player_head")
public class PlayerHeadTemplate {
	private Integer id;
	private String name;
	private String icon;
	private String icon_big;
	private Integer cost_general;
	private String cost;
	private Integer can_use;
	private Integer activity_get;
	private Integer time;

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
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getIcon_big() {
		return icon_big;
	}

	public void setIcon_big(String icon_big) {
		this.icon_big = icon_big;
	}
	public Integer getCost_general() {
		return cost_general;
	}

	public void setCost_general(Integer cost_general) {
		this.cost_general = cost_general;
	}
	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}
	public Integer getCan_use() {
		return can_use;
	}

	public void setCan_use(Integer can_use) {
		this.can_use = can_use;
	}
	public Integer getActivity_get() {
		return activity_get;
	}

	public void setActivity_get(Integer activity_get) {
		this.activity_get = activity_get;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}
}
