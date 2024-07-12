package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("active_three_day_point")
public class ActiveThreeDayPointTemplate {
	private Integer id;
	private Integer point;
	private String reward;
}
