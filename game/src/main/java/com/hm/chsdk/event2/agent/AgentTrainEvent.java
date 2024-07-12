package com.hm.chsdk.event2.agent;

import cn.hutool.core.convert.Convert;
import com.hm.model.agent.Agent;
import com.hm.model.player.Player;

/**
 * 特工特训
 *
 * @author xb
 */
//@EventMsg(obserEnum = ObservableEnum.CHAgentTraining)
public class AgentTrainEvent extends AgentEvent {
    @Override
    public void init(Player player, Object... argv) {
        super.init(player, argv);

        oldLv = Convert.toInt(argv[1]);
        expGet = Convert.toInt(argv[2]);

        Agent agent = player.playerAgent().getAgent(agentId);
        if (agent != null) {
            newLv = agent.getLv();
            expNow = agent.getExp();
        }

        event_id = "10002";
        event_name = "特工特训";
    }

    private int oldLv;
    private int newLv;
    private int expGet;
    private int expNow;

}
