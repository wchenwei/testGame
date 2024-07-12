package com.hm.config.excel.temlate;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.util.string.StringUtil;

import java.util.List;

/**
 * Description:
 * User: yang xb
 * Date: 2018-06-30
 */
@FileConfig("capital_fight_castle")
public class CapitalFightCastleTemplateImpl extends CapitalFightCastleTemplate {
    private List<Integer> npcList = Lists.newArrayList();

    public void init() {
        npcList = StringUtil.splitStr2IntegerList(getNpc(), ":");
    }

    public List<Integer> getNpcList() {
        return npcList;
    }
}
