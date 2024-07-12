package com.hm.config.excel.temlate;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.util.string.StringUtil;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * User: yang xb
 * Date: 2018-04-27
 */
@FileConfig("materials_config")
public class MaterialsConfigTemplateImpl extends MaterialsConfigTemplate {
    private List<Integer> product_id_list = Lists.newArrayList();
    private Map<Integer, Integer> num_lv1 = Maps.newHashMap();
    private Map<Integer, Integer> num_lv2 = Maps.newHashMap();
    private Map<Integer, Integer> num_lv3 = Maps.newHashMap();
    private Map<Integer, Integer> num_lv4 = Maps.newHashMap();

    public List<Integer> getProduct_id_list() {
        return product_id_list;
    }

    public Map<Integer, Integer> getNum_lv1() {
        return num_lv1;
    }

    public Map<Integer, Integer> getNum_lv2() {
        return num_lv2;
    }

    public Map<Integer, Integer> getNum_lv3() {
        return num_lv3;
    }

    public Map<Integer, Integer> getNum_lv4() {
        return num_lv4;
    }

    public void init() {
        product_id_list = StringUtil.splitStr2IntegerList(getProduct_list(), ":");

        buildNumMap(getNum_random1(), num_lv1);

        buildNumMap(getNum_random2(), num_lv2);

        buildNumMap(getNum_random3(), num_lv3);

        buildNumMap(getNum_random4(), num_lv4);
    }

    private void buildNumMap(String str, Map<Integer, Integer> map) {
        for (String s : StringUtil.splitStr2StrList(str, ",")) {
            List<Integer> l = StringUtil.splitStr2IntegerList(s, ":");
            if (l.size() == 2) {
                map.put(l.get(0), l.get(1));
            }
        }
    }
}
