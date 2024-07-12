package com.hm.action.guild.biz;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.CommValueConfig;
import com.hm.enums.CommonValueType;

import java.util.List;

public class GuildFlagManger {

    private static List<String> flagList = Lists.newArrayList();

    public static void init(){
        List<String> flags = Lists.newArrayList();
        int[] values = SpringUtil.getBean(CommValueConfig.class).getCommonValueByInts(CommonValueType.GuildFlag);
        for (int i = 1; i <= values[0]; i++) {
            for (int j = 1; j <= values[1]; j++) {
                for (int k = 1; k <= values[2]; k++) {
                    for (int l = 1; l <= values[3]; l++) {
                        flags.add(i +"_"+j +"_"+k +"_"+l);
                    }
                }
            }
        }
        flagList = ImmutableList.copyOf(flags);
    }

    public static List<String> getFlagList() {
        return flagList;
    }
}
