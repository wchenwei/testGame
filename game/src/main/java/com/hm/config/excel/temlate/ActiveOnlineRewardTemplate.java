package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("active_online_reward")
public class ActiveOnlineRewardTemplate {
	private Integer id;
	private Integer time_require;
	private Integer ad_id;
	private String reward;
}
