package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.util.lv.ILvValue;
import com.hm.config.excel.temlate.MuseumLevelTemplate;

@FileConfig("museum_level")
public class MemorialLvTemplate extends MuseumLevelTemplate implements ILvValue{
	
	@Override
	public int getLv() {
		return getId();
	}

	@Override
	public long getExp() {
		return getMark_value();
	}
	
}
