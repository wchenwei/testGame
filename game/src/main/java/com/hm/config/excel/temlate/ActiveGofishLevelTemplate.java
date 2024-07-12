package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@FileConfig("active_gofish_level")
@Data
public class ActiveGofishLevelTemplate {
	private Integer level;
	private Integer exp;
	private Integer exp_total;
	private String fish_pond;
}
