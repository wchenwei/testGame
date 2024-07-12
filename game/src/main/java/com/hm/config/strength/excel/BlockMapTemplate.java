package com.hm.config.strength.excel;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@FileConfig("block_map")
@Data
public class BlockMapTemplate {
	private Integer id;
	private Integer tank_type;
	private String gride;
	private String ring_attri;
}
