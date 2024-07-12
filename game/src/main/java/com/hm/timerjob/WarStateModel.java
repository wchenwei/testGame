package com.hm.timerjob;

import com.hm.config.GameConstants;
import lombok.Getter;

@Getter
public class WarStateModel {
    private int state;
    private long endTime;

    public WarStateModel(int state, long endTime) {
        this.state = state;
        this.endTime = endTime;
    }

    public WarStateModel tomorrowState() {
        return new WarStateModel(state,endTime+ GameConstants.DAY);
    }
}
