package com.hm.config.strength.excel;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@FileConfig("block_sublime")
@Data
public class BlockSublimeTemplate {
	private Integer level;
	private String cost_grid2;
	private String cost_grid3;
	private String cost_grid4;
	private String cost_grid5;
}
