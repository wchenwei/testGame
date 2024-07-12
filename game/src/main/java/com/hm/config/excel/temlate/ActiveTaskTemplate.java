package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("active_task")
public class ActiveTaskTemplate {
	private Integer task_id;
	private Integer active_id;
	private Integer stage;
	private Integer active_sub_id;
	private Integer day_refresh;
	private Integer task_type;
	private String task_icon;
	private String task_name;
	private String task_sec;
	private String task_finish;
	private String task_reward;
	private Integer task_point;
	private Integer level_limit;
	private Integer goto_ui;
	private int finish_para;
	private Integer last_task;
	private String extra_para;
	private int can_reset;
	private String reset_cost;
	private Integer finish_num;
}
