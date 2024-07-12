package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("mission_event_war")
public class MissionEventWarTemplate {
	private Integer war_id;
	private String war_name;
	private Integer level;
	private Integer war_map;
	private String main_reward;
	private Integer group_id;
	private Integer group_start;
	private Integer next_id;
	private Integer dif_star;
	private Integer level_need;
	private String war_report;
	private Integer free_help;

	public Integer getWar_id() {
		return war_id;
	}

	public void setWar_id(Integer war_id) {
		this.war_id = war_id;
	}
	public String getWar_name() {
		return war_name;
	}

	public void setWar_name(String war_name) {
		this.war_name = war_name;
	}
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getWar_map() {
		return war_map;
	}

	public void setWar_map(Integer war_map) {
		this.war_map = war_map;
	}
	public String getMain_reward() {
		return main_reward;
	}

	public void setMain_reward(String main_reward) {
		this.main_reward = main_reward;
	}
	public Integer getGroup_id() {
		return group_id;
	}

	public void setGroup_id(Integer group_id) {
		this.group_id = group_id;
	}
	public Integer getGroup_start() {
		return group_start;
	}

	public void setGroup_start(Integer group_start) {
		this.group_start = group_start;
	}
	public Integer getNext_id() {
		return next_id;
	}

	public void setNext_id(Integer next_id) {
		this.next_id = next_id;
	}
	public Integer getDif_star() {
		return dif_star;
	}

	public void setDif_star(Integer dif_star) {
		this.dif_star = dif_star;
	}
	public Integer getLevel_need() {
		return level_need;
	}

	public void setLevel_need(Integer level_need) {
		this.level_need = level_need;
	}
	public String getWar_report() {
		return war_report;
	}

	public void setWar_report(String war_report) {
		this.war_report = war_report;
	}
	public Integer getFree_help() {
		return free_help;
	}

	public void setFree_help(Integer free_help) {
		this.free_help = free_help;
	}
}
