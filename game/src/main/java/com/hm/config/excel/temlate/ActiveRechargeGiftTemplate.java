package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("active_recharge_gift")
public class ActiveRechargeGiftTemplate {
	private Integer id;
	private Integer days;
	private String level;
	private Integer need_gold;
	private String reward;
}
