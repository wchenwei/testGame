package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.SeaTradeCityTemplate;
import com.hm.util.StringUtil;

import java.util.List;

/**
 * Description:
 * User: yang xb
 * Date: 2019-01-10
 */
@FileConfig("sea_trade_city")
public class SeaTradeCityTemplateImpl extends SeaTradeCityTemplate {
    private List<Integer> linkCityList = Lists.newArrayList();
    private List<Integer> sellItemList = Lists.newArrayList();

    public void init() {
        linkCityList = StringUtil.splitStr2IntegerList(getLink_city(), ",");
        sellItemList = StringUtil.splitStr2IntegerList(getSell_item(), ",");
    }

    public List<Integer> getLinkCityList() {
        return linkCityList;
    }

    public List<Integer> getSellItemList() {
        return sellItemList;
    }
}
