package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import lombok.Data;

import java.util.List;

@Data
public class MissionWaveDrop {
    private int totalNum;
    private List<Integer> posList;
    //每个坦克的掉落
    private List<Items>[] baseOneItemArray;

    private List<Items> elseList;

    public MissionWaveDrop(List<Integer> posList,String item1, String item2) {
        this.totalNum = posList.size();
        this.posList = posList;
        this.baseOneItemArray = new List[totalNum];
        for (int i = 0; i < this.baseOneItemArray.length; i++) {
            this.baseOneItemArray[i] = Lists.newArrayList();
        }
        
        for (Items items :  ItemUtils.str2DefaultItemList(item1)) {
            Items[] avgItems = avgItem(items,totalNum);//每个平均
            for (int i = 0; i < totalNum; i++) {
                this.baseOneItemArray[i].add(avgItems[i]);
            }
        }
        this.elseList = ItemUtils.str2DefaultItemList(item2);
    }

    public static Items[] avgItem(Items items,int count) {
        long v = items.getCount()/count;
        long lastCount = items.getCount();
        Items[] itemArray = new Items[count];
        for (int i = 0; i < count; i++) {
            itemArray[i] = items.clone();
            if(i == count - 1) {
                itemArray[i].setCount(lastCount);
            }else{
                itemArray[i].setCount(v);
            }
            lastCount -= itemArray[i].getCount();
        }
        return itemArray;
    }

    public List<Items>[] cloneOneBaseItem() {
        List<Items>[] clone = new List[this.baseOneItemArray.length];
        for (int i = 0; i < clone.length; i++) {
            clone[i] = ItemUtils.createCloneItems(this.baseOneItemArray[i]);
        }
        return clone;
    }

}
