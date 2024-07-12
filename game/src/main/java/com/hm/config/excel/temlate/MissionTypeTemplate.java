package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("mission_type")
public class MissionTypeTemplate {
	private Integer id;
	private Integer type;
	private String open_day;
	private Integer free_challenge;
	private Integer unlock_level;
	private String country_limit;
	private Integer cost_oil;
	private Integer day_reward;
}
