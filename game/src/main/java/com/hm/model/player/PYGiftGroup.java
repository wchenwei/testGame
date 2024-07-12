package com.hm.model.player;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.concurrent.ConcurrentHashMap;

@Getter
@NoArgsConstructor
public class PYGiftGroup {
    private int type;//GroupGiftType
    private int lv;//根据显示等级
    //每个礼包的购买或者领取次数
    private ConcurrentHashMap<Integer,Integer> giftMap = new ConcurrentHashMap<>();

    public PYGiftGroup(int type) {
        this.type = type;
    }

    public void addGift(int id) {
        this.giftMap.put(id,getGiftCount(id)+1);
    }
    public int getGiftCount(int id) {
        return this.giftMap.getOrDefault(id,0);
    }

    public void doReset(BasePlayer player) {
        this.giftMap.clear();
        this.lv = player.playerCommander().getMilitaryLv();
    }
}
