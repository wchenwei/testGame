package com.hm.config.strength.excel;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@FileConfig("block_model")
@Data
public class BlockModelTemplate {
	private Integer id;
	private String shape;
	private String shape90;
	private String shape180;
	private String shape270;
}
