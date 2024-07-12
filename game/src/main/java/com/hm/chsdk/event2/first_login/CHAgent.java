package com.hm.chsdk.event2.first_login;

import com.hm.model.agent.Agent;

public class CHAgent {
    public int id;//id
    public int lv;//等级
    public int v;//亲密度

    public CHAgent(Agent agent) {
        this.id = agent.getId();
        this.lv = agent.getLv();
        this.v = agent.getIntimacy();
    }
}
