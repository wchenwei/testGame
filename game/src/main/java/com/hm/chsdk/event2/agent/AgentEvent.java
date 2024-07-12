package com.hm.chsdk.event2.agent;

import cn.hutool.core.convert.Convert;
import com.hm.chsdk.event2.CommonParamEvent;
import com.hm.model.player.Player;

public abstract class AgentEvent extends CommonParamEvent {
    public int agentId;

    @Override
    public void init(Player player, Object... argv) {
        event_type_id = "010";
        event_type_name = "特工";
        agentId = Convert.toInt(argv[0]);
    }
}
