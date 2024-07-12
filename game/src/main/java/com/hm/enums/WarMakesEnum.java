package com.hm.enums;

import com.google.common.collect.Lists;
import com.hm.config.excel.templaextra.KfSeasonPassTemplateImpl;
import com.hm.model.item.Items;
import com.hm.model.player.PlayerWarMakes;

import java.util.List;

/**
 * @author wyp
 * @description
 *      战令类型
 * @date 2021/4/14 20:56
 */
public enum WarMakesEnum {

    free(0, "免费战令奖励"){
        @Override
        public List<Items> getRewardByType(KfSeasonPassTemplateImpl kfSeasonPassTemplate) {
            return kfSeasonPassTemplate.getRewardFreeList();
        }

        @Override
        public boolean typeIsCanReceive(PlayerWarMakes playerWarMakes) {
            return true;
        }
    },
    Trump(1, "王牌战令奖励"){
        @Override
        public List<Items> getRewardByType(KfSeasonPassTemplateImpl kfSeasonPassTemplate) {
            return kfSeasonPassTemplate.getRewardTrumpList();
        }
    },
    Legend(2, "传奇战令奖励"){
        @Override
        public List<Items> getRewardByType(KfSeasonPassTemplateImpl kfSeasonPassTemplate) {
            return kfSeasonPassTemplate.getRewardLegendList();
        }

        @Override
        public boolean typeIsCanReceive(PlayerWarMakes playerWarMakes) {
            // 必须也买过王牌战令才能领取
            return super.typeIsCanReceive(playerWarMakes) && playerWarMakes.getBuyTypeSet().size() >= 2;
        }
    },
    ;

    private WarMakesEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }
    private int type;

    private String desc;

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public List<Items> getRewardByType(KfSeasonPassTemplateImpl kfSeasonPassTemplate){
        return Lists.newArrayList();
    };

    public boolean typeIsCanReceive(PlayerWarMakes playerWarMakes){
        return playerWarMakes.getBuyTypeSet().contains(this.getType());
    }

    public static WarMakesEnum getByType(int type){
        for(WarMakesEnum warMakesEnum :WarMakesEnum.values()){
            if(warMakesEnum.getType() == type){
                return warMakesEnum;
            }
        }
        return null;
    }
}
