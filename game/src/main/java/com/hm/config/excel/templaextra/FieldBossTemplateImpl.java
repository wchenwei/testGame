package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.config.excel.temlate.FieldBossBaseTemplate;
import com.hm.libcore.annotation.ConfigInit;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.util.string.StringUtil;
import lombok.Getter;

import java.util.List;


@FileConfig("field_boss_base")
public class FieldBossTemplateImpl extends FieldBossBaseTemplate {
    @Getter
    private List<Integer> openDays = Lists.newArrayList();

    @ConfigInit
    public void init(){
        this.openDays = StringUtil.splitStr2IntegerList(getOpen_day(),",");
    }


}
