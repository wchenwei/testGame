package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("guild_factory_level")
public class GuildFactoryLevelTemplate {
	private Integer id;
	private Integer exp;
	private Integer active_point;
	private Integer part;
	private String product;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getExp() {
		return exp;
	}

	public void setExp(Integer exp) {
		this.exp = exp;
	}
	public Integer getActive_point() {
		return active_point;
	}

	public void setActive_point(Integer active_point) {
		this.active_point = active_point;
	}
	public Integer getPart() {
		return part;
	}

	public void setPart(Integer part) {
		this.part = part;
	}
	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}
}
