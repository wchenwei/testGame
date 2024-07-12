package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.FriendshipTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@FileConfig("friendship")
public class FriendTemplate extends FriendshipTemplate {
	private List<Items> costList = Lists.newArrayList(); //消耗的材料
	private List<Integer> friendList = Lists.newArrayList(); 
	private List<Integer> attrList = Lists.newArrayList(); 
	public void init(){
		 this.costList = ItemUtils.str2ItemList(getCost(), ",", ":");
		 this.friendList = Arrays.asList(getFriend_id().split(",")).stream().map(Integer::parseInt).collect(Collectors.toList());
		 this.attrList = Arrays.asList(getAttr_value().split(",")).stream().map(Integer::parseInt).collect(Collectors.toList());
	}
	public Items getCostItems(int star) {
		return costList.get(star);
	}
	public List<Integer> getFriendList() {
		return friendList;
	}
	public int getAttr(int star) {
		return star >0 ?attrList.get(star-1):0;
	}
	public List<Items> getAllCostItems(int star) {
		List<Items> tempItems = Lists.newArrayList();
		for(int i=0; i<star; i++) {
			Items tempItem = costList.get(i);
			if(tempItem.getCount()>0) {
				tempItems.add(tempItem);
			}
		}
		return tempItems;
	}
	
}
