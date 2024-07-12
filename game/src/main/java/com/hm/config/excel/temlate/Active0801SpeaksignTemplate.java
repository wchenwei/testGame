package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@FileConfig("active_0801_speaksign")
@Data
public class Active0801SpeaksignTemplate {
	private Integer id;
	private Integer stage;
	private Integer lv_down;
	private Integer lv_up;
	private Integer day;
	private String speak_sign;
}
