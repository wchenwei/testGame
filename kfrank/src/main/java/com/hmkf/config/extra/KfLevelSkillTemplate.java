package com.hmkf.config.extra;

import java.util.List;

import com.hm.libcore.annotation.FileConfig;
import com.hm.model.tankLimitAdapter.TankLimitAdapterGroup;
import com.hmkf.config.template.KfPkSkillTemplate;

@FileConfig("kf_pk_skill")
public class KfLevelSkillTemplate extends KfPkSkillTemplate {
    private TankLimitAdapterGroup tankLimitGroup;

    public void init() {
        this.tankLimitGroup = new TankLimitAdapterGroup(getTank_limit());
    }


    public List<Integer> getLuckTankId(List<Integer> tankId) {
        return this.tankLimitGroup.getAnyMatchFilerTankId(tankId);
    }
}
