package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("capital_fight_castle")
public class CapitalFightCastleTemplate {
	private Integer id;
	private String catle_name;
	private Integer center;
	private Integer camp;
	private String npc;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getCatle_name() {
		return catle_name;
	}

	public void setCatle_name(String catle_name) {
		this.catle_name = catle_name;
	}
	public Integer getCenter() {
		return center;
	}

	public void setCenter(Integer center) {
		this.center = center;
	}
	public Integer getCamp() {
		return camp;
	}

	public void setCamp(Integer camp) {
		this.camp = camp;
	}
	public String getNpc() {
		return npc;
	}

	public void setNpc(String npc) {
		this.npc = npc;
	}
}
