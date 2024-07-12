package com.hm.config.strength.excel;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@FileConfig("block_attr")
@Data
public class BlockAttrTemplate {
	private Integer index;
	private Integer star;
	private Integer attr_id;
	private Integer attr_type;
	private Float start_value;
	private Float levelup_value;
	private Float sublimation_value;
}
