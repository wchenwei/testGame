package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("mission_single_level")
public class MissionSingleLevelTemplate {
	private Integer id;
	private Integer type;
	private Integer stage;
	private Integer big_stage;
	private Integer next_lv;
	private String event_library;

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
	public Integer getStage() {
		return stage;
	}

	public void setStage(Integer stage) {
		this.stage = stage;
	}
	public Integer getBig_stage() {
		return big_stage;
	}

	public void setBig_stage(Integer big_stage) {
		this.big_stage = big_stage;
	}
	public Integer getNext_lv() {
		return next_lv;
	}

	public void setNext_lv(Integer next_lv) {
		this.next_lv = next_lv;
	}
	public String getEvent_library() {
		return event_library;
	}

	public void setEvent_library(String event_library) {
		this.event_library = event_library;
	}
}
