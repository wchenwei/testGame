package com.hm.model.task.daily;

import com.hm.model.task.AbstractTask;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * User: yang xb
 * Date: 2018-08-08
 */
@NoArgsConstructor
@Data
public class DailyTask extends AbstractTask {
    public DailyTask(int id) {
        super(id);
    }

    public void reset(){
    }
}
