package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("reform_base")
public class ReformBaseTemplate {
	private Integer index;
	private Integer reform_type;
	private Integer reform_level;
	private Integer quality;
	private String reform_resource;
	private String unlock_level;
	private Integer cash_cost;
	private String part_attr;
	private String reform_attr;

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
	public Integer getReform_type() {
		return reform_type;
	}

	public void setReform_type(Integer reform_type) {
		this.reform_type = reform_type;
	}
	public Integer getReform_level() {
		return reform_level;
	}

	public void setReform_level(Integer reform_level) {
		this.reform_level = reform_level;
	}
	public Integer getQuality() {
		return quality;
	}

	public void setQuality(Integer quality) {
		this.quality = quality;
	}
	public String getReform_resource() {
		return reform_resource;
	}

	public void setReform_resource(String reform_resource) {
		this.reform_resource = reform_resource;
	}
	public String getUnlock_level() {
		return unlock_level;
	}

	public void setUnlock_level(String unlock_level) {
		this.unlock_level = unlock_level;
	}
	public Integer getCash_cost() {
		return cash_cost;
	}

	public void setCash_cost(Integer cash_cost) {
		this.cash_cost = cash_cost;
	}
	public String getPart_attr() {
		return part_attr;
	}

	public void setPart_attr(String part_attr) {
		this.part_attr = part_attr;
	}
	public String getReform_attr() {
		return reform_attr;
	}

	public void setReform_attr(String reform_attr) {
		this.reform_attr = reform_attr;
	}
}
