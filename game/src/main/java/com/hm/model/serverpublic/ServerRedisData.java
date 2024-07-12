package com.hm.model.serverpublic;

import com.hm.libcore.util.date.DateUtil;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.redis.mode.AbstractRedisHashMode;
import com.hm.redis.type.RedisTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

import java.util.Date;
import java.util.List;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 服务器redis数据
 * @date 2020年12月9日 上午10:47:26
 */
@NoArgsConstructor
@Data
public class ServerRedisData{
    private int id;//服务器id
    private long openTime;//开服时间
    private long lastStartTime;//上次启动时间

    @Transient
    private transient String openDate;

    public ServerRedisData(int id) {
        this.id = id;
        this.openTime = System.currentTimeMillis();
    }

    public void load(ServerData serverData) {
        this.id = serverData.getServerId();
        this.openTime = serverData.getServerOpenData().getFirstOpenDate().getTime();
        this.lastStartTime = System.currentTimeMillis();
    }

    public int getOpenDay() {
        return (int) DateUtil.betweenDay(new Date(openTime), new Date(), true);
    }

    public String getOpenDate() {
        if(this.openDate == null) {
            this.openDate = DateUtil.formatDateTime(new Date(this.openTime));
        }
        return this.openDate;
    }

    public void saveDB() {
        RedisTypeEnum.ServerData.put(this.id+"", GSONUtils.ToJSONString(this));
    }

    public static ServerRedisData get(int serverId) {
        return GSONUtils.FromJSONString(RedisTypeEnum.ServerData.get(serverId+""),ServerRedisData.class);
    }
}
