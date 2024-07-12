package com.hmkf.config.template;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("npc_arena")
public class NpcArenaTemplate {
	private Integer id;
	private Integer pk_lv;
	private Integer day;
	private String enemy_config;
}
