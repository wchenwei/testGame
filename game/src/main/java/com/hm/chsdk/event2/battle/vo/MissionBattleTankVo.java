package com.hm.chsdk.event2.battle.vo;

import com.hm.model.tank.Tank;
import lombok.Setter;

import java.util.Map;

/**
 * @ClassName MissionBattleTankVo
 * @Deacription 主线关卡上阵的坦克信息
 * @Author zxj
 * @Date 2023-02-27 17:49
 * @Version 1.0
 **/
@Setter
public class MissionBattleTankVo {
    //坦克id、等级、星极、     车长等级、  进化等级、专精等级,   专精类型、 羁绊星级、 改造等级(品质)、 魔改等级、 奇兵等级
    private int tankid, lv, startLv, driverLv, evolveStar, specialLv, specialType, fetters, reLv, reformLv, soldierLv;
    //坦克的科技(当前科技的位置，当前科技的等级)
    private Map<Integer, Integer> techData;
    //车长军职、
    private Map<Integer, Integer> driverAdvance;

    public MissionBattleTankVo() {
    }

    public MissionBattleTankVo(Tank tank) {
        this.fetters = tank.getFetters();
        this.reLv = tank.getReLv();
        this.reformLv = tank.getTankMagicReform().getReformLv();
        this.soldierLv = null == tank.getTankSoldier() ? 0 : tank.getTankSoldier().getLv();
        this.techData = tank.getTankTech().getTechData();

        this.tankid = tank.getId();
        this.lv = tank.getLv();
        this.startLv = tank.getStar();
        this.driverLv = null == tank.getDriver() ? 0 : tank.getDriver().getLv();
        this.driverAdvance = null == tank.getDriver() ? null : tank.getDriver().getAdvance();
        this.evolveStar = tank.getEvolveStar();
        this.specialLv = tank.getTankSpecial().getLv();
        this.specialType = tank.getTankSpecial().getType();

    }
}














