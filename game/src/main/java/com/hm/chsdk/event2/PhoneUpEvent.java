package com.hm.chsdk.event2;

import com.hm.chsdk.playerevent.PlayerBindPhoneEvent;
import com.hm.model.player.Player;

/**
 * @author xjt
 * @version 1.0
 * @desc 表述
 * @date 2022/4/1 15:46
 */
public class PhoneUpEvent extends PlayerInfoUploadEvent {
    private String mobile;

    public PhoneUpEvent(Player player) {
        super(player);
    }

    public void loadExtraParams(CommonParamInter param) {
        this.mobile = ((PlayerBindPhoneEvent) param).getMobile();
    }

    @Override
    public String buildUrl() {
        return CHSDKContants2.PhoneUrl;
    }

    @Override
    public Class buildClass() {
        return PhoneUpEvent.class;
    }
}
