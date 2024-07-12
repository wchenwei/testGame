package com.hmkf.level;

import lombok.Data;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 服务器组内段存储
 * @date 2019年12月3日 下午2:31:51
 */
@Data
public class LevelWorldGroup {
    private int gameTypeId;
    private GameTypeGroup gameTypeGroup;


    public LevelWorldGroup(GameTypeGroup gameTypeGroup) {
        this.gameTypeGroup = gameTypeGroup;
        this.gameTypeId = gameTypeGroup.getId();
    }


    public void doWeekClear() {
        this.gameTypeGroup.weekClear();
    }
}
