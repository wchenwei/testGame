package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("guild_level")
public class GuildLevelTemplate {
	private Integer level;
	private Integer exp;
	private Integer exp_total;
	private Integer tec_point;
	private Integer guide_member;
	private Integer city_num;
	private Integer army_base;
	private Float employ_reward_buff;
}
