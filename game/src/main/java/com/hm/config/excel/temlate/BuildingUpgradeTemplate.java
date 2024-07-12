package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("building_upgrade")
public class BuildingUpgradeTemplate {
	private Integer id;
	private Integer build_type;
	private Integer lv_building;
	private Integer time;
	private Integer lv_player;
	private Integer lv_mbuilding;
	private String cost;
	private String building_effect;
	private String effect_des1;
	private String effect_des2;
	private Integer fight_score;
	private String product;
	private String product_time;
	private String product_limit;
	private String product_cost;
	private String rob_resource;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getBuild_type() {
		return build_type;
	}

	public void setBuild_type(Integer build_type) {
		this.build_type = build_type;
	}
	public Integer getLv_building() {
		return lv_building;
	}

	public void setLv_building(Integer lv_building) {
		this.lv_building = lv_building;
	}
	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}
	public Integer getLv_player() {
		return lv_player;
	}

	public void setLv_player(Integer lv_player) {
		this.lv_player = lv_player;
	}
	public Integer getLv_mbuilding() {
		return lv_mbuilding;
	}

	public void setLv_mbuilding(Integer lv_mbuilding) {
		this.lv_mbuilding = lv_mbuilding;
	}
	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}
	public String getBuilding_effect() {
		return building_effect;
	}

	public void setBuilding_effect(String building_effect) {
		this.building_effect = building_effect;
	}
	public String getEffect_des1() {
		return effect_des1;
	}

	public void setEffect_des1(String effect_des1) {
		this.effect_des1 = effect_des1;
	}
	public String getEffect_des2() {
		return effect_des2;
	}

	public void setEffect_des2(String effect_des2) {
		this.effect_des2 = effect_des2;
	}
	public Integer getFight_score() {
		return fight_score;
	}

	public void setFight_score(Integer fight_score) {
		this.fight_score = fight_score;
	}
	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}
	public String getProduct_time() {
		return product_time;
	}

	public void setProduct_time(String product_time) {
		this.product_time = product_time;
	}
	public String getProduct_limit() {
		return product_limit;
	}

	public void setProduct_limit(String product_limit) {
		this.product_limit = product_limit;
	}
	public String getProduct_cost() {
		return product_cost;
	}

	public void setProduct_cost(String product_cost) {
		this.product_cost = product_cost;
	}

	public String getRob_resource() {
		return rob_resource;
	}

	public void setRob_resource(String rob_resource) {
		this.rob_resource = rob_resource;
	}
	
}
