package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("shop_type")
public class ShopTypeTemplate {
	private Integer shop_id;
	private String name;
	private Integer type;
	private String refresh_cost;
	private Integer buy_limit;
	private Integer unlock_level;
	private String reset_time;
	private Integer exist_time;
	private String icon;
	private String refresh_limit;
	private Integer refresh_num;

}
