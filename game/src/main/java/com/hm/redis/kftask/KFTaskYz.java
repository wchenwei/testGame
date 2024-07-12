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
public class KFTaskYz extends BaseKFTask {
    public KFTaskYz(Number id) {
        super(KFTaskType.KfYz, id);
    }
}
