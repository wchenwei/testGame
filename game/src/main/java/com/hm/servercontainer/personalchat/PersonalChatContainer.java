package com.hm.servercontainer.personalchat;

import com.hm.libcore.db.mongo.DBEntity;
import com.hm.servercontainer.ContainerMap;
import lombok.Getter;

public class PersonalChatContainer {

    @Getter
    private static ContainerMap<PersonalChatItemContainer> serverMap =
            new ContainerMap<>(serverId -> new PersonalChatItemContainer(serverId));

    public static PersonalChatItemContainer of(int serverId) {
        return serverMap.getItemContainer(serverId);
    }

    public static PersonalChatItemContainer of(DBEntity entity) {
        return serverMap.getItemContainer(entity);
    }
}
