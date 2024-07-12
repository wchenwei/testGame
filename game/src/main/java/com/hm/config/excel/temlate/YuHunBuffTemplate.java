package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("yuhun_buff")
public class YuHunBuffTemplate {
	private Integer id;
	private Integer type;
	private Integer skill_id;
	private Integer weight;
	private String name;
}
