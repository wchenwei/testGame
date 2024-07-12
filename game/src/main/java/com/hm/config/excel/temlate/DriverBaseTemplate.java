package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("driver_base")
public class DriverBaseTemplate {
	private Integer id;
	private String name;
	private String icon;
	private String icon_big;
	private String animation;
	private String animation_name;
	private String action_name;
	private Integer tank_id;
	private Integer soul;
	private Float atk_add;
	private Float def_add;
	private Float hp_add;
	private Float crit_add;
	private Float crit_def_add;
	private String skill;
	private Integer head_unlock;
	private String item_origin;
	private String evolve_attr_rate;
	private String evolve_tec_rate;
	private String evolve_cost;
	private String evolve_addition;

}
