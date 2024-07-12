package com.hm.model.agent;

import com.google.common.collect.Maps;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.GameConstants;
import com.hm.config.excel.CommValueConfig;
import com.hm.enums.CommonValueType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Description:特工
 * User: yang xb
 * Date: 2019-07-04
 *
 * @author Administrator
 */
@Data
@NoArgsConstructor
public class Agent {
    private int id;
    private int lv;
    private int exp;
    // 亲密度
    private int intimacy;
    // 魅力值
    private int charm;
    // 天赋点
    private int talent;
    // private Map<TankAttrType, Integer> attrLvInfo = Maps.newConcurrentMap();
    /**
     * key, TankAttrType::getType, lv
     */
    private Map<Integer, Integer> attrLvInfo = Maps.newConcurrentMap();

    /**
     * 派遣中
     */
    private boolean isWorking;
    /**
     * 体力
     */
    private int power;
    /**
     * 派遣所在位置
     */
    private int idx;
    private long lastUpdateTime;
    public Agent(int id) {
        this.id = id;
        this.lv = 1;
        // 初始化天赋技能等级
        // AgentConfig agentConfig = SpringUtil.getBean(AgentConfig.class);
        // if (agentConfig != null) {
        //     Set<TankAttrType> types = agentConfig.getTankAttrMap(id).keySet();
        //     types.forEach(type -> attrLvInfo.put(type, 0));
        // }
    }

    public void incExp(int i) {
        exp += i;
    }

    public void incCharm(int i) {
        charm += i;
    }

    public void incTalent(int i) {
        talent += i;
    }

    public void decPower(int n) {
        power -= n;
    }

    public void incPower(int n) {
        power += n;
    }

    /**
     * 是否可收获派遣收益
     *
     * @return
     */
    public boolean canGainPrize() {
        if (!isWorking || power <= 0) {
            return false;
        }

        CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
        int step = commValueConfig.getCommValue(CommonValueType.AgentDispatch_3);
        return (System.currentTimeMillis() - lastUpdateTime) > step * GameConstants.SECOND;
    }
    public boolean canGainPower() {
        CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
        int powerMax = commValueConfig.getCommValue(CommonValueType.AgentDispatch_5);
        if (isWorking || power >= powerMax) {
            return false;
        }

        int step = commValueConfig.getCommValue(CommonValueType.AgentDispatch_2);
        return (System.currentTimeMillis() - lastUpdateTime) > step * GameConstants.SECOND;
    }
}
