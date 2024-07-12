package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
@FileConfig("active_cvout_map")
public class ActiveCvoutMapTemplate {
	private Integer map_id;
	private String map_name;
	private String map_resource;
	private String reward;
	private String formula;

	public Integer getMap_id() {
		return map_id;
	}

	public void setMap_id(Integer map_id) {
		this.map_id = map_id;
	}
	public String getMap_name() {
		return map_name;
	}

	public void setMap_name(String map_name) {
		this.map_name = map_name;
	}
	public String getMap_resource() {
		return map_resource;
	}

	public void setMap_resource(String map_resource) {
		this.map_resource = map_resource;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}
}
