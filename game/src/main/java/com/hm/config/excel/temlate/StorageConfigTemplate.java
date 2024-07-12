package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("storage_config")
public class StorageConfigTemplate {
	private Integer id;
	private Integer level_storage;
	private Integer level_base;
	private Integer level_player;
	private String cost;
	private Integer fe;
	private Integer elec;
	private Integer oli;
	private Float rob;
	private Float rob_def;
	private String rebuild;
	private String rebuild_senior;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getLevel_storage() {
		return level_storage;
	}

	public void setLevel_storage(Integer level_storage) {
		this.level_storage = level_storage;
	}
	public Integer getLevel_base() {
		return level_base;
	}

	public void setLevel_base(Integer level_base) {
		this.level_base = level_base;
	}
	public Integer getLevel_player() {
		return level_player;
	}

	public void setLevel_player(Integer level_player) {
		this.level_player = level_player;
	}
	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
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
	public Float getRob() {
		return rob;
	}

	public void setRob(Float rob) {
		this.rob = rob;
	}
	public Float getRob_def() {
		return rob_def;
	}

	public void setRob_def(Float rob_def) {
		this.rob_def = rob_def;
	}
	public String getRebuild() {
		return rebuild;
	}

	public void setRebuild(String rebuild) {
		this.rebuild = rebuild;
	}
	public String getRebuild_senior() {
		return rebuild_senior;
	}

	public void setRebuild_senior(String rebuild_senior) {
		this.rebuild_senior = rebuild_senior;
	}
}
