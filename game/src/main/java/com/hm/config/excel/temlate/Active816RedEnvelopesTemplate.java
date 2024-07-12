package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_816_red_envelopes")
public class Active816RedEnvelopesTemplate {
	private Integer id;
	private Integer server_level_low;
	private Integer server_level_high;
	private String goods;
	private Integer type;
	private Integer limit;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
	public String getGoods() {
		return goods;
	}

	public void setGoods(String goods) {
		this.goods = goods;
	}
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}
}
