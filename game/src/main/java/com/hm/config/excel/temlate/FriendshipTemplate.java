package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("friendship")
public class FriendshipTemplate {
	private Integer id;
	private Integer tank_id;
	private Integer index;
	private String name;
	private Integer attr_id;
	private Integer friend_num;
	private String friend_id;
	private String attr_value;
	private String cost;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getTank_id() {
		return tank_id;
	}

	public void setTank_id(Integer tank_id) {
		this.tank_id = tank_id;
	}
	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public Integer getAttr_id() {
		return attr_id;
	}

	public void setAttr_id(Integer attr_id) {
		this.attr_id = attr_id;
	}
	public Integer getFriend_num() {
		return friend_num;
	}

	public void setFriend_num(Integer friend_num) {
		this.friend_num = friend_num;
	}
	public String getFriend_id() {
		return friend_id;
	}

	public void setFriend_id(String friend_id) {
		this.friend_id = friend_id;
	}
	public String getAttr_value() {
		return attr_value;
	}

	public void setAttr_value(String attr_value) {
		this.attr_value = attr_value;
	}
	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}
}
