package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("player_arm")
public class PlayerArmTemplate {
	private Integer id;
	private String name;
	private String icon;
	private String desc;
	private Integer arm_pos;
	private Integer quality;
	private String attr;
	private Integer piece_id;
	private int piece_count;
	private Integer decompose_drop;
	private Integer next_arm;
	private String upgrade_cost;
}
