package com.hm.model.fish;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.config.excel.templaextra.ActiveFishFcTemplateImpl;
import com.hm.config.excel.templaextra.ActiveFishTemplateImpl;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.util.ItemUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @introduce: 类注释
 * @author: wyp
 * @DATE: 2023/11/2
 **/
@NoArgsConstructor
@Data
@AllArgsConstructor
public class FishVO {
    private int id;
    private boolean isFish;
    private int size;
    private boolean firstGet;
    private List<Items> list = Lists.newArrayList();
    private boolean isDouble;
    private boolean updateSize;

    public FishVO(Player player, ActiveFishTemplateImpl fishTemplate){
        this.id =fishTemplate.getId();
        this.isDouble = player.playerFish().isDouble();
        if(isDouble){
            this.list = ItemUtils.calItemRateReward(fishTemplate.getIntegralList(), 2);
        }else {
            this.list = fishTemplate.getIntegralList();
        }
        if(fishTemplate.getIsfish() != 1){
            // 不是鱼
            return;
        }
        this.isFish = true;
        this.firstGet = player.playerFish().isFirstRecord(this.id);
        this.size = fishTemplate.getRandomFishSize();
    }

}
