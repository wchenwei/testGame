package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("task_box")
public class TaskBoxTemplate {
	private Integer index;
	private Integer pos;
	private Integer level;
	private Integer need_point;
	private String reward;

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
	public Integer getPos() {
		return pos;
	}

	public void setPos(Integer pos) {
		this.pos = pos;
	}
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getNeed_point() {
		return need_point;
	}

	public void setNeed_point(Integer need_point) {
		this.need_point = need_point;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
}
