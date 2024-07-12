package com.hm.model.task;

import lombok.Data;

@Data
public class Task extends AbstractTask{

    public Task(int taskId) {
        super(taskId);
    }

    public Task() {
    }

}
