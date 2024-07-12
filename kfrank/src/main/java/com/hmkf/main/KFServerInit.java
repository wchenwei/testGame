package com.hmkf.main;

import com.hm.actor.ActorDispatcherType;
import com.hm.config.ConfigLoad;
import com.hm.config.GameConfig;
import com.hm.leaderboards.HdLeaderboardsService;
import com.hm.libcore.handler.ServerStateCache;
import com.hm.libcore.handler.ServerStateCache.ServerState;
import com.hm.libcore.inner.InnerAction;
import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.springredis.factory.MapperFactory;
import com.hm.log.ali.AliLogProducerUtil;
import com.hm.server.GameHttpServer;
import com.hm.war.sg.SettingManager;
import com.hmkf.config.KFLevelConstants;
import com.hmkf.container.KFNpcContainer;
import com.hmkf.db.GameDBUtils;
import com.hmkf.gametype.KfGroupContainer;
import com.hmkf.kfcenter.CenterBiz;
import com.hmkf.kfcenter.KfCenterContainer;
import com.hmkf.rpc.KFRpc;
import com.hmkf.server.ServerGroupManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KFServerInit {

    public static void initServer() {
        //====================注册消息号 ========================================
        SpringUtil.getBeanMap(InnerAction.class).values().forEach(e -> e.registerMsg());
        //=====================加载世界配置文件=========================================
        ConfigLoad.loadAllConfig();
        AliLogProducerUtil.init();

        //注册redisDB
        MapperFactory.getFactory().registerDB();
        //=========================加载排行榜信息============================
        HdLeaderboardsService.getInstance().init();
        ActorDispatcherType.startAll();

        //按照顺序加载Container
        SettingManager.getInstance().initServer();
        ServerGroupManager.getIntance().init();

        KFNpcContainer.init();//npc加载
        SpringUtil.getBean(KfCenterContainer.class).init();

        SpringUtil.getBean(KfGroupContainer.class).init();
        SpringUtil.getBean(CenterBiz.class).checkLoadOrNewRank();

        ActorDispatcherType.startAll();

        GameDBUtils.initGameDB();
        KFLevelConstants.init();
        //检查军团成员列表
        ServerStateCache.changeServerState(ServerState.Running);
//		Test.testLoad();
        log.info("H5跨服段位战启动:1.0.2");
        System.err.println("=================游戏服版本:" + GameConfig.getGameVersion() + "===============");
    }


    public static void startGame() {
        KFRpc.startRpc();

        new Thread() {
            @Override
            public void run() {
                try {
                    GameHttpServer httpServer = new GameHttpServer();
                    httpServer.start();
                } catch (Exception e) {
                    log.error("htpp游戏服务器启动：", e);
                }
            }
        }.start();
        log.info("游戏服务器启动成功");
    }


}









