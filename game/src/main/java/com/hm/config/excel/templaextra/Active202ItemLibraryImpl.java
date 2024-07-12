package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active202ItemLibraryTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;
import java.util.stream.Collectors;

@FileConfig("active_202_item_library")
public class Active202ItemLibraryImpl extends Active202ItemLibraryTemplate{
	List<Items> listItems = Lists.newArrayList();
	
	public void init() {
		listItems = ItemUtils.str2ItemList(this.getItem_unlock(), ",", ":");
	}
	public List<Items> getListItems() {
		return listItems.stream().map(e -> e.clone()).collect(Collectors.toList());
	}
}
