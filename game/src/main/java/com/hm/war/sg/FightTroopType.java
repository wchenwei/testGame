package com.hm.war.sg;

/**
 * @Description: 战斗部队类型
 * @author siyunlong  
 * @date 2019年11月12日 下午8:01:04 
 * @version V1.0
 */
public enum FightTroopType {
    NPC(0, "NPC"),
    Player(1, "真人玩家"),
    ClonePlayer(2, "玩家clone"),
    ;

    private int type;
    private String desc;

    FightTroopType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
    
}
