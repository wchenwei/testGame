package com.hm.chsdk.event2.agent;

import cn.hutool.core.convert.Convert;
import com.hm.model.agent.Agent;
import com.hm.model.player.Player;

/**
 * 特工天赋升级
 *
 * @author xb
 */
//@EventMsg(obserEnum = ObservableEnum.CHAgentSkill)
public class AgentUpgradeSkillEvent extends AgentEvent {
    @Override
    public void init(Player player, Object... argv) {
        super.init(player, argv);

        id = Convert.toInt(argv[1]);
        oldLv = Convert.toInt(argv[2]);
        newLv = Convert.toInt(argv[3]);
        cost = Convert.toInt(argv[4]);

        Agent agent = player.playerAgent().getAgent(agentId);
        if (agent != null) {
            remain = agent.getTalent();
        }

        event_id = "10003";
        event_name = "特工天赋升级";
    }

    private int id;
    private int oldLv;
    private int newLv;
    private int cost;
    private int remain;

}
