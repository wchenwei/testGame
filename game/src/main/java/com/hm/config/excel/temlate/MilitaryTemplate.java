package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("military")
public class MilitaryTemplate {
	private Integer level;
	private String name;
	private String icon;
	private Integer classlevel;
	private Integer atk;
	private Integer def;
	private Integer hp;
	private Float hp_buff;
	private Integer atk_add;
	private Integer def_add;
	private Integer hp_add;
	private String cost;
	private Integer count;
	private Float up_rate;
	private Float rate_add;
	private Float rate_display;

	private int naskill_lv;
	private int naskill_num;
	private int naskill_cd;

	private int kaskill_lv;
	private int kaskill_num;
	private int kaskill_cd;

	private int spskill_lv;
	private int spskill_num;
	private int ghost_reward;

}
