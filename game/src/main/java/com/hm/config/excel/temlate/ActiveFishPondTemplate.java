package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@FileConfig("active_fish_pond")
@Data
public class ActiveFishPondTemplate {
	private Integer id;
	private String name;
	private String type;
	private String bait;
}
