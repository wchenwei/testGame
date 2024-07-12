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
public class KFTaskWz extends BaseKFTask {
    public KFTaskWz(Number id) {
        super(KFTaskType.KfWz, id);
    }
}
