package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_army_race_ticket")
public class ActiveArmyRaceTicketTemplate {
	private Integer id;
	private String item_need;
	private String star_random;
	private String company_name;
	private String price;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getItem_need() {
		return item_need;
	}

	public void setItem_need(String item_need) {
		this.item_need = item_need;
	}
	public String getStar_random() {
		return star_random;
	}

	public void setStar_random(String star_random) {
		this.star_random = star_random;
	}
	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
}
