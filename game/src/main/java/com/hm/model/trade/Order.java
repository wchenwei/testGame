package com.hm.model.trade;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * Description:贸易订单
 * User: yang xb
 * Date: 2019-01-11
 */
@Data
public class Order {
    private int id;
    private int fromCityId;
    private int toCityId;
    // 商品价格
    private int price;
    private int itemId;
    // 航线途径的贸易线
    private List<Integer> line = Lists.newArrayList();
    // 运载数量
    // private int amount;
    // 需要耗时
    // private int time;
    // 启航时间点
    private long beginTime;
    // 订单过期时间
    private long expireTime;
}
