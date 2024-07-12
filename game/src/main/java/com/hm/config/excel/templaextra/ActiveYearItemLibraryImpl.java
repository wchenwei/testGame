package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveYearItemLibraryTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;
import java.util.stream.Collectors;

@FileConfig("active_year_item_library")
public class ActiveYearItemLibraryImpl extends ActiveYearItemLibraryTemplate{
	List<Items> listItems = Lists.newArrayList();
	
	public void init() {
		listItems = ItemUtils.str2ItemList(this.getItem_unlock(), ",", ":");
	}
	public List<Items> getListItems() {
		return listItems.stream().map(e -> e.clone()).collect(Collectors.toList());
	}
}
