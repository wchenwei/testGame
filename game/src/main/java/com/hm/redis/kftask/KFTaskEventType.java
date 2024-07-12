package com.hm.redis.kftask;

import java.util.Arrays;

/**
 * 跨服任务事件类型
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/4/9 10:51
 */
public enum KFTaskEventType {
    Event1(1, "资源战:占矿时间XX分钟"),
    Event2(2, "资源战:获得积分XXX"),
    Event3(3, "资源战:跟同服队友占矿XXX分钟"),
    Event4(4, "资源战:支援队友XXX分钟"),
    Event5(5, "资源战:抢夺矿的xx次"),
    Event6(6, "城战:参与占城XXX"),
    Event7(7, "城战:杀敌（玩家、镜像）总数XXX"),
    Event8(8, "城战:击杀npcxxx"),
    Event9(9, "城战:突击次数"),
    Event10(10, "城战:偷袭次数"),
    Event11(11, "城战:撞城xxx次"),
    Event12(12, "城战:部队死亡"),
    Event13(13, "城战:clone部队死亡"),
    Event14(14, "城战:clone部队"),
    Event15(15, "领地战:购买buff"),

    Event16(16, "王者峡谷：参与占领能源城数量"),
    Event17(17, "王者峡谷：抽取战术次数"),
    Event18(18, "王者峡谷：鼓舞次数"),
    Event19(19, "王者峡谷：观看获得经验"),

    Event20(20, "跨服极地乱斗：击杀核晶运输队数量"),
    Event21(21, "跨服极地乱斗：击杀机场守卫数量"),
    Event22(22, "跨服极地乱斗：获得空降师战术"),
    Event23(23, "跨服极地乱斗：强化xx战车到魔改"),
    Event24(24, "跨服极地乱斗：参与占领核晶矿数量"),


    Event25(25, "跨服积分：参与占领积分城市次数"),
    Event26(26, "跨服积分：在积分城市累计杀敌"),
    Event27(27, "跨服积分：在积分城市累计死亡"),
    Event28(28, "跨服远征：在宝箱城市累计杀敌"),
    Event29(29, "跨服远征：在宝箱城市累计死亡"),

    Event999(999, "领地战:占领领地"),
    ;

    private KFTaskEventType(int type, String desc) {
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

    public static KFTaskEventType getKFTaskEventType(int type) {
        return Arrays.stream(KFTaskEventType.values()).filter(e -> e.getType() == type).findFirst().orElse(null);
    }
}
