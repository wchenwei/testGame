package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("active_three_day")
public class ActiveThreeDayTemplate {
	private Integer index;
	private Integer day;
	private Integer tab_num;
	private String tab_name;
	private Integer tab_index;
	private Integer type;
	private Integer value;
	private String task_desc;
	private String reward;
	private Integer discount;
	private Integer scores;

}
