package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.GuildFlagTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

@FileConfig("guild_flag")
public class GuildFlagTemplateImpl extends GuildFlagTemplate{
	private Items costItems;
	
	public void init() {
    	costItems = ItemUtils.str2Item(this.getCost(), ":");
    }
    public Items getCostItems() {
		return costItems;
	}
}
