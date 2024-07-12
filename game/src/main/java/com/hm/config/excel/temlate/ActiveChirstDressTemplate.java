package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_chirst_dress")
public class ActiveChirstDressTemplate {
	private Integer id;
	private Integer wish_id;
	private Integer location;
	private String cost;
	private String login;
	private Integer show_type;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getWish_id() {
		return wish_id;
	}

	public void setWish_id(Integer wish_id) {
		this.wish_id = wish_id;
	}
	public Integer getLocation() {
		return location;
	}

	public void setLocation(Integer location) {
		this.location = location;
	}
	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	public Integer getShow_type() {
		return show_type;
	}

	public void setShow_type(Integer show_type) {
		this.show_type = show_type;
	}
}
