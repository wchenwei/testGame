package com.hm.redis;


import cn.hutool.core.date.DateUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 服务器redis数据
 * @date 2020年12月9日 上午10:47:26
 */
@NoArgsConstructor
@Data
public class ServerRedisData {
    private int id;//服务器id
    private long openTime;//开服时间
    private long lastStartTime;//上次启动时间

    public int getOpenDay() {
        return (int) DateUtil.betweenDay(new Date(openTime), new Date(), true) + 1;
    }
}
