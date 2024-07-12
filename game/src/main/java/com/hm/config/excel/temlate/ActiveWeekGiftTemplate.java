package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@FileConfig("active_week_gift")
@Data
public class ActiveWeekGiftTemplate {
	private Integer id;
	private Integer stage;
	private Integer quality;
	private Integer active_level;
	private String name;
	private Integer price_base;
	private Float discount;
	private Integer buy_times;
	private Integer player_lv_down;
	private Integer player_lv_up;
	private Integer recharge_gift_id;
	private Integer daily_reset;
	private Integer gift_free;
}
