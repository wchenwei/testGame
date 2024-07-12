package com.hm.model.task;

import com.hm.model.task.daily.DailyTask;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class ActivityTask extends DailyTask {
    @Getter
    @Setter
    private String activityId;//任务唯一id


    public ActivityTask(int taskId) {
        super(taskId);
    }


    public void reset() {

    }
}
