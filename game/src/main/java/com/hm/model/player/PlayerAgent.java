package com.hm.model.player;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.AgentConfig;
import com.hm.model.agent.Agent;
import com.hm.model.item.Items;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Description:特工
 * User: yang xb
 * Date: 2019-07-04
 *
 * @author Administrator
 */
public class PlayerAgent extends PlayerDataContext {
    /**
     * Agent::GetId, Agent
     */
    private Map<Integer, Agent> agentMap = Maps.newConcurrentMap();
    /**
     * 回馈领取记录
     * key:agentId
     */
    private Map<Integer, List<Integer>> feedbackRecord = Maps.newConcurrentMap();

    /**
     * 特工中心等级
     */
    private int agentCenterLv;
    /**
     * 特工派遣已结算、未领取的奖励
     */
    private List<Items> dispatchReward = Lists.newArrayList();

    public Agent getAgent(int agentId) {
        return agentMap.getOrDefault(agentId, null);
    }

    public Collection<Agent> getAllAgent() {
        return agentMap.values();
    }

    /**
     * 该特工是否领取过对应id的回馈奖励
     *
     * @param agentId
     * @param feedbackId
     * @return
     */
    public boolean haveRecord(int agentId, int feedbackId) {
        return feedbackRecord.containsKey(agentId) && feedbackRecord.get(agentId).contains(feedbackId);
    }

    public void addRecord(int agentId, int feedbackId) {
        if (feedbackRecord.containsKey(agentId)) {
            feedbackRecord.get(agentId).add(feedbackId);
        } else {
            feedbackRecord.put(agentId, Lists.newArrayList(feedbackId));
        }
        SetChanged();
    }

    public void addAgent(Agent agent) {
        agentMap.put(agent.getId(), agent);
        SetChanged();
    }

    public int getAgentCenterLv() {
        return agentCenterLv;
    }

    public void agentCenterLvUp() {
        AgentConfig agentConfig = SpringUtil.getBean(AgentConfig.class);
        if (agentConfig == null) {
            return;
        }
        int maxLv = agentConfig.getAgentCenterMaxLv();
        if (agentCenterLv < maxLv) {
            agentCenterLv++;
            SetChanged();
        }
    }

    public List<Items> getDispatchReward() {
        return dispatchReward;
    }

    @Override
    protected void fillMsg(JsonMsg msg) {
        msg.addProperty("playerAgent", this);
    }
}
