package com.hm.model.trade;

import lombok.Data;

/**
 * Description:
 * User: yang xb
 * Date: 2019-01-19
 */
@Data
public class Earnings {
    private int boatId;
    private int times;
    private int amount;

    public void reset() {
        boatId = 0;
        times = 0;
        amount = 0;
    }

    public void inc(int i) {
        times++;
        amount += i;
    }
}
