package com.hm.chsdk.event2;

import com.hm.chsdk.ICHEvent;
import com.hm.model.player.Player;

/**
 * @ClassName CommonParamInter
 * @Deacription CommonParamInter
 * @Author zxj
 * @Date 2022/3/4 10:42
 * @Version 1.0
 **/
public interface CommonParamInter {
    public void initInfo(Player player, Object... argv);
    public void init(Player player, Object... argv);

    default ICHEvent createCHEvent(Player player) {
        return null;
    }
}
