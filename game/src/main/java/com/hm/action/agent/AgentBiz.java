package com.hm.action.agent;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Biz;
import com.hm.action.item.ItemBiz;
import com.hm.action.mail.biz.MailBiz;
import com.hm.config.GameConstants;
import com.hm.config.excel.AgentConfig;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.MailConfig;
import com.hm.config.excel.temlate.MailTemplate;
import com.hm.config.excel.templaextra.AgentBaseTemplateImpl;
import com.hm.config.excel.templaextra.AgentCenterUpgradeExtraTemplate;
import com.hm.enums.*;
import com.hm.model.agent.Agent;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.tank.TankAttr;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.util.ItemUtils;
import com.hm.util.MathUtils;
import com.hm.util.RandomUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Description:
 * User: yang xb
 * Date: 2019-07-10
 */
@Biz
public class AgentBiz implements IObserver {
    @Resource
    private AgentConfig agentConfig;
    @Resource
    private CommValueConfig commValueConfig;
    @Resource
    private ItemBiz itemBiz;
    @Resource
    private MailBiz mailBiz;
    @Resource
    private MailConfig mailConfig;

    /**
     * 获取特工总亲密度
     * 0：直接获得的亲密度
     * 1：魅力值以特定比例转化的亲密度
     *
     * @param agent
     * @return
     */
    public int getTotalIntimacy(Agent agent) {
        if (agent == null) {
            return 0;
        }


        int e = commValueConfig.getCommValue(CommonValueType.AgentExchange);
        if (e <= 0) {
            return 0;
        }

        return agent.getIntimacy() + agent.getCharm() / e;
    }

    /**
     * 获取特工解锁技能数量
     *
     * @param agent
     * @return
     */
    public int unlockSkillCnt(Agent agent) {
        int[] ints = commValueConfig.getCommonValueByInts(CommonValueType.AgentUnlock);
        return (int) Arrays.stream(ints).filter(i -> agent.getLv() >= i).count();
    }

    /**
     * 全体属性
     *
     * @param player
     * @return
     */
    public Map<TankAttrType, Double> getAgentAdded(Player player) {
        Map<TankAttrType, Double> result = Maps.newConcurrentMap();
        if (!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.Agent)) {
            return result;
        }
        // 处理特工中心属性加成
        int agentCenterLvAdd = 0;
        final int agentCenterLv = player.playerAgent().getAgentCenterLv();
        AgentCenterUpgradeExtraTemplate cfg = agentConfig.getAgentCenterUpgradeCfg(agentCenterLv);
        if (cfg != null) {
            agentCenterLvAdd = cfg.getAgent_lv_add();
        }
        Collection<Agent> allAgent = player.playerAgent().getAllAgent();
        for (Agent agent : allAgent) {
            Map<TankAttrType, Double> totalAttrMap = agentConfig.getTotalAttrMap(agent.getId(), agentCenterLvAdd + agent.getLv());
            for (Map.Entry<TankAttrType, Double> entry : totalAttrMap.entrySet()) {
                result.merge(entry.getKey(), entry.getValue(), Double::sum);
            }
        }
        return result;
    }

    /**
     * 对特定tank id 的属性加成
     *
     * @param player
     * @param tankId
     * @return
     */
    public Map<TankAttrType, Double> getAgentAddedByTankId(Player player, int tankId) {
        Map<TankAttrType, Double> result = Maps.newConcurrentMap();
        if (!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.Agent)) {
            return result;
        }
        Collection<Agent> allAgent = player.playerAgent().getAllAgent();
        for (Agent agent : allAgent) {
            AgentBaseTemplateImpl cfg = agentConfig.getBaseCfg(agent.getId());
            // 只处理对应tank id 相同的
            if (!cfg.getTank_id().equals(tankId)) {
                continue;
            }
            Map<TankAttrType, Double> tankAttrMap = cfg.getTankAttrMap();
            for (Map.Entry<TankAttrType, Double> entry : tankAttrMap.entrySet()) {
                // 数值 = base value × 天赋等级
                Integer lv = agent.getAttrLvInfo().getOrDefault(entry.getKey().getType(), 0);
                // 默认是0级，0级无效果
                if (lv > 0) {
                    result.merge(entry.getKey(), entry.getValue() * lv, Double::sum);
                }
            }
        }
        return result;
    }

    @Override
    public void registObserverEnum() {
        ObserverRouter.getInstance().registObserver(ObservableEnum.FunctionUnlock, this);
        ObserverRouter.getInstance().registObserver(ObservableEnum.LOGIN, this);
        ObserverRouter.getInstance().registObserver(ObservableEnum.DayFirstLogin, this);
    }

    @Override
    public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
        if (observableEnum == ObservableEnum.FunctionUnlock) {
            int funcId = Integer.parseInt(argv[0].toString());
            if (funcId != PlayerFunctionType.Agent.getType()) {
                return;
            }
            if (!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.Agent)) {
                return;
            }
            // 解锁功能时、给玩家发放1#特工解锁所需要的碎片+个数
            AgentBaseTemplateImpl cfg = agentConfig.getBaseCfg(1);
            if (cfg == null) {
                return;
            }

            Integer pieceId = cfg.getItem_id();
            int count = commValueConfig.getCommValue(CommonValueType.AgentRecruitCost);
            itemBiz.addItem(player, ItemType.ITEM, pieceId, count, LogType.Agent.value("unlock function"));
        } else if (observableEnum == ObservableEnum.LOGIN) {
            calcAgentDispatch(player);
        } else if (observableEnum == ObservableEnum.DayFirstLogin) {
            // 每日首次登录时判断是否有未领取的奖励，有的话以邮件发送
            calcAgentDispatch(player);
            if (!player.playerAgent().getDispatchReward().isEmpty()) {
                List<Items> items = ItemUtils.mergeItemList(player.playerAgent().getDispatchReward());
                MailTemplate mailTemplate = mailConfig.getMailTemplate(MailConfigEnum.AgentDispatch);
                mailBiz.sendSysMail(player, mailTemplate, items);
                player.playerAgent().getDispatchReward().clear();
                player.playerAgent().SetChanged();
            }
        }
    }

    /**
     * 计算派遣收益、体力
     *
     * @param player
     */
    private void calcAgentDispatch(Player player) {
        long now = System.currentTimeMillis();
        int powerMax = commValueConfig.getCommValue(CommonValueType.AgentDispatch_5);
        int step = commValueConfig.getCommValue(CommonValueType.AgentDispatch_2);
        int prizeStep = commValueConfig.getCommValue(CommonValueType.AgentDispatch_3);
        List<Items> rewardList = Lists.newArrayList();
        Collection<Agent> allAgent = player.playerAgent().getAllAgent();
        boolean isChange = false;
        for (Agent agent : allAgent) {
            if (agent.canGainPrize()) {
                int cnt = (int) MathUtils.div(now - agent.getLastUpdateTime(), prizeStep * GameConstants.SECOND);
                int realCnt = Math.min(agent.getPower(), cnt);
                agent.decPower(realCnt);
                agent.setLastUpdateTime(agent.getLastUpdateTime() + realCnt * prizeStep * GameConstants.SECOND);

                rewardList.addAll(getDispatchRewardItems(realCnt, agent.getId()));
                isChange = true;
                // 体力用光后尝试下架、补体力
                if (agent.getPower() == 0) {
                    agent.setWorking(false);
                    if (agent.canGainPower()) {
                        int rc = Math.min(powerMax, (int)MathUtils.div(now - agent.getLastUpdateTime(), step * GameConstants.SECOND));
                        agent.incPower(rc);
                        agent.setLastUpdateTime(agent.getLastUpdateTime() + rc * step * GameConstants.SECOND);
                    }
                }
            } else if (agent.canGainPower()){
                int cnt = (int) MathUtils.div(now - agent.getLastUpdateTime(), step * GameConstants.SECOND);
                int realCnt = Math.min(powerMax - agent.getPower(), cnt);
                agent.incPower(realCnt);
                agent.setLastUpdateTime(agent.getLastUpdateTime() + realCnt * step * GameConstants.SECOND);
                isChange = true;
            }
        }
        if (!rewardList.isEmpty()) {
            player.playerAgent().getDispatchReward().addAll(rewardList);
        }
        if (isChange) {
            player.playerAgent().SetChanged();
        }
    }


    /**
     * 获取特工升级额外属性
     *
     * @param player
     * @return
     */
    public TankAttr calAgentCenterAttr(Player player) {
        TankAttr tankAttr = new TankAttr();
        int lv = player.playerAgent().getAgentCenterLv();
        if (lv > 0) {
            AgentCenterUpgradeExtraTemplate lvTemplate = agentConfig.getAgentCenterUpgradeCfg(lv);
            if (lvTemplate != null) {
                //本身等级带来的属性
                tankAttr.addAttr(lvTemplate.getAttrMap());
            }
            //特殊等级带来的额外属性
            tankAttr.addAttr(agentConfig.getAgentCenterExtraAttr(lv));
        }
        return tankAttr;
    }

    public List<Items> getDispatchRewardItems(int realCnt, int agentId) {
        // 单次奖励
        List<Items> listItem = commValueConfig.getListItem(CommonValueType.AgentDispatch_6);
        List<Items> rewardItems;
        if (realCnt > 1) {
            rewardItems = ItemUtils.calItemRateReward(listItem, realCnt);
        } else {
            rewardItems = Lists.newArrayList(listItem);
        }

        // 概率额外奖励
        AgentBaseTemplateImpl baseCfg = agentConfig.getBaseCfg(agentId);
        int extra = (int) IntStream.range(0, realCnt).filter(i -> RandomUtils.randomIsRate(baseCfg.getRate())).count();
        if (extra > 0) {
            if (extra > 1) {
                rewardItems.addAll(ItemUtils.calItemRateReward(baseCfg.getRewardItems(), extra));
            } else {
                rewardItems.addAll(baseCfg.getRewardItems());
            }
        }
        return rewardItems;
    }
}
