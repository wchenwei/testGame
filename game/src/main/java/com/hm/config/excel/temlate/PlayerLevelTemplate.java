package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("player_level")
public class PlayerLevelTemplate {
	private Integer level;
	private Integer exp;
	private Integer exp_totle;
	private Integer levy_cash;
	private Integer levy_oil;
	private String level_reward;
	private String ten_reward;
	private Integer expedition_clean;
}
