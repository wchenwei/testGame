package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@FileConfig("active_2063_gift")
@Data
public class Active2063GiftTemplate {
	private Integer id;
	private Integer stage;
	private Integer quality;
	private Integer price_base;
	private Integer buy_times;
	private Integer player_lv_down;
	private Integer player_lv_up;
	private Integer recharge_gift_id;
}
