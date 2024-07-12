package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("gift_push")
public class GiftPushTemplate {
	private Integer id;
	private Integer type_id;
	private String Type_parameters;
	private Integer buy_times;
	private Integer last_time;
	private String recharge_id;
}
