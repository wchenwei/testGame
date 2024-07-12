package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("active_main")
public class ActiveMainTemplate {
	private Integer active_id;
	private String name;
	private Integer sort_value;
	private Integer open_type;
	private String config_name;
	private String lang1;
	private String lang2;
	private Integer canbe_config;
	private Integer cal_type;
	private Integer active_list;
	private String unlock;
}
