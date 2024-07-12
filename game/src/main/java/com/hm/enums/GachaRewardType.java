package com.hm.enums;

/**
 * Description:酒馆奖励类型
 * User: yang xb
 * Date: 2018-10-18
 */
public enum GachaRewardType {
    None(0, "error!!!"),
    Normal(1, "普通抽奖"),
    OnceNotGet(2, "高级单抽非必得"),
    OnceGetTank(4, "高级单抽必得"),
    TenNotGet(5, "十连九次非必得"),
    TenGetTank(6, "十连必得"),
    TankRate(7, "SS坦克权重"),
    STankRate(8, "S坦克权重"),
    TenGetPaper(9, "十连必得图纸act_id == 84 用"),
    ;

    GachaRewardType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    private int type;
    private String desc;
}
