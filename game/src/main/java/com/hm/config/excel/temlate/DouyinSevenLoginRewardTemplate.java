package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("douyin_seven_login_reward")
public class DouyinSevenLoginRewardTemplate {
	private Integer id;
	private String reward;
}
