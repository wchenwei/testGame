package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("car_driver_unlock")
public class CarDriverUnlockTemplate {
	private Integer id;
	private Integer unlock_lv;
	private String attri;
	private String resource;
	private String name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUnlock_lv() {
		return unlock_lv;
	}

	public void setUnlock_lv(Integer unlock_lv) {
		this.unlock_lv = unlock_lv;
	}
	public String getAttri() {
		return attri;
	}

	public void setAttri(String attri) {
		this.attri = attri;
	}
	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
