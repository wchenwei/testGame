package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.util.string.StringUtil;
import com.hm.config.excel.temlate.SeaTradeItemTemplate;

import java.util.List;

/**
 * Description:
 * User: yang xb
 * Date: 2019-01-10
 */
@FileConfig("sea_trade_item")
public class SeaTradeItemTemplateImpl extends SeaTradeItemTemplate {
    private List<Integer> fromCityList = Lists.newArrayList();
    public void init() {
        fromCityList = StringUtil.splitStr2IntegerList(getFrom_city(), ",");
    }

    public List<Integer> getFromCityList() {
        return fromCityList;
    }
}
