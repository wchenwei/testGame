package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.GuildFactoryPaperTemplate;
import com.hm.model.weight.RandomRatio;

@FileConfig("guild_factory_paper")
public class GuildFactoryPaperExtraTemplate extends GuildFactoryPaperTemplate{
	private RandomRatio paperRatio;
	

	public void init(){
		this.paperRatio = new RandomRatio(this.getLibrary());
	}
	
	public int randomArmsId(){
		return paperRatio.randomRatio();
	}
}
