package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("gift_level")
public class GiftLevelTemplate {
	private Integer id;
	private Integer recharge_id;
	private int type;
	private int val;
	private int day;
}
