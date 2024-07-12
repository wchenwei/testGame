package com.hm.chsdk.event2.first_login;

import com.hm.model.tank.ElementSkill;
import com.hm.model.tank.Tank;

public class CHSSTank {
    public int id;//id
    public int lv;//等级
    public int dlv;//车长等级
    public int tlv;//突破等级
    public int jlv;//进化等级
    public int zlv;//专精等级
    public int mlv;//魔改等级
    public int clv;//中控等级


    public CHSSTank(Tank tank) {
        this.id = tank.getId();
        this.lv = tank.getLv();
        if(tank.getDriver() != null) {
            this.dlv = tank.getDriver().getLv();
        }
        this.tlv = tank.getReLv();
        this.jlv = tank.getEvolveStar();
        this.zlv = tank.getTankSpecial().getLv();
        this.mlv = tank.getTankMagicReform().getReformLv();
        ElementSkill bigSkill = tank.getTankControl().getBigSkill();
        if(bigSkill != null) {
            this.clv = bigSkill.getLv();
        }
    }
}
