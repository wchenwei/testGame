package com.hm.chsdk;

import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Biz;
import com.hm.chsdk.event2.CommonParamInter;
import com.hm.chsdk.event2.PlayerInfoUploadEvent;
import com.hm.model.player.Player;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.util.AnnotationUtil;

import java.util.List;
import java.util.Map;


@Biz
public class CHObserverBiz3 implements IObserver {
    public static Map<ObservableEnum, List<Class>> eventMap = Maps.newHashMap();
    public static boolean isOpen = true;
    public static boolean isZip = true;
    public static int PackMaxNum = 100;

    @Override
    public void registObserverEnum() {
        eventMap = AnnotationUtil.getAnnoTypeAll("com.hm.chsdk.playerevent", "com.hm.chsdk.annotation.EventMsg");
        eventMap.keySet().forEach(e -> {
            ObserverRouter.getInstance().registObserver(e, this);
        });
    }

    @Override
    public void invoke(ObservableEnum observableEnum, Player player,
                       Object... argv) {
        if (player == null || CHSDKContants.SDKClose) {
            return;
        }
        List<Class> eventData = eventMap.get(observableEnum);
        eventData.forEach(e -> {
            try {
                CommonParamInter tempEvent = (CommonParamInter) e.newInstance();
                tempEvent.initInfo(player, argv);
                PlayerInfoUploadEvent event = (PlayerInfoUploadEvent) tempEvent.createCHEvent(player);
                if (event != null) {
                    event.loadExtraParams(tempEvent);
                    event.sendSDK();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private boolean isSendCH(Player player) {
        return player.playerFix().isSendCHSDK();
    }
}
