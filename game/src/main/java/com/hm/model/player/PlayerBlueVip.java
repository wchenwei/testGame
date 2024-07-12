package com.hm.model.player;

import com.google.common.collect.Lists;
import com.hm.libcore.msg.JsonMsg;
import com.hm.util.bluevip.QQBlueVip;
import lombok.Data;

import java.util.List;

/**
 * @author wyp
 * @description 蓝钻奖励信息
 * @date 2021/8/4 9:49
 */
@Data
public class PlayerBlueVip extends PlayerDataContext {
    private QQBlueVip blueVip;

    private List<Integer> gift = Lists.newArrayList();

    public boolean checkCanRec(int id) {
        return !gift.contains(id);
    }

    public void addGift(int id) {
        this.gift.add(id);
        SetChanged();
    }

    public void resetBlueVip(QQBlueVip blueVip) {
        this.blueVip = blueVip;
        SetChanged();
    }

    public boolean checkBlueVip() {
        return this.blueVip != null && blueVip.checkBlueVip();
    }

    public QQBlueVip getBlueVip() {
        return blueVip;
    }

    public int getBlueVipLv() {
        return this.blueVip != null ? this.blueVip.getBlueVipLv() : 0;
    }

    @Override
    public void fillMsg(JsonMsg msg) {
        msg.addProperty("PlayerBlueVip", this);
    }
}
