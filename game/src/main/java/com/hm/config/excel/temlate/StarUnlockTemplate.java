package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("star_unlock")
public class StarUnlockTemplate {
	private Integer star;
	private Integer need_paper;
	private Integer cost_cash;
	private Integer repeat_paper;
	private Integer skill_unlock;
	private Integer tame_num;
}
