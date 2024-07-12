package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("enemy_rebal_type")
public class EnemyRebalTypeTemplate {
	private Integer type;
	private String reward;
	private Integer wave;
	private Integer atk_city;
	private String guardin_type;
	private String head_icon;
	private String name;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
	public Integer getWave() {
		return wave;
	}

	public void setWave(Integer wave) {
		this.wave = wave;
	}
	public Integer getAtk_city() {
		return atk_city;
	}

	public void setAtk_city(Integer atk_city) {
		this.atk_city = atk_city;
	}
	public String getGuardin_type() {
		return guardin_type;
	}

	public void setGuardin_type(String guardin_type) {
		this.guardin_type = guardin_type;
	}
	public String getHead_icon() {
		return head_icon;
	}

	public void setHead_icon(String head_icon) {
		this.head_icon = head_icon;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
