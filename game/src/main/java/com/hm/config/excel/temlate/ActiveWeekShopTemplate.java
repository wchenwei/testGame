package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@FileConfig("active_week_shop")
@Data
public class ActiveWeekShopTemplate {
	private Integer id;
	private Integer limit_num;
	private Integer active_level;
	private String goods;
	private Integer price_old;
	private Integer price_now;
}
