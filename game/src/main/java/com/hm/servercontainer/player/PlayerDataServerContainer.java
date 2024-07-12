package com.hm.servercontainer.player;

import com.hm.libcore.db.mongo.DBEntity;
import com.hm.servercontainer.ContainerMap;
import lombok.Getter;

/**
 * 玩家数据服务器容器
 * @Author xjt
 * @Date 2022-05-31
 **/
public class PlayerDataServerContainer {
    @Getter
    private static ContainerMap<PlayerDataItemContainer> serverMap =
            new ContainerMap<>(serverId -> new PlayerDataItemContainer(serverId));

    public static PlayerDataItemContainer of(int serverId) {
        return serverMap.getItemContainer(serverId);
    }

    public static PlayerDataItemContainer of(DBEntity entity) {
        return serverMap.getItemContainer(entity);
    }

}
