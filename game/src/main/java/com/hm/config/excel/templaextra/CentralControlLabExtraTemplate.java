package com.hm.config.excel.templaextra;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.CentralControlLabTemplate;
import com.hm.model.item.Items;
import com.hm.model.weight.Drop;
import com.hm.util.StringUtil;

import java.util.List;
import java.util.stream.Collectors;
@FileConfig("central_control_lab")
public class CentralControlLabExtraTemplate extends CentralControlLabTemplate {
    private List<Drop> drops = Lists.newArrayList();
    private List<Integer> nextIds = Lists.newArrayList();
    public void init(){
        List<String> dropStr = Lists.newArrayList(this.getLibrary().split(";"));
        this.drops = ImmutableList.copyOf(dropStr.stream().map(t -> new Drop(t)).collect(Collectors.toList()));
        this.nextIds = ImmutableList.copyOf(StringUtil.splitStr2IntegerList(this.getNext_lab(),","));
    }

    public List<Drop> getDrops(){
        return drops;
    }

    public List<Integer> getNextIds(){
        return nextIds;
    }

    public List<Items> getRandomRewards(){
        List<Items> dropItems = Lists.newArrayList();
        for(Drop drop:drops){
            Items item = drop.randomItem();
            if(item != null&&item.getType()!=0&&item.getCount()!=0) {
                dropItems.add(item);
            }
        }
        return dropItems;
    }

    public int randomNextId(){
        if(CollUtil.isEmpty(nextIds)){
            return -1;
        }
        return RandomUtil.randomEle(nextIds);
    }
}
