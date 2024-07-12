package com.hm.war.sg;


import com.google.common.collect.Lists;
import com.hm.config.excel.FieldBossConfig;
import com.hm.config.excel.templaextra.FieldBossRewardTemplateImpl;
import com.hm.libcore.spring.SpringUtil;
import com.hm.model.fight.FightProxy;
import com.hm.war.sg.handler.WarBossHandler;
import com.hm.war.sg.troop.AbstractFightTroop;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class FieldBossResult {
    private int totalFrame;
    private long totalHurt;
    private int lastWinBoosId;// 最后一个胜利的bossId
    private List<WarResult> warResults = Lists.newArrayList();
    private Object atkInfo;//攻击信息
    private Object defInfo;//防守信息


    public void doWarFight(List<AbstractFightTroop> atkList, AbstractFightTroop def, WarParam warParam) {
        int maxCount = atkList.size();
        WarBossHandler warHandler = (WarBossHandler) warParam.getWarHandler();
        for (int i = 0; i < maxCount; i++) {
            AbstractFightTroop atk = findWarTroop(atkList);
            if(atk == null) {
                break;
            }
            WarResult warResult = new FightProxy().doFight(atk, def, warParam);
            warResults.add(warResult);
            totalFrame += warResult.getBattleFrame();
            warParam.reduceBattleFrame(warResult.getBattleFrame());
            int bossId = warHandler.getBossId();
            if(warResult.isAtkWin()) {
                atk.doWarResult(warResult.getAtk());
                def.getTankList().forEach(e -> e.setHp(0));
                lastWinBoosId = bossId;
            }else{
                def.doWarResult(warResult.getDef());
                atk.getTankList().forEach(e -> e.setHp(0));
                this.lastWinBoosId = bossId - 1;
            }
            if (isEnd(def, warParam, maxCount, i)){
                calAtkTotalHp(warHandler, warResult, bossId);
                break;
            }
        }
    }

    // 计算boss掉的总血量
    private void calAtkTotalHp(WarBossHandler warHandler, WarResult warResult, int bossId) {
        FieldBossConfig fieldBossConfig = SpringUtil.getBean(FieldBossConfig.class);
        FieldBossRewardTemplateImpl bossRewardTemplate = fieldBossConfig.getFieldBossRewardTemplate(bossId);
        // 最后一场boss剩余的血量
        long lastHp = warResult.isAtkWin() ? 0 : warResult.getDef().getTotalTankHp();
        this.totalHurt = bossRewardTemplate.getBeforeBossTotalHp(warHandler.getIndex()) + bossRewardTemplate.getBossHp(warHandler.getIndex()) - lastHp;
    }

    // 最后一场 或者时间结束 或者防御方死亡
    private boolean isEnd(AbstractFightTroop def, WarParam warParam, int maxCount, int i) {
        return i == maxCount - 1 || warParam.getMaxFrame() <= 0 || !def.haveLifeTank();
    }

    public AbstractFightTroop findWarTroop(List<AbstractFightTroop> troopList) {
        for (AbstractFightTroop troop : troopList) {
            if(troop.haveLifeTank()) {
                return troop;
            }
        }
        return null;
    }


}
