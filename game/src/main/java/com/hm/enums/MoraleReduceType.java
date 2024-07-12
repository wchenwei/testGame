package com.hm.enums;

/**
 * 士气掉落类型
 *
 * @author 司云龙
 * @Version 1.0.0
 * @date 2022/8/5 16:09
 */
public enum MoraleReduceType {
    AllYes(0, "双方都掉落") {
        @Override
        public boolean isCanReduce(boolean isSameCamp) {
            return true;
        }
    },
    AtkReduce(1, "攻击方掉") {
        @Override
        public boolean isCanReduce(boolean isSameCamp) {
            return !isSameCamp;
        }
    },
    DefReduce(2, "防守方掉") {
        @Override
        public boolean isCanReduce(boolean isSameCamp) {
            return isSameCamp;
        }
    },
    AllNo(3, "双方不掉") {
        @Override
        public boolean isCanReduce(boolean isSameCamp) {
            return false;
        }
    },

    ;

    /**
     * @param type
     * @param desc
     */
    private MoraleReduceType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    private int type;
    private String desc;

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public abstract boolean isCanReduce(boolean isAtk);

    public static MoraleReduceType getType(int type) {
        for (MoraleReduceType weaponType : MoraleReduceType.values()) {
            if (type == weaponType.getType()) {
                return weaponType;
            }
        }
        return null;
    }
}
