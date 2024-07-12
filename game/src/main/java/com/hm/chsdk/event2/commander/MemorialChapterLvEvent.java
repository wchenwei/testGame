package com.hm.chsdk.event2.commander;

import cn.hutool.core.convert.Convert;
import com.hm.model.player.Player;

/**
 * @ClassName MemorialChapterLvEvent
 * @Deacription 纪念馆章节升级
 * @Author zxj
 * @Date 2022/3/11 17:45
 * @Version 1.0
 **/
//@EventMsg(obserEnum = ObservableEnum.CHMemorialChapterLv)
public class MemorialChapterLvEvent extends CommanderEvent{
    @Override
    public void initCommanderData(Player player, Object... argv) {
        //chapterId, curLv, newLv, memorialHallChapter.getTotalPohoto()
        event_id = "9023";
        event_name = "指挥官纪念馆展厅升级";
        this.chapterId = Convert.toInt(argv[0]);
        this.Level_Before = Convert.toInt(argv[1]);
        this.Level_After = Convert.toInt(argv[2]);
        this.count = Convert.toInt(argv[3]);
    }
    private int chapterId;
    private int count;
    private int Level_Before;
    private int Level_After;

}
