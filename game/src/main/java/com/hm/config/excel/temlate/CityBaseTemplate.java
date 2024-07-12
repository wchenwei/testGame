package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("city_base")
public class CityBaseTemplate {
	private Integer id;
	private String name;
	private Integer map;
	private Integer area;
	private Integer city_type;
	private Integer unlock_level;
	private String link_city;
	private String misson_id;
	private String armory_id;
	private Integer main_tank;
	private String tank_paper;
	private String turn_card;
	private String resource;
	private String special_resource;
	private Integer armory_part;
	private Integer guardin_level;
	private String guardin_type;
	private Integer mission_map;
	private Integer npc_level_diff;
	private Integer chapter;
	private Integer seaside;
	private String release_reward;
	private String rebal_display;
	private Integer city_test_type;
	private Integer achievements;
	private Integer city_terrain;
	private int morale;
	private String new_levy_res;
	private String new_levy_item;
}
