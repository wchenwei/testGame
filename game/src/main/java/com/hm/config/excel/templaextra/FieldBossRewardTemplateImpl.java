package com.hm.config.excel.templaextra;

import com.hm.config.excel.NpcConfig;
import com.hm.config.excel.temlate.FieldBossRewardTemplate;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.util.string.StringUtil;

import java.util.List;

@FileConfig("field_boss_reward")
public class FieldBossRewardTemplateImpl extends FieldBossRewardTemplate {
    private int [] dropIds;
    private int [] enemyIds;
    public int [] bossHp;
    public int [] beforeBossTotalHp;

    public void init(List<FieldBossRewardTemplateImpl> templates){
        NpcConfig npcConfig = SpringUtil.getBean(NpcConfig.class);
        this.dropIds = StringUtil.splitStr2IntArray(getDrop_id(),",");
        this.enemyIds = StringUtil.splitStr2IntArray(getEnemy_id(), ",");
        bossHp = new int[enemyIds.length];
        beforeBossTotalHp = new int[enemyIds.length];
        for (int i = 0; i < enemyIds.length; i++) {
            int enemyId = enemyIds[i];
            NpcTroopTemplate npcTroopTemplate = npcConfig.getNpcTroopTemplate(enemyId);
            PvpNpcTemplate pvpNpcTemplate = npcConfig.getPvpNpcTemplate(npcTroopTemplate.getPvpNpcId());
            this.bossHp[i] = pvpNpcTemplate.getHp();
            for (FieldBossRewardTemplateImpl template : templates) {
                if (template.getId() < getId()){
                    this.beforeBossTotalHp[i]+= template.getBossHp(i+1);
                }
            }
        }
    }

    public int getDropId(int index){
        return dropIds[index-1];
    }

    public int getNpcId(int index){
        return enemyIds[index-1];
    }

    public int getBossHp(int index){
        return bossHp[index-1];
    }

    public int getBeforeBossTotalHp(int index){
        return beforeBossTotalHp[index-1];
    }
}
