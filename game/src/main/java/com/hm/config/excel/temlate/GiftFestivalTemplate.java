package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("gift_festival")
public class GiftFestivalTemplate {
	private Integer id;
	private String gift_name;
	private Integer price_base;
	private Integer buy_times;
	private Integer level_lower;
	private Integer level_upper;
	private Integer recharge_id;
}
