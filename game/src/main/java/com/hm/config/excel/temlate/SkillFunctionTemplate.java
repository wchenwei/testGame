package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("skill_function")
public class SkillFunctionTemplate {
	private Integer id;
	private Integer function_effect;
	private Integer function_type;
	private String function_target;
	private String function_trigger;
	private String buff_trigger;
	private Integer continued_frame;
	private Integer frequency;
	private Integer cal_type;
	private String function_value;
	private Integer formula;
	private Integer buff_layers;
	private Integer function_time;
	private Integer cleanbuff;
	private String buff_effect;
	private String buff_effect_down;
	private String color;
	private Integer effect_pos;
	private Integer buff_time;
	private Integer hierarchy;
	private Integer direction;
	private Integer delay;
	private Integer recovery;
	private Integer buff_play_type;
	private String extra_param;
}
