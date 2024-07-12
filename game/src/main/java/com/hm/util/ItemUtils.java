package com.hm.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.hm.enums.ItemType;
import com.hm.enums.PlayerAssetEnum;
import com.hm.model.item.Items;
import com.hm.model.item.WeightItem;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ItemUtils {

	public static List<Items> str2DefaultItemImmutableList(String source) {
		return str2ItemImmutableList(source, ",", ":");
	}

	public static List<Items> str2ItemImmutableList(String source, String sp1, String sp2) {
		return ImmutableList.copyOf(str2ItemList(source, sp1, sp2));
	}

	public static List<Items> str2DefaultItemList(String source) {
		return str2ItemList(source, ",", ":");
	}
	
	public static List<Items> str2ItemList(String source,String sp1,String sp2) {
		List<Items> items = Lists.newArrayList();
		try {
			for (String str : source.split(sp1)) {
				int type = Integer.parseInt(str.split(sp2)[0]);
				int id = Integer.parseInt(str.split(sp2)[1]);
				long count = Long.parseLong(str.split(sp2)[2]);
				items.add(new Items(id,count,type));
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return items;
	}

	public static List<Items> str2HTItemList(String source,String sp1,String sp2) {
		List<Items> items = Lists.newArrayList();
		try {
			for (String str : source.split(sp1)) {
				int type = Integer.parseInt(str.split(sp2)[0]);
				int id = Integer.parseInt(str.split(sp2)[1]);
				long count = Long.parseLong(str.split(sp2)[3]);
				items.add(new Items(id,count,type));
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return items;
	}

    /**
     * 解析 2:4:1:490, type:id:count:weight
     *
     * @param source
     * @param sp1
     * @return
     */
    public static WeightItem str2WeightItem(String source, String sp1) {
        if (StringUtils.isBlank(source) || source.split(sp1).length != 4) {
            return null;
        }
        int type = Integer.parseInt(source.split(sp1)[0]);
        int id = Integer.parseInt(source.split(sp1)[1]);
        int count = Integer.parseInt(source.split(sp1)[2]);
        int weight = Integer.parseInt(source.split(sp1)[3]);

        Items items = new Items(id, count, type);
        WeightItem weightItem = new WeightItem();
        weightItem.setItems(items);
        weightItem.setWeight(weight);
        return weightItem;
    }

	public static Items str2Item(String source,String sp1) {
		if(StringUtils.isBlank(source) || source.split(sp1).length!=3){
			return null;
		}
		int type = Integer.parseInt(source.split(sp1)[0]);
		int id = Integer.parseInt(source.split(sp1)[1]);
		long count = Long.parseLong(source.split(sp1)[2]);
		return new Items(id,count,type);
	}
	
	/**
	 * 根据矿的资源转换成道具列表，用于展示
	 * @param robRes
	 * @return
	 */
	public static List<Items> createItems(Map<Integer,Long> robRes) {
		List<Items> itemList = Lists.newArrayList();
		for (Map.Entry<Integer,Long> entry : robRes.entrySet()) {
			itemList.add(new Items(entry.getKey(),entry.getValue().intValue(),ItemType.CURRENCY.getType()));
		}
		return itemList;
	}
	
	public static List<Items> createCloneItems(List<Items> itemList) {
		List<Items> cloneList = Lists.newArrayList();
		for (Items items : itemList) {
			cloneList.add(items.clone());
		}
		return cloneList;
	}
	
	public static List<Items> jsonToItems(JSONArray jsonArray) {
		List<Items> rewards = Lists.newArrayList();
		if(jsonArray!=null){
			for(int i=0;i<jsonArray.size();i++) {
				JSONObject obj = jsonArray.getJSONObject(i);
	    		if(obj!=null){
	    			int id = obj.getInt("id");
	    			int type = obj.getInt("itemType");
	    			int count = obj.getInt("count");
	    			Items item = new Items(id, count, type);
	    			rewards.add(item);
	    		}
			}
		}
		return rewards;
	}
	/**
	 * 
	 * calItemExtraAdd:(计算道具额外产出). <br/>  
	 * @author zxj  
	 * @param itemList
	 * @param itemType
	 * @param assetEnum
	 * @param extraAdd
	 * @return  使用说明
	 */
	public static List<Items> calItemExtraAdd(List<Items> itemList,ItemType itemType,PlayerAssetEnum assetEnum, double extraAdd) {
		if(extraAdd <= 0) {
			return itemList;
		}
		List<Items> newList = Lists.newArrayList();
		for (Items item : itemList) {
			Items newItem = item.clone();
			if(item.getEnumItemType() == itemType && item.getId()==assetEnum.getTypeId()) {
				newItem.setCount((long)(newItem.getCount()*(1+extraAdd)));
				newList.add(newItem);
			}else{
				newList.add(item);
			}
		}
		return newList;
	} 
	
	public static List<Items> calItemExtraAdd(List<Items> itemList,ItemType itemType,double extraAdd) {
		if(extraAdd <= 0) {
			return itemList;
		}
		List<Items> newList = Lists.newArrayList();
		for (Items item : itemList) {
			if(item.getEnumItemType() == itemType) {
				Items newItem = item.clone();
				newItem.setCount((long)(newItem.getCount()*(1+extraAdd)));
				newList.add(newItem);
			}else{
				newList.add(item);
			}
		}
		return newList;
	} 
	
	public static List<Items> calItemExtraAdd(List<Items> itemList,double extraAdd) {
		if(extraAdd <= 0) {
			return itemList;
		}
		List<Items> newList = Lists.newArrayList();
		for (Items item : itemList) {
			Items newItem = item.clone();
			if(!ItemType.isRealGift(newItem.getType())){
				newItem.addCountRate(extraAdd);
			}
			newList.add(newItem);
		}
		return newList;
	} 
	
	public static String itemListToString(List<Items> itemList) {
		String str = "";
		if(CollectionUtil.isEmpty(itemList)) {
			return str;
		}
		for(Items items:itemList){
			str +=items.getItemType()+":"+items.getId()+":"+items.getCount()+",";
		}
		str = StringUtils.stripEnd(str, ",");
		return str;
	}
	public static String itemToString(Items items) {
		return items.getItemType()+":"+items.getId()+":"+items.getCount();
	}
	
	
	//是否是经验
	public static boolean isExp(Items item) {
		return item.getEnumItemType()==ItemType.CURRENCY&&item.getId()==PlayerAssetEnum.EXP.getTypeId();
	}
	//list是否包含needItem
	public static boolean isContains(List<Items> items,Items needItem) {
		/*for (Items item : items) {
			if(item.getItemType()==needItem.getItemType()&&item.getId()==needItem.getId()){
				return true;
			}
		}*/
		items.stream().anyMatch(item -> {
	        return item.getItemType()==needItem.getItemType()&&item.getId()==needItem.getId();
	    });
		return false;
	}
	
	
	 /**
	    * map转为items
	    *
	    * @author yanpeng 
	    * @param map
	    * @return  
	    *
	    */
	   public static List<Items> parseMapToItems(Table<Integer, Integer,Integer> table){
		   List<Items> itemList = Lists.newArrayList(); 
		   for(Table.Cell<Integer, Integer, Integer> entry : table.cellSet()){
				Items items = new Items(entry.getRowKey()); 
				items.setCount(entry.getValue());
				items.setItemType(entry.getColumnKey());
				itemList.add(items);
			}
		   return itemList; 
	   }
	   
	 //检查合并itemList
	public static List<Items> mergeItemList(List<Items> itemList) {
		if(CollUtil.isEmpty(itemList)) {
			return itemList;
		}
		Table<Integer, Integer, Items> tables = HashBasedTable.create();
		for (Items items : itemList) {
			if(items.getItemType()!=ItemType.OTHER.getType()){
				Items temp = tables.get(items.getItemType(), items.getId());
				if(temp == null) {
					if(items.getCount()>0){
						temp = items.clone();
						tables.put(items.getItemType(), items.getId(), temp);
					}
				}else{
					temp.addCount(items.getCount());
				}
			}
		}
		return Lists.newArrayList(tables.values());
	}
	
	/**
	 * 获取奖励的比例奖励
	 * @param itemList
	 * @param rate
	 * @return
	 */
	public static List<Items> calItemRateReward(List<Items> itemList,double rate) {
		List<Items> tempList = Lists.newArrayList();
		for (Items items : itemList) {
			Items clone = items.clone();
			clone.setCount((long)(clone.getCount()*rate));
			if(clone.getCount() > 0) {
				tempList.add(clone);
			}
		}
		return tempList;
	}
	
	/**
	 * 获取奖励的比例奖励
	 * @param itemList
	 * @param rate
	 * @return
	 */
	public static Items calItemRateReward(Items item,double rate) {
		Items clone = item.clone();
		clone.setCount((long)(clone.getCount()*rate));
		return clone;
	}
	/**
	 * 在itemList中去除对应itemType,id的item
	 * @param itemList
	 * @param itemType
	 * @param itemId
	 * @return
	 */
	public static List<Items> filterItemList(List<Items> itemList,ItemType itemType,int itemId){
		return itemList.stream().filter(t -> !(t.getItemType()==itemType.getType()&&t.getId()==itemId)).collect(Collectors.toList());
	}
	
	/**
	 * @Title: getGoldNum 
	 * @Description: 获取items中的金砖数量综合
	 * @param listItems
	 * @return
	 * @return long    返回类型 
	 * @throws
	 */
    public static long getGoldNum(List<Items> listItems) {
    	if(CollectionUtil.isEmpty(listItems)) {
    		return 0;
    	}
    	return listItems.stream().filter(e->{
    		return e.getItemType()==ItemType.CURRENCY.getType() && PlayerAssetEnum.isGold(e.getId());
    	}).mapToLong(Items::getCount).sum();
    }
    
    /**
	 * @Title: getItemNum 
	 * @Description: 获取items中的某个道具的数量
	 * @param listItems
	 * @return
	 * @return long    返回类型 
	 * @throws
	 */
    public static long getItemNum(List<Items> listItems,int itemType,int itemId) {
    	if(CollectionUtil.isEmpty(listItems)) {
    		return 0;
    	}
    	return listItems.stream().filter(e->{
    		return e.getItemType()==itemType && e.getId()==itemId;
    	}).mapToLong(Items::getCount).sum();
    }
    //判断是否包含实物礼包
    public static boolean containRealGift(List<Items> listItems) {
    	for(Items tempItem: listItems) {
    		boolean isRealItemType = (ItemType.RealGift.getType() == tempItem.getItemType());
    		if(isRealItemType) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public static List<Items> checkItemList(List<Items> rewards){
    	return rewards.stream().filter(t ->t!=null).filter(t->t.getItemType()>0).collect(Collectors.toList());
    }

	public static boolean isGold(Items item) {
		return item.getItemType() == ItemType.CURRENCY.getType() && PlayerAssetEnum.isGold(item.getId());
	}

}
