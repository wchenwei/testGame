package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("building_training_npc")
public class BuildingTrainingNpcTemplate {
	private Integer id;
	private Integer type;
	private String resource;
	private Integer atk;
	private Integer hp;
	private Integer speed_move;
	private Integer cd;
	private Integer rate;

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
	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}
	public Integer getAtk() {
		return atk;
	}

	public void setAtk(Integer atk) {
		this.atk = atk;
	}
	public Integer getHp() {
		return hp;
	}

	public void setHp(Integer hp) {
		this.hp = hp;
	}
	public Integer getSpeed_move() {
		return speed_move;
	}

	public void setSpeed_move(Integer speed_move) {
		this.speed_move = speed_move;
	}
	public Integer getCd() {
		return cd;
	}

	public void setCd(Integer cd) {
		this.cd = cd;
	}
	public Integer getRate() {
		return rate;
	}

	public void setRate(Integer rate) {
		this.rate = rate;
	}
}
