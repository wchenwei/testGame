package com.hm.chsdk.event2.login;

import com.hm.chsdk.annotation.EventMsg;
import com.hm.chsdk.event2.CommonParamEvent;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;

/**
 * @ClassName ReLoginEvent
 * @Deacription ReLoginEvent
 * @Author zxj
 * @Date 2022/3/3 14:02
 * @Version 1.0
 **/
@EventMsg(obserEnum = ObservableEnum.PlayerReLogin)
public class ReLoginEvent extends CommonParamEvent {
    public void init(Player player, Object... argv) {
        event_id = "3004";
        event_name = "断线重连";
        event_type_id="003";
        event_type_name="登陆相关";

        Profession_Id = player.getGuildId();
        Is_Success = (boolean) argv[0];
    }
    private int Profession_Id;       //阵营
    private boolean Is_Success;  //是否成功
}
