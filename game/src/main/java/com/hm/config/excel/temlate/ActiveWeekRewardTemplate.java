package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@FileConfig("active_week_reward")
@Data
public class ActiveWeekRewardTemplate {
	private Integer id;
	private Integer stage;
	private Integer need_level;
	private Integer need_vip;
	private String reward_back;
}
