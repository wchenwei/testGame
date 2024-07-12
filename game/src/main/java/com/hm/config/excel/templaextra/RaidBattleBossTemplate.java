package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.MissionSingleLevelTemplate;
import com.hm.model.weight.RandomRatio;

@FileConfig("mission_single_level")
public class RaidBattleBossTemplate extends MissionSingleLevelTemplate{
	private RandomRatio eventRatio;
	public void init(){
		this.eventRatio = new RandomRatio(this.getEvent_library());
	}
	public RandomRatio getEventRatio() {
		return eventRatio;
	}
	
}

