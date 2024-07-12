package com.hm.model.trade;

import com.google.common.collect.Lists;
import com.hm.enums.BoatState;
import lombok.Data;

import java.util.List;

/**
 * Description:
 * User: yang xb
 * Date: 2019-01-08
 */
@Data
public class Boat {
    private int id;
    // 等级
    private int lv;
    // 状态
    private int state;
    // 航线途径的贸易线
    private List<Integer> line = Lists.newArrayList();
    // 进货价
    private int price;
    // 运输的数量
    private int amount;
    private long beginTime;
    private long endTime;
    // 运载的物品id
    private int itemId;
    private int orderId;

    public void lvUp() {
        lv++;
    }

    public void resetFree() {
        line.clear();
        price = 0;
        amount = 0;
        beginTime = 0;
        endTime = 0;
        itemId = 0;
        line.clear();
        orderId = 0;
        state = BoatState.Free.getType();
    }
}
