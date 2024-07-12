package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@FileConfig("active_2063_integralbox")
@Data
public class Active2063IntegralboxTemplate {
	private Integer id;
	private Integer stage;
	private Integer lv_down;
	private Integer lv_up;
	private Integer day;
	private String reward_integral;
	private Integer integral;
}
