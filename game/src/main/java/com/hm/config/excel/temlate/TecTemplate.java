package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("tec")
public class TecTemplate {
	private Integer id;
	private Integer tec_id;
	private Integer tec_lv;
	private Integer player_level;
	private Integer tecbuild_level;
	private Integer tec_id_pre;
	private Integer tec_id_lv;
	private Float tec_effect;
	private Integer up_stage;
	private String cost;
	private Integer time;
	private Integer num_display;
	private Integer fight_score;
	private Integer is_percent;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getTec_id() {
		return tec_id;
	}

	public void setTec_id(Integer tec_id) {
		this.tec_id = tec_id;
	}
	public Integer getTec_lv() {
		return tec_lv;
	}

	public void setTec_lv(Integer tec_lv) {
		this.tec_lv = tec_lv;
	}
	public Integer getPlayer_level() {
		return player_level;
	}

	public void setPlayer_level(Integer player_level) {
		this.player_level = player_level;
	}
	public Integer getTecbuild_level() {
		return tecbuild_level;
	}

	public void setTecbuild_level(Integer tecbuild_level) {
		this.tecbuild_level = tecbuild_level;
	}
	public Integer getTec_id_pre() {
		return tec_id_pre;
	}

	public void setTec_id_pre(Integer tec_id_pre) {
		this.tec_id_pre = tec_id_pre;
	}
	public Integer getTec_id_lv() {
		return tec_id_lv;
	}

	public void setTec_id_lv(Integer tec_id_lv) {
		this.tec_id_lv = tec_id_lv;
	}
	public Float getTec_effect() {
		return tec_effect;
	}

	public void setTec_effect(Float tec_effect) {
		this.tec_effect = tec_effect;
	}
	public Integer getUp_stage() {
		return up_stage;
	}

	public void setUp_stage(Integer up_stage) {
		this.up_stage = up_stage;
	}
	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}
	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}
	public Integer getNum_display() {
		return num_display;
	}

	public void setNum_display(Integer num_display) {
		this.num_display = num_display;
	}
	public Integer getFight_score() {
		return fight_score;
	}

	public void setFight_score(Integer fight_score) {
		this.fight_score = fight_score;
	}
	public Integer getIs_percent() {
		return is_percent;
	}

	public void setIs_percent(Integer is_percent) {
		this.is_percent = is_percent;
	}
}
