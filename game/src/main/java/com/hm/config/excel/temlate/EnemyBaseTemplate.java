package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("enemy_base")
public class EnemyBaseTemplate {
	private Integer id;
	private Integer type;
	private Integer level;
	private String name;
	private Integer head_icon;
	private Integer head_frame;
	private String enemy_config;
	private String enemy_pos;
	private String reward;
	private Integer military_lv;
	private Integer car_lv;
	private String equipment;
	private Long power;
	private int no_change_morale;
}
