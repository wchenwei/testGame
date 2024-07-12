package com.hm.action.player.vo;

import com.hm.model.tank.Tank;

public class TankSimpleVo {
    private int id;
    private int index;

    private int lv; // 等级
    private int reLv; //改造等级(品质)
    private int star; //星级
    private int evolveStar;//进化星级
    private int tankSpecialType;


    public TankSimpleVo(int index, Tank tank) {
        this.index = index;
        this.id = tank.getId();

        this.id = tank.getId();
        this.index = index;
        this.lv = tank.getLv();
        this.reLv = tank.getReLv();
        this.star = tank.getStar();
        this.evolveStar = tank.getEvolveStar();
        this.tankSpecialType = tank.getTankSpecial().getType();
    }
}
