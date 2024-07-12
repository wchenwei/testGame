package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("server_function_unlock")
public class ServerFunctionUnlockTemplate {
	private Integer id;
	private Integer level;
	private Integer city;
	private Integer guild_lv;
	private String name;
}
