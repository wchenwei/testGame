package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("master_base_buff")
public class MasterBaseBuffTemplate {
	private Integer id;
	private Integer lv_master;
	private Integer quality;
	private Integer type;
	private String icon;
	private Integer time_last;
	private String cost;
	private Float buff_add;
	private Integer fe;
	private Integer elec;
	private Integer oli;
	private Integer gold;
	private Integer time_reduce;
	private Integer add_time;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getLv_master() {
		return lv_master;
	}

	public void setLv_master(Integer lv_master) {
		this.lv_master = lv_master;
	}
	public Integer getQuality() {
		return quality;
	}

	public void setQuality(Integer quality) {
		this.quality = quality;
	}
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Integer getTime_last() {
		return time_last;
	}

	public void setTime_last(Integer time_last) {
		this.time_last = time_last;
	}
	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}
	public Float getBuff_add() {
		return buff_add;
	}

	public void setBuff_add(Float buff_add) {
		this.buff_add = buff_add;
	}
	public Integer getFe() {
		return fe;
	}

	public void setFe(Integer fe) {
		this.fe = fe;
	}
	public Integer getElec() {
		return elec;
	}

	public void setElec(Integer elec) {
		this.elec = elec;
	}
	public Integer getOli() {
		return oli;
	}

	public void setOli(Integer oli) {
		this.oli = oli;
	}
	public Integer getGold() {
		return gold;
	}

	public void setGold(Integer gold) {
		this.gold = gold;
	}
	public Integer getTime_reduce() {
		return time_reduce;
	}

	public void setTime_reduce(Integer time_reduce) {
		this.time_reduce = time_reduce;
	}
	public Integer getAdd_time() {
		return add_time;
	}

	public void setAdd_time(Integer add_time) {
		this.add_time = add_time;
	}
}
