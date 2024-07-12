package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("world_project_level")
public class WorldProjectLevelTemplate {
	private Integer id;
	private Long cost;
	private String effect;
	private Integer resource;
	private String reward;
	private String reward_red;
	private String reward_blue;
	private String reward_green;
	private Integer collect;
	private String name;
	private String icon;
	private String desc;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Long getCost() {
		return cost;
	}

	public void setCost(Long cost) {
		this.cost = cost;
	}
	public String getEffect() {
		return effect;
	}

	public void setEffect(String effect) {
		this.effect = effect;
	}
	public Integer getResource() {
		return resource;
	}

	public void setResource(Integer resource) {
		this.resource = resource;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
	public String getReward_red() {
		return reward_red;
	}

	public void setReward_red(String reward_red) {
		this.reward_red = reward_red;
	}
	public String getReward_blue() {
		return reward_blue;
	}

	public void setReward_blue(String reward_blue) {
		this.reward_blue = reward_blue;
	}
	public String getReward_green() {
		return reward_green;
	}

	public void setReward_green(String reward_green) {
		this.reward_green = reward_green;
	}
	public Integer getCollect() {
		return collect;
	}

	public void setCollect(Integer collect) {
		this.collect = collect;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
