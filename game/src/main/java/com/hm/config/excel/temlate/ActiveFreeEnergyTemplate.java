package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_free_energy")
public class ActiveFreeEnergyTemplate {
	private Integer active_id;
	private Integer open_time;
	private Integer end_time;
	private String level;
	private String reward_drop;

	public Integer getActive_id() {
		return active_id;
	}

	public void setActive_id(Integer active_id) {
		this.active_id = active_id;
	}
	public Integer getOpen_time() {
		return open_time;
	}

	public void setOpen_time(Integer open_time) {
		this.open_time = open_time;
	}
	public Integer getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Integer end_time) {
		this.end_time = end_time;
	}
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
	public String getReward_drop() {
		return reward_drop;
	}

	public void setReward_drop(String reward_drop) {
		this.reward_drop = reward_drop;
	}
}
