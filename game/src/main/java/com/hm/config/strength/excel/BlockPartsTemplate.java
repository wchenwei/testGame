package com.hm.config.strength.excel;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@FileConfig("block_parts")
@Data
public class BlockPartsTemplate {
	private Integer id;
	private Integer star;
	private Integer tank_type;
	private Integer model_id;
	private Integer gird;
	private Integer exp;
	private Integer gacha_rate;
	private Integer base_attr_num;
	private Integer percentage_attr_num;
	private Integer level_limit;
	private String name;
	private String icon;
}
