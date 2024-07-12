package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("mission")
public class MissionTemplate {
	private Integer id;
	private Integer city;
	private Integer type;
	private Integer mission_number;
	private Integer next_mission;
	private Integer unlock_city;
	private Integer unlock_level;
	private String icon;
	private String name;
	private String desc1;
	private String desc2;
	private Integer enemy_wave;
	private Integer start_point;
	private String enemy_config;
	private String enemy_pos;
	private Integer player_exp;
	private Integer tank_exp;
	private String reward;
	private String else_reward;
	private Integer mission_map;
	private String cost;
	private Integer need_oil;
	private String dialogue;
	private String boss_who;
	private Integer boss_music;
	private Integer bottom_power;
	private String record_reward;
	private String mission_reward;
	private String box_reward_base;
}
