package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_816_formula")
public class Active816FormulaTemplate {
	private Integer id;
	private Integer type;
	private Integer server_level_low;
	private Integer server_level_high;
	private String product;
	private String name;
	private String dec;
	private String icon;
	private String formula;

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
	public Integer getServer_level_low() {
		return server_level_low;
	}

	public void setServer_level_low(Integer server_level_low) {
		this.server_level_low = server_level_low;
	}
	public Integer getServer_level_high() {
		return server_level_high;
	}

	public void setServer_level_high(Integer server_level_high) {
		this.server_level_high = server_level_high;
	}
	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getDec() {
		return dec;
	}

	public void setDec(String dec) {
		this.dec = dec;
	}
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}
}
