package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("function_unlock")
public class FunctionUnlockTemplate {
	private Integer id;
	private Integer level;
	private Integer mission;
	private Integer task_Id;
	private Integer guide_step;
	private Integer icon_fly;
	private Integer display_level;
	private String icon;
	private String name;
	private String desc;
}
