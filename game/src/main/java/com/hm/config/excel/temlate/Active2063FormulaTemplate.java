package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@FileConfig("active_2063_formula")
@Data
public class Active2063FormulaTemplate {
	private Integer id;
	private Integer type;
	private Integer server_level_low;
	private Integer server_level_high;
	private String product;
	private String name;
	private String dec;
	private String icon;
	private String formula;
}
