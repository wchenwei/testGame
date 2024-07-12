package com.hm.enums;

public enum BossRewardType {
	//1、随机宝箱；2、关键伤害宝箱；3、杀死boss宝箱
	Random(1, "随机宝箱"),
    KeyPoints(2, "关键伤害宝箱"),
    KillBoss(3, "杀死boss宝箱"),
    
    ;

    private int type;
    private String desc;

    BossRewardType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

	public int getType() {
		return type;
	}
}
