package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.util.lv.ILvValue;
import com.hm.config.excel.temlate.WarGodTemplate;

@FileConfig("war_god")
public class WarGodTemplateImpl extends WarGodTemplate implements ILvValue{

	@Override
	public int getLv() {
		return getLevel();
	}

	@Override
	public long getExp() {
		return getPower_total();
	}
}
