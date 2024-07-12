package com.hm.model.task;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 主线任务
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/8/11 16:27
 */
@NoArgsConstructor
@Data
public class MainTask extends AbstractTask {
    /**
     * 任务开始时用户在线时长
     */
    private long startSecond;
    public MainTask(int id) {
        super(id);
        setUnlock(true);
    }
}
