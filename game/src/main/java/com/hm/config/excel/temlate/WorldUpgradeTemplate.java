package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("world_upgrade")
public class WorldUpgradeTemplate {
	private Integer id;
	private Integer server_lv;
	private Float city_product;
	private Float war_product;
	private Float answer_product;
	private Float daily_task_add;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getServer_lv() {
		return server_lv;
	}

	public void setServer_lv(Integer server_lv) {
		this.server_lv = server_lv;
	}
	public Float getCity_product() {
		return city_product;
	}

	public void setCity_product(Float city_product) {
		this.city_product = city_product;
	}
	public Float getWar_product() {
		return war_product;
	}

	public void setWar_product(Float war_product) {
		this.war_product = war_product;
	}
	public Float getAnswer_product() {
		return answer_product;
	}

	public void setAnswer_product(Float answer_product) {
		this.answer_product = answer_product;
	}
	public Float getDaily_task_add() {
		return daily_task_add;
	}

	public void setDaily_task_add(Float daily_task_add) {
		this.daily_task_add = daily_task_add;
	}
}
