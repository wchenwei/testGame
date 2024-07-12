package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@FileConfig("active_6year_pass")
@Data
public class Active6yearPassTemplate {
	private Integer id;
	private Integer stage;
	private Integer pass_level;
	private String reward_free;
	private String reward_trump;
}
