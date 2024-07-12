package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("guide_config")
public class GuideConfigTemplate {
	private Integer guide_id;
	private Integer next_id;
	private Integer recovery_id;
	private Integer mission_id;
	private String start;
	private String over;
	private String action;
	private String value;
	private String record_id;

	public Integer getGuide_id() {
		return guide_id;
	}

	public void setGuide_id(Integer guide_id) {
		this.guide_id = guide_id;
	}
	public Integer getNext_id() {
		return next_id;
	}

	public void setNext_id(Integer next_id) {
		this.next_id = next_id;
	}
	public Integer getRecovery_id() {
		return recovery_id;
	}

	public void setRecovery_id(Integer recovery_id) {
		this.recovery_id = recovery_id;
	}
	public Integer getMission_id() {
		return mission_id;
	}

	public void setMission_id(Integer mission_id) {
		this.mission_id = mission_id;
	}
	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}
	public String getOver() {
		return over;
	}

	public void setOver(String over) {
		this.over = over;
	}
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	public String getRecord_id() {
		return record_id;
	}

	public void setRecord_id(String record_id) {
		this.record_id = record_id;
	}
}
