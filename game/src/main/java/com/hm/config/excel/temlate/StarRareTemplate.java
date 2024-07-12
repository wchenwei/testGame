package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("star_rare")
public class StarRareTemplate {
	private Integer id;
	private Integer star;
	private Integer rare;
	private Float value;
	private Integer tank_master_scores;
}
