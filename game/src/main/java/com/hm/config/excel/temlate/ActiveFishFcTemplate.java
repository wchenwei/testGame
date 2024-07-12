package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@FileConfig("active_fish_fc")
@Data
public class ActiveFishFcTemplate {
	private Integer id;
	private Integer pic_id;
	private String name;
	private String desc;
	private Integer type;
	private Integer rare;
	private String integral;
	private Integer minsize;
	private Integer maxsize;
	private String gold;
	private Integer show;
	private Integer isfish;
	private String time;
	private String item;
}
