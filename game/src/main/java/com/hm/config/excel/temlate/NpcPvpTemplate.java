package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("npc_pvp")
public class NpcPvpTemplate {
	private Integer id;
	private String name;
	private Integer model;
	private Integer type;
	private Integer level;
	private Integer reform;
	private Integer star;
	private Integer driver_lv;
	private String skill_level;
	private Integer atk;
	private Integer def;
	private Integer hp;
	private Integer hit;
	private Integer dodge;
	private Integer crit;
	private Integer crit_def;
	private Float crit_dam;
	private Float crit_res;
	private Float damage_increase;
	private Float damage_reduce;
	private String reward;
	private String skill_id;


}
