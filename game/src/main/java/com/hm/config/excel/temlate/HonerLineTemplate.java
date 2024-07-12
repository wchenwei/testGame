package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("honer_line")
public class HonerLineTemplate {
	private Integer id;
	private int type;
	private Integer index;
	private Integer honer;
	private String reward;
	private int mission_id_min;
	private int mission_id_max;
}
