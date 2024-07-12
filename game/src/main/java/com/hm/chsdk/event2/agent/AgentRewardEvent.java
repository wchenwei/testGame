package com.hm.chsdk.event2.agent;

import cn.hutool.core.convert.Convert;
import com.hm.libcore.spring.SpringUtil;
import com.hm.action.agent.AgentBiz;
import com.hm.model.agent.Agent;
import com.hm.model.player.Player;

/**
 * 特工送礼物
 *
 * @author xb
 */
//@EventMsg(obserEnum = ObservableEnum.CHAgentReward)
public class AgentRewardEvent extends AgentEvent {
    @Override
    public void init(Player player, Object... argv) {
        super.init(player, argv);

        itemId = Convert.toInt(argv[1]);

        AgentBiz agentBiz = SpringUtil.getBean(AgentBiz.class);
        Agent oldAgent = Convert.convert(Agent.class, argv[2]);
        if (oldAgent != null) {
            oldTalent = oldAgent.getTalent();
            oldCharm = oldAgent.getCharm();
            oldIntimacy = agentBiz.getTotalIntimacy(oldAgent);
        }

        Agent agent = player.playerAgent().getAgent(agentId);
        if (agent != null) {
            talent = agent.getTalent();
            charm = agent.getCharm();
            intimacy = agentBiz.getTotalIntimacy(agent);
        }

        event_id = "10004";
        event_name = "特工送礼物";
    }

    private int itemId;
    // old
    // 天赋点
    private int oldTalent;
    // 亲密度
    private int oldIntimacy;
    // 魅力值
    private int oldCharm;
    //new
    // 天赋点
    private int talent;
    // 亲密度
    private int intimacy;
    // 魅力值
    private int charm;
}
