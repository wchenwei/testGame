package com.hm.model.trade;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Ship {
    private int id;
    private int lv;
    /**
     * 是否待交货
     */
    private boolean full;
    /**
     * 升级状态
     */
    private boolean onReform;
    /**
     * 升级结束时间
     */
    private long reformEndTime;
    /**
     * 最后一次结算后开始时间
     */
    private long beginTime;
    /**
     * 最后一次结算后开始的路线结束时间
     */
    private long endTime;
    /**
     * 城市id
     */
    private int targetId;
    /**
     * 运送物品id
     */
    private int itemId;
    /**
     * 是否是vip特权船只
     */
    private boolean vip;

    public void lvUp() {
        lv++;
    }

    /**
     * 目前航线是否超时结束
     *
     * @return
     */
    public boolean isOver() {
        return endTime > 0 && endTime < System.currentTimeMillis();
    }
}
