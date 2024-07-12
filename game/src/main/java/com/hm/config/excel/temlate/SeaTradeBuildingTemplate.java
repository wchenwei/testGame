package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("sea_trade_building")
public class SeaTradeBuildingTemplate {
	private Integer level;
	private Integer store;
	private Integer order_time;
	private Integer reform_time;
	private Integer reform_cost;
	private Integer hour_point;
	private Integer gold_time;
	private int max_stock;
}
