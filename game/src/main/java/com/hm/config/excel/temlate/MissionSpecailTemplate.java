package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("mission_specail")
public class MissionSpecailTemplate {
	private Integer id;
	private Integer type;
	private Integer chapter_id;
	private String open_day;
	private Integer before_mission;
	private Integer next_mission;
	private Integer unlock_level;
	private String icon;
	private String name;
	private Integer mission_map;
	private Integer main_tank;
	private Integer enemy_wave;
	private Integer start_point;
	private String enemy_config;
	private String enemy_pos;
	private Integer player_exp;
	private Integer first_player_exp;
	private Integer tank_exp;
	private String reward;
	private String first_reward;
	private String advanced_reward;
	private String turn_card;
	private Integer oil_cost;
	private Integer need_oil;
	private String tank_limit;
    private Long power;
    private Long power_single;
    private Long bottom_power;
	private Integer power_limit;
	private Integer boss_mission;
	private String next_limit;
	private String desc2;
	private String record_reward;
	private String day_reward;
	private String sweep_reward;
	private Integer level_number;
}
