package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("driver_level")
public class DriverLevelTemplate {
	private Integer id;
	private Integer rare_id;
	private Integer level;
	private Integer cost_soul;
	private String cost_item;
	private Integer evolve_level;
}
