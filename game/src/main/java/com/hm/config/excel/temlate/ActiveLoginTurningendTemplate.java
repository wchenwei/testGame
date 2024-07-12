package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("active_login_turningend")
public class ActiveLoginTurningendTemplate {
	private Integer id;
	private Integer days;
	private String reward;
}
