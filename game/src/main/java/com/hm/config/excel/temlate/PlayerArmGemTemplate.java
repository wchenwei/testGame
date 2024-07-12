package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("player_arm_gem")
public class PlayerArmGemTemplate {
	private Integer id;
	private String name;
	private String icon;
	private String desc;
	private Integer level;
	private Integer arm_pos;
	private Integer quality;
	private String attr;
	private String cost;
	private String levelup_cost;
	private Integer next_id;
	private Integer before_id;
	private int next_need_count;//合成下一级需要多个
}
