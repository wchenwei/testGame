package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("active_recharge_turningend")
public class ActiveRechargeTurningendTemplate {
	private Integer id;
	private Integer lv_down;
	private Integer lv_up;
	private Integer index;
	private Integer charge_gold;
	private String reward;
}
