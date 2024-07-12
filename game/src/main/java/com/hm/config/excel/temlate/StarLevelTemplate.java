package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("star_level")
public class StarLevelTemplate {
	private Integer id;
	private Integer type;
	private Integer star;
	private Integer node;
	private Integer need_paper;
	private String node_attr;
	private Integer star_upgrade;

}
