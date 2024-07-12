package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.GuildFactoryLevelTemplate;
import com.hm.model.weight.RandomRatio;

@FileConfig("guild_factory_level")
public class GuildFactoryLevelExtraTemplate extends GuildFactoryLevelTemplate{
	private RandomRatio paperRatio;
	
	public void init(){
		this.paperRatio = new RandomRatio(this.getProduct());
	}

	public RandomRatio getPaperRatio() {
		return paperRatio;
	}
	
	public int ranomPaper(){
		return paperRatio.randomRatio();
	}
	
	
}
