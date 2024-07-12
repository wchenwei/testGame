package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("tank_crew_shop")
public class TankCrewShopTemplate {
	private Integer id;
	private Integer open;
	private String label;
	private String shop_items;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getOpen() {
		return open;
	}

	public void setOpen(Integer open) {
		this.open = open;
	}
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	public String getShop_items() {
		return shop_items;
	}

	public void setShop_items(String shop_items) {
		this.shop_items = shop_items;
	}
}
