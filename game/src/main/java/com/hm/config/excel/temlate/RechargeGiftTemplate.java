package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("recharge_gift")
public class RechargeGiftTemplate {
	private Integer id;
	private Integer type;
	private Integer diamonds;
	private Integer else_diamonds;
	private Integer days;
	private Integer month_gift;
	private String name;
	private String icon;
	private Integer recharge_id;
	private Integer specail_gift;
	private Integer index;
	private Integer first_double;
	private Integer limit_buy;

	private String ext_info;
}
