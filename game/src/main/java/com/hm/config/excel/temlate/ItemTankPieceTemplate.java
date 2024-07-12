package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("item_tank_piece")
public class ItemTankPieceTemplate {
	private Integer id;
	private String name;
	private Integer tank;
	private String desc;
	private String desc1;
	private String desc2;
	private String icon;
	private Integer quality;
	private Integer rank;
	private String battle_id_list;

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
	public Integer getTank() {
		return tank;
	}

	public void setTank(Integer tank) {
		this.tank = tank;
	}
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getDesc1() {
		return desc1;
	}

	public void setDesc1(String desc1) {
		this.desc1 = desc1;
	}
	public String getDesc2() {
		return desc2;
	}

	public void setDesc2(String desc2) {
		this.desc2 = desc2;
	}
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Integer getQuality() {
		return quality;
	}

	public void setQuality(Integer quality) {
		this.quality = quality;
	}
	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}
	public String getBattle_id_list() {
		return battle_id_list;
	}

	public void setBattle_id_list(String battle_id_list) {
		this.battle_id_list = battle_id_list;
	}
}
