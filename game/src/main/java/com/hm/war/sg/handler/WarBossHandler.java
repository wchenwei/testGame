package com.hm.war.sg.handler;

import com.hm.config.excel.FieldBossConfig;
import com.hm.config.excel.NpcConfig;
import com.hm.config.excel.TankConfig;
import com.hm.config.excel.templaextra.FieldBossRewardTemplateImpl;
import com.hm.config.excel.templaextra.NpcTroopTemplate;
import com.hm.config.excel.templaextra.PvpNpcTemplate;
import com.hm.enums.TankAttrType;
import com.hm.libcore.spring.SpringUtil;
import com.hm.war.sg.Frame;
import com.hm.war.sg.event.FullHpEvent;
import com.hm.war.sg.setting.TankSetting;
import com.hm.war.sg.unit.Unit;
import com.hm.war.sg.unit.UnitAttr;
import com.hm.war.sg.unit.UnitInfo;
import com.hm.war.sg.unit.UnitSetting;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: boss第三方处理器 用于死亡后换boss属性
 * @date 2024/4/30 9:35
 */
@Slf4j
@Getter
@NoArgsConstructor
public class WarBossHandler implements IWarHandler{
    private int bossId;
    private int index;

    public WarBossHandler(int bossId, int index) {
        this.bossId = bossId;
        this.index = index;
    }

    @Override
    public void doUnitDeath(Frame frame, Unit unit) {
        UnitSetting setting = unit.getSetting();
        UnitInfo unitInfo = unit.getSetting().getUnitInfo();
        if(!unitInfo.isNpc()) {
            return;//不是npc
        }
        // 下一个boss
        int newBossId = this.bossId + 1;
        FieldBossConfig bossConfig = SpringUtil.getBean(FieldBossConfig.class);
        FieldBossRewardTemplateImpl bossRewardTemplate = bossConfig.getFieldBossRewardTemplate(newBossId);
        if (bossRewardTemplate == null){
            log.error("boos不存在{}", newBossId);
            return;
        }
        int npcId = bossRewardTemplate.getNpcId(index);
        NpcConfig npcConfig = SpringUtil.getBean(NpcConfig.class);
        NpcTroopTemplate npcTroopTemplate = npcConfig.getNpcTroopTemplate(npcId);
        if (npcTroopTemplate == null){
            log.error("boss npcTroop不存在{}", npcId);
            return;
        }
        PvpNpcTemplate pvpNpcTemplate = npcConfig.getPvpNpcTemplate(npcTroopTemplate.getPvpNpcId());
        TankConfig tankConfig = SpringUtil.getBean(TankConfig.class);
        TankSetting tankSetting = tankConfig.getTankSetting(pvpNpcTemplate.getModel());
        Map<Integer, Double> attrMap = pvpNpcTemplate.getNpcAttrInit(tankSetting);
        UnitAttr unitAttr = new UnitAttr();
        unitAttr.addAttrMap(attrMap);
        long newMaxHp = unitAttr.getLongValue(TankAttrType.HP);//新的血量
        //改变血量
        setting.setInitHp(newMaxHp);
        setting.setMaxHp(newMaxHp);
        unit.getHpEngine().setInitHp(newMaxHp);
        //改变boss属性
        setting.setUnitAttr(unitAttr);
        unitInfo.setNpcId(pvpNpcTemplate.getId());
        changeBossId(newBossId);
        frame.addEvent(new FullHpEvent(unit, newBossId));
    }

    private void changeBossId(int newBossId) {
        this.bossId = newBossId;
    }

    public int getBossId() {
        return bossId;
    }
}
