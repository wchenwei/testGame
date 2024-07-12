package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_resource_truck")
public class ActiveResourceTruckTemplate {
	private Integer id;
	private String modle;
	private Integer weight;
	private Integer weight_speedup;
	private Integer weight_speeddown;
	private Integer hp;
	private Float speed;
	private Integer resource;
	private String effect;
	private Integer type;
	private String describe;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getModle() {
		return modle;
	}

	public void setModle(String modle) {
		this.modle = modle;
	}
	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	public Integer getWeight_speedup() {
		return weight_speedup;
	}

	public void setWeight_speedup(Integer weight_speedup) {
		this.weight_speedup = weight_speedup;
	}
	public Integer getWeight_speeddown() {
		return weight_speeddown;
	}

	public void setWeight_speeddown(Integer weight_speeddown) {
		this.weight_speeddown = weight_speeddown;
	}
	public Integer getHp() {
		return hp;
	}

	public void setHp(Integer hp) {
		this.hp = hp;
	}
	public Float getSpeed() {
		return speed;
	}

	public void setSpeed(Float speed) {
		this.speed = speed;
	}
	public Integer getResource() {
		return resource;
	}

	public void setResource(Integer resource) {
		this.resource = resource;
	}
	public String getEffect() {
		return effect;
	}

	public void setEffect(String effect) {
		this.effect = effect;
	}
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}
}
