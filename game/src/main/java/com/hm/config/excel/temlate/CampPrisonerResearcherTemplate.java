package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("camp_prisoner_researcher")
public class CampPrisonerResearcherTemplate {
	private Integer id;
	private String name;
	private String desc;
	private String resource;
	private String hire_price;
	private Integer last_time;
	private String extra_drop;

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
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}
	public String getHire_price() {
		return hire_price;
	}

	public void setHire_price(String hire_price) {
		this.hire_price = hire_price;
	}
	public Integer getLast_time() {
		return last_time;
	}

	public void setLast_time(Integer last_time) {
		this.last_time = last_time;
	}
	public String getExtra_drop() {
		return extra_drop;
	}

	public void setExtra_drop(String extra_drop) {
		this.extra_drop = extra_drop;
	}
}
