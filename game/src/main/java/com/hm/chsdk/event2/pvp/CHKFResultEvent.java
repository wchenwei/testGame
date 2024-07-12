package com.hm.chsdk.event2.pvp;

import com.hm.chsdk.annotation.EventMsg;
import com.hm.chsdk.event2.CHEventType;
import com.hm.chsdk.event2.CHKeyVal;
import com.hm.chsdk.event2.CHPushUtil;
import com.hm.chsdk.event2.CommonParamEvent;
import com.hm.enums.KfType;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;

import java.util.List;
import java.util.Map;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 荣誉统计
 * @date 2023/2/27 15:50
 */
@EventMsg(obserEnum = ObservableEnum.CHKFPlayerKFResult)
public class CHKFResultEvent extends CommonParamEvent {
    @Override
    public void init(Player player, Object... argv) {
        this.cross_id = ((KfType) argv[1]).getType();
        Map playerMap = (Map) argv[2];
        this.player_list = CHPushUtil.buildListForMap(playerMap);

        System.out.println("CHKFResultEvent:" + cross_id + "->" + player_list.size());
        if (this.cross_id == KfType.Sports.getType()) {
            loadEventType(CHEventType.PVP, 6005, "military_result");
        } else {
            loadEventType(CHEventType.PVP, 6004, "cross_result");
        }

    }

    private int cross_id;
    private List<CHKeyVal> player_list;
}
