package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("active_gift_day")
public class ActiveGiftDayTemplate {
	private Integer id;
	private int gift_type;//GroupGiftType
	private Integer player_lv_down;
	private Integer player_lv_up;
	private Integer type;
	private Integer recharge_gift_id;
	private String reward;
	private Integer buy_time;
}
