package com.hm.chsdk.playerevent;

import com.hm.chsdk.ICHEvent;
import com.hm.chsdk.annotation.EventMsg;
import com.hm.chsdk.event2.CommonParamInter;
import com.hm.chsdk.event2.PhoneUpEvent;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;

/**
 * @ClassName PlayerBindPhoneEvent
 * @Deacription 绑定手机号
 * @Author xjt
 * @Date 2022年4月1日15:53:13
 * @Version 1.0
 **/
@EventMsg(obserEnum = ObservableEnum.BindPhone)
public class PlayerBindPhoneEvent implements CommonParamInter {
    private String mobile;

    @Override
    public void initInfo(Player player, Object... argv) {
        this.mobile = (String) argv[0];
    }

    @Override
    public void init(Player player, Object... argv) {

    }

    public String getMobile() {
        return mobile;
    }

    @Override
    public ICHEvent createCHEvent(Player player) {
        return new PhoneUpEvent(player);
    }
}
