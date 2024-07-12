package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@FileConfig("active_login_100gacha")
@Data
public class ActiveLogin100gachaTemplate {
	private Integer id;
	private String reward;
	private Integer order;
}
