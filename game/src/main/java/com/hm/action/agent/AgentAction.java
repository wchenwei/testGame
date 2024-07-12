package com.hm.action.agent;

import com.google.common.collect.Maps;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.item.ItemBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.action.vip.VipBiz;
import com.hm.config.GameConstants;
import com.hm.config.MissionConfig;
import com.hm.config.excel.AgentConfig;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.ItemConfig;
import com.hm.config.excel.temlate.AgentLevelUpTemplate;
import com.hm.config.excel.temlate.ItemTemplate;
import com.hm.config.excel.templaextra.AgentBaseTemplateImpl;
import com.hm.config.excel.templaextra.AgentCenterUpgradeExtraTemplate;
import com.hm.config.excel.templaextra.AgentFeedbackTemplateImpl;
import com.hm.enums.*;
import com.hm.log.LogBiz;
import com.hm.message.MessageComm;
import com.hm.model.agent.Agent;
import com.hm.model.item.Items;
import com.hm.model.player.CurrencyKind;
import com.hm.model.player.Player;
import com.hm.model.player.PlayerCurrency;
import com.hm.observer.ObservableEnum;
import com.hm.sysConstant.SysConstant;
import com.hm.util.ItemUtils;
import com.hm.util.MathUtils;
import com.rits.cloning.Cloner;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:
 * User: yang xb
 * Date: 2019-07-09
 *
 * @author Administrator
 */
@Action
public class AgentAction extends AbstractPlayerAction {
    @Resource
    private AgentConfig agentConfig;
    @Resource
    private CommValueConfig commValueConfig;
    @Resource
    private ItemBiz itemBiz;
    @Resource
    private VipBiz vipBiz;
    @Resource
    private MissionConfig missionConfig;
    @Resource
    private PlayerBiz playerBiz;
    @Resource
    private ItemConfig itemConfig;
    @Resource
    private AgentBiz agentBiz;
    @Resource
    private LogBiz logBiz;

    /**
     * 激活
     *
     * @param player
     * @param msg
     */
    @MsgMethod(MessageComm.C2S_Agent_Active)
    public void active(Player player, JsonMsg msg) {
        if (!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.Agent)) {
            player.sendErrorMsg(SysConstant.Function_Lock);
            return;
        }
        int agentId = msg.getInt("id");

        // 已经激活 !!!禁止重复激活
        if (null != player.playerAgent().getAgent(agentId)) {
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }

        AgentBaseTemplateImpl baseCfg = agentConfig.getBaseCfg(agentId);
        if (baseCfg == null) {
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }
        // 激活需要消耗的item id
        Integer itemId = baseCfg.getItem_id();
        // 数量
        int count = commValueConfig.getCommValue(CommonValueType.AgentRecruitCost);
        if (!itemBiz.checkItemEnoughAndSpend(player, itemId, count, ItemType.ITEM, LogType.Agent.value("active"))) {
            player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
            return;
        }

        Agent agent = new Agent(agentId);
        agent.setLastUpdateTime(System.currentTimeMillis());
        agent.setPower(commValueConfig.getCommValue(CommonValueType.AgentDispatch_5));
        logBiz.addPlayerActionLog(player, ActionType.AgentLvUp.getCode(),String.format("%s_%s", agent.getId(), agent.getLv()));
        player.playerAgent().addAgent(agent);
        player.notifyObservers(ObservableEnum.Agent, agentId, "active");
        player.notifyObservers(ObservableEnum.CHAgentActive, agentId);
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Agent_Active);
    }

    @MsgMethod(MessageComm.C2S_Agent_Use_Item)
    public void useItem(Player player, JsonMsg msg) {
        if (!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.Agent)) {
            player.sendErrorMsg(SysConstant.Function_Lock);
            return;
        }
        int itemId = msg.getInt("itemId");
        int count = msg.getInt("count");
        ItemTemplate template = itemConfig.getItemTemplateById(itemId);
        if (count <= 0 || template == null) {
            return;
        }

        if (!itemBiz.checkItemEnoughAndSpend(player, itemId, count, ItemType.ITEM, LogType.Agent)) {
            player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
            return;
        }

        // template.getValue() : 单个道具可以增加的cd 值
        int addValue = template.getValue() * count;
        player.getPlayerCDs().addCdNum(CDType.Agent, addValue);
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Agent_Use_Item, addValue);
        // 客户端复用刷新体力变化逻辑,多发个信号
        player.sendMsg(MessageComm.S2C_Agent_BuyTimes);

    }

    @MsgMethod(MessageComm.C2S_Agent_BuyTimes)
    public void buyTimes(Player player, JsonMsg msg) {
        if (!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.Agent)) {
            player.sendErrorMsg(SysConstant.Function_Lock);
            return;
        }
        // 今天购买次数
        long statistics = player.getPlayerStatistics().getTodayStatistics(StatisticsType.AgentBuyTimes);
        // 是否达还有次数
        int buyCap = vipBiz.getVipPow(player, VipPowType.AgentCount);
        if (statistics >= buyCap) {
            player.sendErrorMsg(SysConstant.VIP_LV_NOT_ENOUGH);
            return;
        }

        Integer price = missionConfig.getBuyChanceTemplate((int) statistics + 1).getAgent_train_price();
        if (!playerBiz.checkAndSpendPlayerCurrency(player, CurrencyKind.Gold, price, LogType.Agent.value("buyTimes"))) {
            player.sendErrorMsg(SysConstant.PLAYER_DIAMOND_NOT);
            return;
        }

        player.getPlayerStatistics().addTodayStatistics(StatisticsType.AgentBuyTimes);

        // 单次可以加体力点数
        int addValue = commValueConfig.getCommValue(CommonValueType.AgentCdCount);
        player.getPlayerCDs().addCdNum(CDType.Agent, addValue);
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Agent_BuyTimes);
    }

    // 调教

    @MsgMethod(MessageComm.C2S_Agent_Training)
    public void training(Player player, JsonMsg msg) {
        if (!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.Agent)) {
            player.sendErrorMsg(SysConstant.Function_Lock);
            return;
        }
        int agentId = msg.getInt("agentId");
        Agent agent = player.playerAgent().getAgent(agentId);
        if (agent == null) {
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }

        int oldLv = agent.getLv();
        // 没次数了
        if (!player.getPlayerCDs().checkCanCd(CDType.Agent)) {
            return;
        }
        player.getPlayerCDs().touchCdEvent(CDType.Agent);

        Map<String, Integer> vo = Maps.newConcurrentMap();
        // 【美女特工】调教时消耗1点体力亲密度提升1点的概率
        double rate = commValueConfig.getDoubleCommValue(CommonValueType.AgentIntimacyRate);
        if (Math.random() <= rate) {
            agent.setIntimacy(agent.getIntimacy() + 1);
            vo.put("intimacy", 1);
        }

        // 【美女特工】调教消耗1点体力可得多少经验
        int expAdd = commValueConfig.getCommValue(CommonValueType.AgentTrainExp);
        double r = commValueConfig.getDoubleCommValue(CommonValueType.AgentExpRate);
        // 可获得经验的公式：CommonValueType.AgentTrainExp × （1+魅力值×CommonValueType.AgentExpRate)
        int v = (int) (expAdd * (1 + agent.getCharm() * r));
        agent.incExp(v);
        vo.put("exp", v);

        // 判断是否需要升级
        int newLv = agentConfig.calcAgentLv(agent.getExp());
        if (newLv != agent.getLv()) {
            agent.setLv(newLv);
            logBiz.addPlayerActionLog(player, ActionType.AgentLvUp.getCode(),String.format("%s_%s", agent.getId(), agent.getLv()));
            // 特工等级有变化、刷新tank战力
            player.notifyObservers(ObservableEnum.Agent, agentId, "training");
        }
        player.playerAgent().SetChanged();
        player.notifyObservers(ObservableEnum.CHAgentTraining, agentId, oldLv, v);

        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Agent_Training, vo);
    }

    // 打赏
    @MsgMethod(MessageComm.C2S_Agent_Reward)
    public void reward(Player player, JsonMsg msg) {
        if (!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.Agent)) {
            player.sendErrorMsg(SysConstant.Function_Lock);
            return;
        }
        int agentId = msg.getInt("agentId");
        int itemId = msg.getInt("itemId");
        int num = Math.max(1, msg.getInt("num"));
        ItemTemplate template = itemConfig.getItemTemplateById(itemId);
        if (template == null) {
            return;
        }

        // 特工碎片、需要验证碎片类型跟特工一致
        if (!template.getPlayer_arm().isEmpty()) {
            if (template.getEquId() != ItemType.AGENT.getType() || template.getNeedNum() != agentId) {
                return;
            }
        }

        Agent agent = player.playerAgent().getAgent(agentId);
        if (agent == null) {
            return;
        }

        if (!itemBiz.checkItemEnoughAndSpend(player, itemId, num, ItemType.ITEM, LogType.Agent)) {
            player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
            return;
        }

        Agent agentOld = Cloner.standard().deepClone(agent);

        int oldIntimacy = agentBiz.getTotalIntimacy(agent);
        // 可增加的魅力值
        //魅力值和天赋点增加相同数值
        Integer value = template.getValue() * num;
        agent.incCharm(value);
        agent.incTalent(value);

        int newIntimacy = agentBiz.getTotalIntimacy(agent);
        Map<String, Integer> vo = Maps.newConcurrentMap();
        vo.put("charm", value);
        vo.put("talent", value);
        if (newIntimacy > oldIntimacy) {
            vo.put("intimacy", newIntimacy - oldIntimacy);
        }

        player.notifyObservers(ObservableEnum.CHAgentReward, agentId, itemId, agentOld);
        player.playerAgent().SetChanged();
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Agent_Reward, vo);
    }

    @MsgMethod(MessageComm.C2S_Agent_Upgrade_Skill)
    public void upgradeSkill(Player player, JsonMsg msg) {
        if (!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.Agent)) {
            player.sendErrorMsg(SysConstant.Function_Lock);
            return;
        }
        int agentId = msg.getInt("agentId");
        int type = msg.getInt("type");
        Agent agent = player.playerAgent().getAgent(agentId);
        TankAttrType attrType = TankAttrType.getType(type);
        if (agent == null || attrType == null) {
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }

        // 查出要升级的是第几个技能
        List<TankAttrType> list = agentConfig.getBaseCfg(agentId).getTankAttrList();
        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getType() == type) {
                index = i;
                break;
            }
        }
        // 该特工不具有(type)技能
        if (index < 0) {
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }

        // 技能解锁个数
        int unlockSkillCnt = agentBiz.unlockSkillCnt(agent);
        // 技能未解锁
        if (index >= unlockSkillCnt) {
            return;
        }

        Integer nowLv = agent.getAttrLvInfo().getOrDefault(type, 0);
        AgentLevelUpTemplate levelUpCfg = agentConfig.getLevelUpCfg(nowLv);
        if (levelUpCfg == null) {
            return;
        }

        // 需要消耗的天赋点数
        Integer cost = levelUpCfg.getSkill_cost();
        if (cost <= 0 || agent.getTalent() < cost) {
            player.sendErrorMsg(SysConstant.Agent_Talent_Not);
            return;
        }

        agent.incTalent(-cost);
        // 天赋技能等级+1
        agent.getAttrLvInfo().merge(type, 1, Integer::sum);
        player.notifyObservers(ObservableEnum.Agent, agentId, "upgradeSkill");
        player.notifyObservers(ObservableEnum.CHAgentSkill, agentId, index, nowLv, agent.getAttrLvInfo().get(type), cost);
        player.playerAgent().SetChanged();
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Agent_Upgrade_Skill);
    }

    @MsgMethod(MessageComm.C2S_Agent_Action_feedback)
    public void feedback(Player player, JsonMsg msg) {
        if (!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.Agent)) {
            player.sendErrorMsg(SysConstant.Function_Lock);
            return;
        }
        int agentId = msg.getInt("agentId");
        int id = msg.getInt("id");
        Agent agent = player.playerAgent().getAgent(agentId);
        // 已经领取过
        if (agent == null || player.playerAgent().haveRecord(agentId, id)) {
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }

        AgentFeedbackTemplateImpl cfg = agentConfig.getFeedbackCfg(id);
        if (cfg == null) {
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }

        // 亲密度不足
        if (agentBiz.getTotalIntimacy(agent) < cfg.getIntimacy()) {
            player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
            return;
        }

        itemBiz.addItem(player, cfg.getRewardItems(), LogType.Agent.value("feedback"));
        // 记录下来
        player.playerAgent().addRecord(agentId, id);
        player.sendUserUpdateMsg();
        JsonMsg jsonMsg = JsonMsg.create(MessageComm.S2C_Agent_Action_feedback);
        jsonMsg.addProperty("itemList", cfg.getRewardItems());
        player.sendMsg(jsonMsg);
    }

    @MsgMethod(MessageComm.C2S_AgentCenterLvUp)
    public void agentCenterLvUp(Player player, JsonMsg msg) {
        int limit = commValueConfig.getCommValue(CommonValueType.AgentCenterLimit);
        int size = player.playerAgent().getAllAgent().size();
        if (size < limit) {
            player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
            return;
        }

        int maxLv = agentConfig.getAgentCenterMaxLv();
        int lv = player.playerAgent().getAgentCenterLv();
        if (lv >= maxLv) {
            return;
        }

        AgentCenterUpgradeExtraTemplate template = agentConfig.getAgentCenterUpgradeCfg(lv);
        if (template == null) {
            return;
        }
        if (!itemBiz.checkItemEnoughAndSpend(player, template.getCosts(), LogType.AgentCenterLvUp.value(lv + 1))) {
            player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
            return;
        }
        player.playerAgent().agentCenterLvUp();
        player.notifyObservers(ObservableEnum.AgentCenterLvUp);
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_AgentCenterLvUp, true);
    }

    @MsgMethod(MessageComm.C2S_AgentDispatch)
    public void agentDispatch(Player player, JsonMsg msg) {
        int agentId = msg.getInt("agentId");
        int idx = msg.getInt("idx");
        Agent agent = player.playerAgent().getAgent(agentId);
        if (agent == null) {
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }

        if (agent.isWorking()) {
            return;
        }

        List<Agent> workingAgent = player.playerAgent().getAllAgent().stream().filter(Agent::isWorking).collect(Collectors.toList());
        // 该位置已经占用
        if (workingAgent.stream().anyMatch(a->a.getIdx() == idx)) {
            return;
        }
        int cntLimit = commValueConfig.getCommValue(CommonValueType.AgentDispatch_1);
        if (workingAgent.size() >= cntLimit) {
            return;
        }

        long now = System.currentTimeMillis();
        if (agent.canGainPower()) {
            int powerMax = commValueConfig.getCommValue(CommonValueType.AgentDispatch_5);
            int step = commValueConfig.getCommValue(CommonValueType.AgentDispatch_2);
            int cnt = (int) MathUtils.div(now - agent.getLastUpdateTime(), step * GameConstants.SECOND);
            int realCnt = Math.min(powerMax - agent.getPower(), cnt);
            agent.incPower(realCnt);
            agent.setLastUpdateTime(agent.getLastUpdateTime() + realCnt * step * GameConstants.SECOND);
            player.playerAgent().SetChanged();
        }

        int powerMin = commValueConfig.getCommValue(CommonValueType.AgentDispatch_4);
        if (agent.getPower() < powerMin) {
            player.sendUserUpdateMsg();
            return;
        }

        agent.setWorking(true);
        agent.setIdx(idx);
        agent.setLastUpdateTime(now);
        player.playerAgent().SetChanged();
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_AgentDispatch);
    }
    @MsgMethod(MessageComm.C2S_AgentUndispatch)
    public void agentUndispatch(Player player, JsonMsg msg) {
        int agentId = msg.getInt("agentId");
        Agent agent = player.playerAgent().getAgent(agentId);
        if (agent == null) {
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }

        if (!agent.isWorking()) {
            return;
        }

        // 校验是否有可领取的奖励
        long now = System.currentTimeMillis();
        if (agent.canGainPrize()) {
            int step = commValueConfig.getCommValue(CommonValueType.AgentDispatch_3);
            int cnt = (int) MathUtils.div(now - agent.getLastUpdateTime(), step * GameConstants.SECOND);
            int realCnt = Math.min(agent.getPower(), cnt);
            agent.decPower(realCnt);

            List<Items> rewardItems = agentBiz.getDispatchRewardItems(realCnt, agentId);
            if (!rewardItems.isEmpty()) {
                itemBiz.addItem(player, rewardItems, LogType.AgentDispatch.value(agentId));
            }
        }
        agent.setWorking(false);
        agent.setLastUpdateTime(now);
        player.playerAgent().SetChanged();
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_AgentUndispatch);
    }

    @MsgMethod(MessageComm.C2S_AgentSync)
    public void agentSync(Player player, JsonMsg msg) {
        int agentId = msg.getInt("agentId");
        Agent agent = player.playerAgent().getAgent(agentId);
        if (agent == null) {
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }

        JsonMsg jsonMsg = new JsonMsg(MessageComm.S2C_AgentSync);
        long now = System.currentTimeMillis();
        // 收取体力
        if (agent.canGainPower()) {
            // 体力上限
            int powerMax = commValueConfig.getCommValue(CommonValueType.AgentDispatch_5);
            int step = commValueConfig.getCommValue(CommonValueType.AgentDispatch_2);
            int cnt = (int) MathUtils.div(now - agent.getLastUpdateTime(), step * GameConstants.SECOND);
            int realCnt = Math.min(powerMax - agent.getPower(), cnt);
            agent.incPower(realCnt);
            agent.setLastUpdateTime(agent.getLastUpdateTime() + realCnt * step * GameConstants.SECOND);
        } else if (agent.canGainPrize()) { //收奖励
            int step = commValueConfig.getCommValue(CommonValueType.AgentDispatch_3);
            int cnt = (int) MathUtils.div(now - agent.getLastUpdateTime(), step * GameConstants.SECOND);
            int realCnt = Math.min(agent.getPower(), cnt);
            agent.decPower(realCnt);
            agent.setLastUpdateTime(agent.getLastUpdateTime() + realCnt * step * GameConstants.SECOND);

            // List<Items> rewardItems = agentBiz.getDispatchRewardItems(realCnt, agentId);
            // itemBiz.addItem(player, rewardItems, LogType.AgentDispatch.value(agentId));
            // jsonMsg.addProperty("itemList", rewardItems);
            // 奖励存起来不发，用户后期手动领取
            player.playerAgent().getDispatchReward().addAll(agentBiz.getDispatchRewardItems(realCnt, agentId));
        } else {
            return;
        }
        player.playerAgent().SetChanged();
        player.sendUserUpdateMsg();
        player.sendMsg(jsonMsg);
    }

    @MsgMethod(MessageComm.C2S_AgentReward)
    public void getDispatchReward(Player player, JsonMsg msg) {
        if (!player.playerAgent().getDispatchReward().isEmpty()) {
            List<Items> items = ItemUtils.mergeItemList(player.playerAgent().getDispatchReward());
            itemBiz.addItem(player, items, LogType.AgentDispatch);
            player.playerAgent().getDispatchReward().clear();
            player.playerAgent().SetChanged();
            player.sendUserUpdateMsg();
            player.sendMsg(MessageComm.S2C_AgentReward, items);
        }
    }
}
