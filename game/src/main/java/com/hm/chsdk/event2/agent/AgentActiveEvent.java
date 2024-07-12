package com.hm.chsdk.event2.agent;

import com.hm.libcore.spring.SpringUtil;
import com.hm.action.item.ItemBiz;
import com.hm.config.excel.AgentConfig;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.templaextra.AgentBaseTemplateImpl;
import com.hm.enums.CommonValueType;
import com.hm.enums.ItemType;
import com.hm.model.player.Player;

/**
 * 获得特工
 *
 * @author xb
 */
//@EventMsg(obserEnum = ObservableEnum.CHAgentActive)
public class AgentActiveEvent extends AgentEvent {
    @Override
    public void init(Player player, Object... argv) {
        super.init(player, argv);

        AgentConfig agentConfig = SpringUtil.getBean(AgentConfig.class);
        AgentBaseTemplateImpl baseCfg = agentConfig.getBaseCfg(agentId);

        itemId = baseCfg.getItem_id();

        CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
        cnt = commValueConfig.getCommValue(CommonValueType.AgentRecruitCost);

        ItemBiz itemBiz = SpringUtil.getBean(ItemBiz.class);
        remain = itemBiz.getItemTotal(player, ItemType.ITEM.getType(), itemId);

        event_id = "10001";
        event_name = "获得特工";
    }

    private int itemId;
    private int cnt;
    private long remain;

}
