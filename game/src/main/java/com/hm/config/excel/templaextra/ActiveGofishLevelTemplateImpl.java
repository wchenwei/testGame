package com.hm.config.excel.templaextra;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveGofishLevelTemplate;
import com.hm.util.StringUtil;

import java.util.List;

/**
 * @introduce: 类注释
 * @author: wyp
 * @DATE: 2023/11/1
 **/
@FileConfig("active_gofish_level")
public class ActiveGofishLevelTemplateImpl extends ActiveGofishLevelTemplate {
    private List<Integer> list = Lists.newArrayList();

    public void init() {
        this.list = StringUtil.splitStr2IntegerList(this.getFish_pond(), ",");
    }

    public boolean canGoFish(int fishpondId){
        return CollUtil.contains(list, fishpondId);
    }
}
