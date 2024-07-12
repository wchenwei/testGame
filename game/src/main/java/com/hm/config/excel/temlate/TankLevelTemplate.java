package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("tank_level")
public class TankLevelTemplate {
	private Integer level;
	private Long exp;
	private Long exp_total;
	private Integer reform_level_unclock;
	private Integer tame_num;
	private Integer military_level;
}
