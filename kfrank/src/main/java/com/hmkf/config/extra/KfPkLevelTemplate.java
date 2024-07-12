package com.hmkf.config.extra;

import java.util.List;

import com.google.common.collect.Lists;
import com.hm.enums.KfLevelType;
import com.hm.libcore.annotation.FileConfig;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import com.hmkf.config.template.KfPkSingleTemplate;

@FileConfig("kf_pk_single")
public class KfPkLevelTemplate extends KfPkSingleTemplate {
    private List<Items> winItemList = Lists.newArrayList();
    private List<Items> failItemList = Lists.newArrayList();

    private KfLevelType upType;
    private KfLevelType downType;


    public void init() {
        this.winItemList = ItemUtils.str2DefaultItemList(getReward_win());
        this.failItemList = ItemUtils.str2DefaultItemList(getReward_fail());

        this.upType = KfLevelType.getKfLevelType(getId()).getUpType();
        this.downType = KfLevelType.getKfLevelType(getId()).getDownType();
    }

    public List<Items> getItemList(boolean isWin) {
        return isWin ? winItemList : failItemList;
    }

    public String getItemListStr(boolean isWin) {
        return isWin ? getReward_win() : getReward_fail();
    }

    public int getNewLvType(int rank) {
        if(rank <= getLevel_up() && this.upType != null) {
            return this.upType.getType();
        }
        if(rank >= getLevel_down() && this.downType != null) {
            return this.downType.getType();
        }
        return getId();
    }

}
