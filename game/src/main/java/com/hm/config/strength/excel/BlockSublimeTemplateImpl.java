package com.hm.config.strength.excel;

import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.enums.StrengthGridType;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import lombok.Data;

import java.util.List;
import java.util.Map;

@FileConfig("block_sublime")
@Data
public class BlockSublimeTemplateImpl extends BlockSublimeTemplate{
    private Map<Integer,List<Items>> map = Maps.newConcurrentMap();

    public void init() {
        map.put(StrengthGridType.two.getType(), ItemUtils.str2DefaultItemImmutableList(this.getCost_grid2()));
        map.put(StrengthGridType.three.getType(), ItemUtils.str2DefaultItemImmutableList(this.getCost_grid3()));
        map.put(StrengthGridType.four.getType(), ItemUtils.str2DefaultItemImmutableList(this.getCost_grid4()));
        map.put(StrengthGridType.five.getType(), ItemUtils.str2DefaultItemImmutableList(this.getCost_grid5()));
    }

    /**
     * @param num 格子数量
     * @return
     */
    public List<Items> getCost(int num){
        return map.get(num);
    }
}
