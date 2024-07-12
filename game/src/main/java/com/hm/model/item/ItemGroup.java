package com.hm.model.item;

import com.google.common.collect.Lists;
import com.hm.util.ItemUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 奖励组
 * @author siyunlong  
 * @date 2020年12月14日 下午4:52:35 
 * @version V1.0
 */
@Data
@NoArgsConstructor
public class ItemGroup {
	private List<Items> itemList = Lists.newArrayList();

	public ItemGroup(String itemStr) {
		this.itemList = ItemUtils.str2DefaultItemList(itemStr);
	}
	
	public static List<ItemGroup> buildItemGroupList(String str) {
		return Arrays.stream(str.split(";")).map(e -> new ItemGroup(e)).collect(Collectors.toList());
	}
}
