package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("active_seven_login_reward")
public class ActiveSevenLoginRewardTemplate {
	private Integer id;
	private String reward;
	private int itemqty;
}
