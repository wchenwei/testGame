package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("task_config")
public class TaskConfigTemplate {
	private Integer id;
	private Integer task_type;
	private Integer task_line;
	private Integer base_open;
	private Integer next_task;
	private String task_icon;
	private String task_name;
	private String task_sec;
	private String task_finish;
	private String task_reward;
	private Integer level_limit;
	private Integer goto_ui;
	private Integer finish_num;
	private Integer order;
}
