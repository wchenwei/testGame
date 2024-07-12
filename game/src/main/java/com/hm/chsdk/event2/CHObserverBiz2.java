package com.hm.chsdk.event2;

import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.model.player.Player;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.util.AnnotationUtil;

import java.util.List;
import java.util.Map;


@Biz
public class CHObserverBiz2 implements IObserver {
    public static Map<ObservableEnum, List<Class>> eventMap = Maps.newHashMap();
    public static boolean isOpen = false;
    public static boolean isZip = true;
    public static int PackMaxNum = 30;

    @Override
    public void registObserverEnum() {
        if (!ServerConfig.getInstance().isTestPay()
                || ServerConfig.getInstance().isSendChEvent()) {
            checkEventMap();
        }
    }

    public void checkEventMap() {
        if (!this.eventMap.isEmpty()) {
            return;
        }
        eventMap = AnnotationUtil.getAnnoTypeAll("com.hm.chsdk.event2", "com.hm.chsdk.annotation.EventMsg");
        eventMap.keySet().forEach(e -> {
            ObserverRouter.getInstance().registObserver(e, this);
        });
    }

    @Override
    public void invoke(ObservableEnum observableEnum, Player player,
                       Object... argv) {
        if (!isOpenCH()) {
            return;
        }
        if (player != null && !isSendCH(player)) {
            return;
        }
        List<Class> eventData = eventMap.get(observableEnum);
        for (Class eventDatum : eventData) {
            try {
                //构建事件
                CommonParamEvent tempEvent = (CommonParamEvent) eventDatum.newInstance();
                tempEvent.init(player, argv);
                if (tempEvent.getEvent_type_id() == null) {
                    continue;
                }
                tempEvent.initInfo(player, argv);
                AUploadEvent event = new AUploadEvent(player, tempEvent);
                event.sendSDK();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static boolean isOpenCH() {
        return isOpen;
    }

    private boolean isSendCH(Player player) {
        return player.playerFix().isSendCHSDK();
    }
}
