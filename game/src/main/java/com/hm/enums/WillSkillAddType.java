package com.hm.enums;

/**
 * 战意技能恢复战意类型
 */
public enum WillSkillAddType {
    /**
     * 1，每次普攻恢复。1:9
     * 2，每次受到普攻恢复。2:9
     * 3，每10帧恢复。3:9
     * 4，每掉1%血量恢复。4:9
     */
    NormalAtk(1, "每次普攻恢复"),
    BeNormalAtk(2, "每次受到普攻恢复"),
    Frame(3, "每10帧恢复"),
    HpReduce(4, "每掉1%血量恢复"),

    ;

    private WillSkillAddType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    private int type;

    private String desc;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
