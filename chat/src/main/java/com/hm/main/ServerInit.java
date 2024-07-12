package com.hm.main;

import com.hm.libcore.spring.SpringUtil;
import com.hm.actor.ActorDispatcherType;
import com.hm.config.ConfigLoad;
import com.hm.container.ForbidWordManager;
import com.hm.db.GameServerManager;
import com.hm.libcore.inner.InnerAction;
import com.hm.room.RoomManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerInit {

    public static void initServer() {
        //加载世界配置文件
        ConfigLoad.loadAllConfig();
        //加载服务器数据
        GameServerManager.getInstance().init();
        RoomManager.getIntance().init();
        ForbidWordManager.initWordFromDB();
        for (InnerAction temp : SpringUtil.getBeanMap(InnerAction.class).values()) {
            temp.registerMsg();
        }
        ActorDispatcherType.startAll();
        //ChatMsgUtils.createIndex();
        log.error("聊天服版本:1.0.0");
    }

}
