package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("mission_event_link")
public class MissionEventLinkTemplate {
	private Integer id;
	private Integer war_id;
	private String mission_id_link;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getWar_id() {
		return war_id;
	}

	public void setWar_id(Integer war_id) {
		this.war_id = war_id;
	}
	public String getMission_id_link() {
		return mission_id_link;
	}

	public void setMission_id_link(String mission_id_link) {
		this.mission_id_link = mission_id_link;
	}
}
