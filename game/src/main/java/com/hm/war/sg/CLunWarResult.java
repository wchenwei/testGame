package com.hm.war.sg;

import com.google.common.collect.Lists;
import com.hm.model.fight.FightProxy;
import com.hm.war.sg.troop.AbstractFightTroop;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CLunWarResult {
    private int maxFrame;//最大战斗总帧数
    private int totalFrame;//战斗总帧数
    private boolean isAtkWin;//是否胜利
    private List<WarResult> warResults = Lists.newArrayList();//战斗报告
    private Object atkInfo;//攻击信息
    private Object defInfo;//防守信息


    public void doWarFight(List<AbstractFightTroop> atkList, List<AbstractFightTroop> defList,WarParam warParam) {
        this.maxFrame = warParam.getMaxFrame();

        int maxCount = atkList.size() + defList.size();
        for (int i = 0; i < maxCount; i++) {
            AbstractFightTroop atk = findWarTroop(atkList);
            AbstractFightTroop def = findWarTroop(defList);

            if(atk == null || def == null) {
                calEnd();
                return;
            }
            WarResult warResult = new FightProxy().doFight(atk, def,warParam);
            warResults.add(warResult);
            if(warResult.isAtkWin()) {
                atk.doWarResult(warResult.getAtk());
                def.getTankList().forEach(e -> e.setHp(0));
            }else{
                def.doWarResult(warResult.getDef());
                atk.getTankList().forEach(e -> e.setHp(0));
            }

            totalFrame += warResult.getBattleFrame();

            //扣除本次战斗帧
            warParam.reduceBattleFrame(warResult.getBattleFrame());
            if(!warParam.haveNextBattleFrame()) {
                calEnd();
                return;
            }
        }
    }

    public void calEnd() {
        this.isAtkWin = this.warResults.isEmpty() || warResults.get(warResults.size()-1).isAtkWin();
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
