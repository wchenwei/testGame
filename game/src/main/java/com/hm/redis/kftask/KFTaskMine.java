package com.hm.redis.kftask;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/4/9 10:40
 */
@Data
@NoArgsConstructor
public class KFTaskMine extends BaseKFTask {
    public KFTaskMine(Number id) {
        super(KFTaskType.KfMine, id);
    }

    public static KFTaskMine addEvent(Number playerId, KFTaskEventType type, long add) {
        KFTaskMine kfTask = KFPlayerTaskCache.getKFPlayerTaskFromCache(KFTaskType.KfMine, playerId);
        kfTask.addEvent(type, add);
        kfTask.saveRedis();
        return kfTask;
    }

    public static KFTaskMine getKFTaskMineFromCache(Number playerId) {
        return KFPlayerTaskCache.getKFPlayerTaskFromCache(KFTaskType.KfMine, playerId);
    }

}
