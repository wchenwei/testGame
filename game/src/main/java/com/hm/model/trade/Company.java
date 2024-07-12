package com.hm.model.trade;

import lombok.Data;

/**
 * Description:
 * User: yang xb
 * Date: 2019-01-08
 */
@Data
public class Company {
    private int lv = 1;
    // 升级结束时间,如果有的话
    private long lvUpEndTime;
    // 上次航运币产出结算时间
    private long lastCalcTime;
    // 最近一轮订单结束时间
    // private long lastOrderTime;

    public void incLv() {
        lv++;
    }
}
