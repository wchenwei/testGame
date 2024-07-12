package com.hm.action.battle.Handler;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Maps;
import com.hm.action.bag.BagBiz;
import com.hm.config.MissionConfig;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.temlate.YuHunBuffTemplate;
import com.hm.config.excel.templaextra.MissionSpecailExtraTemplate;
import com.hm.enums.CommonValueType;
import com.hm.enums.RankType;
import com.hm.leaderboards.HdLeaderboardsService;
import com.hm.libcore.annotation.Biz;
import com.hm.model.battle.TowerBattle;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.util.ArrayUtils;
import com.hm.war.sg.WarResult;
import com.hm.war.sg.troop.NpcTroop;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 驭魂之地
 * @Author chenwei
 * @Date 2024/5/13
 * @Description:
 */
@Biz
public class TowerBattleHandler extends AbstractBattleHandler<TowerBattle>{

    @Resource
    private MissionConfig missionConfig;
    @Resource
    private CommValueConfig commValueConfig;
    @Resource
    private BagBiz bagBiz;


    @Override
    public void doFightAfter(Player player, WarResult warResult, TowerBattle battle, int id) {
        if (warResult.isAtkWin()){
            battle.setCurId(id);
            checkLastCnt(player, battle);
            if (battle.isFirstWin(id)){
                battle.setMaxHistory(id);
                MissionSpecailExtraTemplate template = missionConfig.getBattle(id);
                //同步到排行榜
                HdLeaderboardsService.getInstance().updatePlayerRankOverride(player, RankType.TowerRank, template.getLevel_number());
            }
            player.playerBattle().SetChanged();
        }
    }

    @Override
    public List<Items> getFightReward(TowerBattle battle, WarResult warResult, int id) {
        if (warResult.isAtkWin()){
            MissionSpecailExtraTemplate template = missionConfig.getBattle(id);
            List<Items> rewards;
            if (battle.isFirstWin(id)){
                rewards = template.getFirstRewards();
            }else {
                rewards = template.getFightRewards();
            }
            return bagBiz.getRandomDropItem(rewards);
        }
        return null;
    }

    @Override
    public boolean isCanSweep(Player player, TowerBattle battle, int id) {
        return battle != null && battle.isCanSweep(id);
    }

    @Override
    public List<Items> sweep(Player player, TowerBattle battle, int id) {
        MissionSpecailExtraTemplate template = missionConfig.getBattle(id);
        if (template == null){
            return null;
        }
        battle.setCurId(id);
        checkLastCnt(player, battle);
        player.playerBattle().SetChanged();
        return template.getSweepRewards();
    }

    private void checkLastCnt(Player player, TowerBattle battle) {
        int totalCnt = getBuffTotalCnt(battle);
        battle.setLastCnt(Math.max(0, totalCnt - battle.getBuffCnt()));
        if (battle.getLastCnt() > 0 && battle.getRandBuffIds().isEmpty()){
            randomBuffIds(player, battle);
        }
    }

    @Override
    public NpcTroop buildNpcTroop(TowerBattle battle, int npcId) {
        NpcTroop npcTroop = super.buildNpcTroop(battle, npcId);
        Map<Integer, Integer> skillMap = getSkillMap(battle);
        npcTroop.getExtraSkillLvs().putAll(skillMap);
        return npcTroop;
    }

    // 添加buff
    public void addBuff(Player player, TowerBattle battle, Integer id, int index){
        battle.addBuff(id, index);
        checkLastCnt(player, battle);
        if (battle.getLastCnt() > 0){// 如果没有达到最大次数  随机下次buff
            randomBuffIds(player, battle);
        }
        player.playerBattle().SetChanged();
    }

    // 一键设置buff
    public void autoAddAllBuff(Player player, TowerBattle battle){
        // 剩余随机次数
        int lastCnt = battle.getLastCnt();
        if (lastCnt <= 0){
            return;
        }
        // 解锁到的最大位置
        int maxPosition = getMaxPosition(battle);
        // 预设类型
        int[] preBuffType = battle.getPreBuffType();
        // 预设是否为空
        boolean preEmpty = ArrayUtils.isEmptyOrAllZero(preBuffType);
        for (int i = 0; i < lastCnt; i++) {
            int luckId = findLuckBuffId(battle, preBuffType, preEmpty);
            int index = -1;// 位置
            if (luckId > 0){
                int buffIndex = battle.getBuffIndex(luckId);
                if (buffIndex >= 0){// 已存在 等级+1
                    index = buffIndex;
                } else {
                    int emptyIndex = battle.getFirstEmptyIndex(maxPosition);
                    if (emptyIndex >= 0){// 还有空位置
                        index = emptyIndex;
                    }
                }
            }
            if (index >= 0){
                battle.addBuff(luckId, index);
            } else {// 没有位置放弃本次选择 次数+1
                battle.addBuffCont();
            }
        }
        battle.setLastCnt(0);// 清空剩余次数
        player.playerBattle().SetChanged();
    }

    private int findLuckBuffId(TowerBattle battle, int[] preBuffType, boolean preEmpty) {
        List<Integer> buffIds = battle.getRandBuffIds();
        if (CollUtil.isEmpty(buffIds)){
            // 随机三个buff
            buffIds = missionConfig.randomBuffIds();
        }
        if (!preEmpty){// 有预设
            for (int buffType : preBuffType) {
                if (buffType > 0){
                    Integer buffId = buffIds.stream()
                            .filter(e -> missionConfig.getYuHunBuffTemplate(e).getType() == buffType)
                            .max(Integer::compareTo)
                            .orElse(0);
                    if (buffId > 0){
                        return buffId;
                    }
                }
            }
        }
        // 没有找到预设  默认第一个
        return buffIds.get(0);
    }

    // 随机buff列表
    public void randomBuffIds(Player player, TowerBattle battle){
        List<Integer> buffIds = missionConfig.randomBuffIds();
        battle.setRandBuffIds(buffIds);
    }

    //检查是否可以随机buff
    public boolean checkCanRandomBuff(TowerBattle battle){
        if (CollUtil.isNotEmpty(battle.getRandBuffIds())){// 已经随机过了
            return false;
        }
        return battle.getLastCnt() > 0;
    }

    //检查是否可以添加buff
    public boolean checkCanAddBuff(TowerBattle battle, Integer id, int index){
        if (battle.getLastCnt() <= 0){// 次数已达到最大
            return false;
        }
        if (battle.getRandBuffIds().isEmpty()){// 还没有随机buff
            return false;
        }
        if (id <= 0){// 本次放弃
            return true;
        }
        int buffIndex = battle.getBuffIndex(id);
        if (buffIndex >= 0){// 已选择过
            return buffIndex == index;
        }
        // 没有选择过
        int position = getMaxPosition(battle);
        return index >= 0 && index <= position;

    }

    // 获取解锁的最大位置
    public int getMaxPosition(TowerBattle towerBattle){
        int historyId = towerBattle.getMaxHistory();
        MissionSpecailExtraTemplate template = missionConfig.getBattle(historyId);
        int levelNum = template == null ? 0 : template.getLevel_number();
        int [] unlockLevel = commValueConfig.getConvertObj(CommonValueType.TowerBuffPosUnlockMission);
        for (int i = unlockLevel.length - 1; i >= 0 ; i--) {
            if (levelNum >= unlockLevel[i]){
                return i;
            }
        }
        return -1;
    }

    // 获取可设置的总次数
    private int getBuffTotalCnt(TowerBattle battle) {
        MissionSpecailExtraTemplate template = missionConfig.getBattle(battle.getNextId());
        return template != null ? template.getChapter_id() : 0;
    }

    public Map<Integer, Integer> getSkillMap(TowerBattle battle){
        Map<Integer, Integer> skillMap = Maps.newHashMap();
        int[][] buffsLv = battle.getBuffsLv();
        for (int i = 0; i < buffsLv.length; i++) {
            int buffId = buffsLv[i][0];
            int buffLv = buffsLv[i][1];
            if (buffId > 0){
                YuHunBuffTemplate template = missionConfig.getYuHunBuffTemplate(buffId);
                if (template != null){
                    skillMap.put(template.getSkill_id(), buffLv);
                }
            }
        }
        return skillMap;
    }


}
