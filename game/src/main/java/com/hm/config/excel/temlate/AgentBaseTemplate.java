package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("agent_base")
public class AgentBaseTemplate {
	private Integer id;
	private String name;
	private String animation;
	private String bonesName;
	private String wait_action;
	private String happy_action;
	private String lock_action;
	private String desc;
	private Integer tank_id;
	private String total_attr;
	private String tank_attr;
	private Integer display;
	private Integer item_id;
	private String item_drop;
	private String odds_drop;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getAnimation() {
		return animation;
	}

	public void setAnimation(String animation) {
		this.animation = animation;
	}
	public String getBonesName() {
		return bonesName;
	}

	public void setBonesName(String bonesName) {
		this.bonesName = bonesName;
	}
	public String getWait_action() {
		return wait_action;
	}

	public void setWait_action(String wait_action) {
		this.wait_action = wait_action;
	}
	public String getHappy_action() {
		return happy_action;
	}

	public void setHappy_action(String happy_action) {
		this.happy_action = happy_action;
	}
	public String getLock_action() {
		return lock_action;
	}

	public void setLock_action(String lock_action) {
		this.lock_action = lock_action;
	}
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Integer getTank_id() {
		return tank_id;
	}

	public void setTank_id(Integer tank_id) {
		this.tank_id = tank_id;
	}
	public String getTotal_attr() {
		return total_attr;
	}

	public void setTotal_attr(String total_attr) {
		this.total_attr = total_attr;
	}
	public String getTank_attr() {
		return tank_attr;
	}

	public void setTank_attr(String tank_attr) {
		this.tank_attr = tank_attr;
	}
	public Integer getDisplay() {
		return display;
	}

	public void setDisplay(Integer display) {
		this.display = display;
	}
	public Integer getItem_id() {
		return item_id;
	}

	public void setItem_id(Integer item_id) {
		this.item_id = item_id;
	}

	public String getItem_drop() {
		return item_drop;
	}

	public void setItem_drop(String item_drop) {
		this.item_drop = item_drop;
	}

	public String getOdds_drop() {
		return odds_drop;
	}

	public void setOdds_drop(String odds_drop) {
		this.odds_drop = odds_drop;
	}
}
