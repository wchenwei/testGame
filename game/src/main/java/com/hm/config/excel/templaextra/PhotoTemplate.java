package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ItemPhotoTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("item_photo")
public class PhotoTemplate extends ItemPhotoTemplate{
	private List<Items> itemList = Lists.newArrayList();
	public void init() {
		this.itemList = ItemUtils.str2DefaultItemList(getRecycle());
	}
	public List<Items> getItemList() {
		return itemList;
	}
}
