package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.util.lv.ILvValue;
import com.hm.config.excel.temlate.OfficialLevelTemplate;

@FileConfig("official_level")
public class CampLevelTemplate extends OfficialLevelTemplate implements ILvValue{

	@Override
	public int getLv() {
		return getLevel();
	}

	@Override
	public long getExp() {
		return getAchievement_totle();
	}
}
