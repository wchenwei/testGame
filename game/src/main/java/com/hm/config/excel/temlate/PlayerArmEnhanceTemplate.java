package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("player_arm_enhance")
public class PlayerArmEnhanceTemplate {
	private Integer level;
	private String arm_attr;
	private String cost_item;
	private int quality;//所需品质
}
