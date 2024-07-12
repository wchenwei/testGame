package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@FileConfig("active_fish")
@Data
public class ActiveFishTemplate {
	private Integer id;
	private String name;
	private String desc;
	private Integer type;
	private Integer rare;
	private String integral;
	private Integer exp;
	private Integer minsize;
	private Integer maxsize;
	private String gold;
	private Integer show;
	private Integer isfish;

}
