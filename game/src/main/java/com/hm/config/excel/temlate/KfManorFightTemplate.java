package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("kf_manor_fight")
public class KfManorFightTemplate {
	private Integer id;
	private String name;
	private Integer size;
	private Integer location;
	private Integer seize_speed;
	private Integer manor;
	private String npc;
	private Integer num;
	private Integer mission_map;
	private Integer start_point;

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
	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}
	public Integer getLocation() {
		return location;
	}

	public void setLocation(Integer location) {
		this.location = location;
	}
	public Integer getSeize_speed() {
		return seize_speed;
	}

	public void setSeize_speed(Integer seize_speed) {
		this.seize_speed = seize_speed;
	}
	public Integer getManor() {
		return manor;
	}

	public void setManor(Integer manor) {
		this.manor = manor;
	}
	public String getNpc() {
		return npc;
	}

	public void setNpc(String npc) {
		this.npc = npc;
	}
	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}
	public Integer getMission_map() {
		return mission_map;
	}

	public void setMission_map(Integer mission_map) {
		this.mission_map = mission_map;
	}
	public Integer getStart_point() {
		return start_point;
	}

	public void setStart_point(Integer start_point) {
		this.start_point = start_point;
	}

}
