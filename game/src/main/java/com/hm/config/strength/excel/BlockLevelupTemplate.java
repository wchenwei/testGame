package com.hm.config.strength.excel;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@FileConfig("block_levelup")
@Data
public class BlockLevelupTemplate {
	private Integer level;
	private Integer exp_grid2;
	private Integer exp_grid3;
	private Integer exp_grid4;
	private Integer exp_grid5;
}
