package com.hm.redis.kftask;

import com.hm.libcore.util.TimeUtils;
import com.hm.enums.ActivityType;
import com.hm.libcore.mongodb.MongoUtils;

import java.util.Arrays;
import java.util.Date;

/**
 * 跨服任务类型
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/4/9 10:40
 */
public enum KFTaskType {
    KfMine(1, "跨服资源战") {
        @Override
        public BaseKFTask create(Number playerId) {
            return new KFTaskMine(playerId);
        }

        @Override
        public ActivityType toActivityType() {
            return null;
        }

        @Override
        public Class<KFTaskMine> toKFTaskClass() {
            return KFTaskMine.class;
        }

        @Override
        public String buildKeyName() {
            String mark = TimeUtils.formatSimpeTime2(new Date());
            return super.buildKeyName() + mark;
        }
    },
    KfYz(2, "跨服远征") {
        @Override
        public BaseKFTask create(Number playerId) {
            return new KFTaskYz(playerId);
        }

        @Override
        public ActivityType toActivityType() {
            return ActivityType.KfExpeditionActivity;
        }

        @Override
        public Class<KFTaskYz> toKFTaskClass() {
            return KFTaskYz.class;
        }
    },
    KfWz(4, "跨服王者") {
        @Override
        public BaseKFTask create(Number playerId) {
            return new KFTaskWz(playerId);
        }

        @Override
        public ActivityType toActivityType() {
            return ActivityType.KFKingsCanyon;
        }

        @Override
        public Class<KFTaskWz> toKFTaskClass() {
            return KFTaskWz.class;
        }
    },
    KfJdld(5, "跨服极地乱斗") {
        @Override
        public BaseKFTask create(Number playerId) {
            return new KFTaskJdld(playerId);
        }

        @Override
        public ActivityType toActivityType() {
            return ActivityType.KFScuffle;
        }

        @Override
        public Class<KFTaskJdld> toKFTaskClass() {
            return KFTaskJdld.class;
        }
    },
    KfScore(6, "跨服积分") {
        @Override
        public BaseKFTask create(Number playerId) {
            return new KFTaskScore(playerId);
        }

        @Override
        public ActivityType toActivityType() {
            return ActivityType.KFScoreWar;
        }

        @Override
        public Class<KFTaskScore> toKFTaskClass() {
            return KFTaskScore.class;
        }
    },
    ;

    private KFTaskType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public abstract BaseKFTask create(Number playerId);

    public abstract ActivityType toActivityType();

    public abstract <T extends BaseKFTask> Class<T> toKFTaskClass();

    public String buildKeyName() {
        return MongoUtils.getGameDBName() + "_kftask" + getType();
    }

    private int type;
    private String desc;

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static KFTaskType getKFTaskType(int type) {
        return Arrays.stream(KFTaskType.values()).filter(e -> e.getType() == type).findFirst().orElse(null);
    }
}
