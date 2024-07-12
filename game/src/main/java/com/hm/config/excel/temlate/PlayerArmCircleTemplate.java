package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("player_arm_circle")
public class PlayerArmCircleTemplate {
	private Integer id;
	private Integer type;
	private Integer level;
	private Integer request_case;
	private String attr;
	private Float hp_recover;
	private String frame;
}
