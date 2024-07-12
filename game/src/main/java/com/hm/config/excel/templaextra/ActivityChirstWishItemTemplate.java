package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveChirstWishItemTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

@FileConfig("active_chirst_wish_item")
public class ActivityChirstWishItemTemplate extends ActiveChirstWishItemTemplate{
	private Items wishs;
	public void init(){
		this.wishs = ItemUtils.str2Item(this.getWish(), ":");
	}
	public Items getWishs() {
		return wishs;
	}
	public boolean isFit(int type,int playerLv) {
		return type==this.getWish_id()&&playerLv>=this.getPlayer_lv_down()&&playerLv<=this.getPlayer_lv_up();
	}
	
}
