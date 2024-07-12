package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
@FileConfig("active_1001_pass_level")
public class Active1001PassLevelTemplate {
	private Integer id;
	private Integer flowers;
	private Integer flowers_total;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getFlowers() {
		return flowers;
	}

	public void setFlowers(Integer flowers) {
		this.flowers = flowers;
	}
	public Integer getFlowers_total() {
		return flowers_total;
	}

	public void setFlowers_total(Integer flowers_total) {
		this.flowers_total = flowers_total;
	}
}
