package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("expedition")
public class ExpeditionTemplate {
	private Integer id;
	private Integer index;
	private String name;
	private Integer player_lv_down;
	private Integer player_lv_up;
	private Integer player_exp;
	private Integer tank_exp;
	private String war_reward;
	private String turn_card;
	private String box_reward;
	private String box_display;
	private Integer mission_map;
	private Integer start_point;
	private Integer enemy_wave;
	private String three_pass;
	private String six_pass;
	private String nine_pass;
	private String twelve_pass;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPlayer_lv_down() {
		return player_lv_down;
	}

	public void setPlayer_lv_down(Integer player_lv_down) {
		this.player_lv_down = player_lv_down;
	}

	public Integer getPlayer_lv_up() {
		return player_lv_up;
	}

	public void setPlayer_lv_up(Integer player_lv_up) {
		this.player_lv_up = player_lv_up;
	}

	public Integer getPlayer_exp() {
		return player_exp;
	}

	public void setPlayer_exp(Integer player_exp) {
		this.player_exp = player_exp;
	}
	public Integer getTank_exp() {
		return tank_exp;
	}

	public void setTank_exp(Integer tank_exp) {
		this.tank_exp = tank_exp;
	}
	public String getWar_reward() {
		return war_reward;
	}

	public void setWar_reward(String war_reward) {
		this.war_reward = war_reward;
	}
	public String getTurn_card() {
		return turn_card;
	}

	public void setTurn_card(String turn_card) {
		this.turn_card = turn_card;
	}
	public String getBox_reward() {
		return box_reward;
	}

	public void setBox_reward(String box_reward) {
		this.box_reward = box_reward;
	}
	public String getBox_display() {
		return box_display;
	}

	public void setBox_display(String box_display) {
		this.box_display = box_display;
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
	public Integer getEnemy_wave() {
		return enemy_wave;
	}

	public void setEnemy_wave(Integer enemy_wave) {
		this.enemy_wave = enemy_wave;
	}
	public String getThree_pass() {
		return three_pass;
	}

	public void setThree_pass(String three_pass) {
		this.three_pass = three_pass;
	}
	public String getSix_pass() {
		return six_pass;
	}

	public void setSix_pass(String six_pass) {
		this.six_pass = six_pass;
	}
	public String getNine_pass() {
		return nine_pass;
	}

	public void setNine_pass(String nine_pass) {
		this.nine_pass = nine_pass;
	}
	public String getTwelve_pass() {
		return twelve_pass;
	}

	public void setTwelve_pass(String twelve_pass) {
		this.twelve_pass = twelve_pass;
	}
}
