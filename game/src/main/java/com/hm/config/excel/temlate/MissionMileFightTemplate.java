package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("mission_mile_fight")
public class MissionMileFightTemplate {
	private Integer id;
	private Integer type;
	private Integer chapter_id;
	private Integer next_mission;
	private String name;
	private Integer mission_map;
	private Integer main_tank;
	private Integer enemy_wave;
	private Integer start_point;
	private String enemy_config;
	private String enemy_pos;
	private String first_reward;
	private String reward;
	private Integer need_oil;
	private String tank_limit;
    private Long power;
	private Long power_single;
	private Long bottom_power;
	private Integer boss_mission;
	private String record_reward;
}
