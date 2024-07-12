package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_army_race_show")
public class ActiveArmyRaceShowTemplate {
	private Integer id;
	private Integer type;
	private Integer star;
	private String company_name;
	private String company_info;
	private String icon;
	private String goods;
	private String gongsi_icon;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getStar() {
		return star;
	}

	public void setStar(Integer star) {
		this.star = star;
	}
	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public String getCompany_info() {
		return company_info;
	}

	public void setCompany_info(String company_info) {
		this.company_info = company_info;
	}
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getGoods() {
		return goods;
	}

	public void setGoods(String goods) {
		this.goods = goods;
	}
	public String getGongsi_icon() {
		return gongsi_icon;
	}

	public void setGongsi_icon(String gongsi_icon) {
		this.gongsi_icon = gongsi_icon;
	}
}
