package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.MissionEventBuffGroupTemplate;
import com.hm.util.StringUtil;

import java.util.Arrays;

@FileConfig("mission_event_buff_group")
public class LevelEventBuffGroupTemplate extends MissionEventBuffGroupTemplate {
    private int[] events = new int[]{0, 0, 0};

    public void init() {
        this.events = StringUtil.strToIntArray(this.getLibrary(), ",");
    }

    public int[] getEvents() {
        return Arrays.stream(events).toArray();
    }


    public static void main(String[] args) {
        int[] events = new int[]{0, 0, 0};

        int[] e = Arrays.stream(events).toArray();
        e[0] = 1;
        System.out.println("e = " + e);
    }
}
