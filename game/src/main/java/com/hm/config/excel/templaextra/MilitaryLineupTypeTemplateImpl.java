package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.util.string.StringUtil;
import com.hm.config.excel.temlate.MilitaryLineupTypeTemplate;

import java.util.List;

/**
 * Description:
 * User: yang xb
 * Date: 2019-09-03
 */
@FileConfig("military_lineup_type")
public class MilitaryLineupTypeTemplateImpl extends MilitaryLineupTypeTemplate {
    private List<Integer> positions = Lists.newArrayList();

    public void init() {
        positions = StringUtil.splitStr2IntegerList(getFormation(), ",");
    }

    public List<Integer> getPositions() {
        return positions;
    }
}
