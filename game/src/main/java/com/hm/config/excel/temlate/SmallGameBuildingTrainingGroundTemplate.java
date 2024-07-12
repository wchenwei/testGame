package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("small_game_building_training_ground")
public class SmallGameBuildingTrainingGroundTemplate {
	private Integer id;
	private String name;
	private String npc;
	private String other_npc;
	private String map;

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
	public String getNpc() {
		return npc;
	}

	public void setNpc(String npc) {
		this.npc = npc;
	}
	public String getOther_npc() {
		return other_npc;
	}

	public void setOther_npc(String other_npc) {
		this.other_npc = other_npc;
	}
	public String getMap() {
		return map;
	}

	public void setMap(String map) {
		this.map = map;
	}
}
