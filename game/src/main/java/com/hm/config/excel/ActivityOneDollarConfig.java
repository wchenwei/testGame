package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.templaextra.Active1yuanTemplateImpl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author xb
 */
@Config
public class ActivityOneDollarConfig extends ExcleConfig {
    /**
     * Active1yuanTemplateImpl::getDay --- Active1yuanTemplateImpl::getGround -- object
     */
    private Table<Integer, Integer, Active1yuanTemplateImpl> table = HashBasedTable.create();

    public Active1yuanTemplateImpl getCfg(int day, int stage) {
        // 天数也找不到了呢
        // 天数循环
        int dayMax = table.rowKeySet().stream().max(Integer::compare).orElse(0);
        if (dayMax <= 0) {
            return null;
        }

        // 修正一下天数
        int d = (day - 1) % dayMax + 1;

        if (table.contains(d, stage)) {
            return table.get(d, stage);
        }

        // 期数在 activie_1yuan里找不到咋办？
        // 读第1期 最小那一期
        if (!table.containsRow(d)) {
            return null;
        }
        Map<Integer, Active1yuanTemplateImpl> row = table.row(d);
        Optional<Integer> min = row.keySet().stream().min(Integer::compare);
        if (min.isPresent()) {
            return row.get(min.get());
        }

        return null;
    }

    @Override
    public void loadConfig() {
        /*List<Active1yuanTemplateImpl> list = JSONUtil.fromJson(getJson(Active1yuanTemplateImpl.class), new TypeReference<List<Active1yuanTemplateImpl>>() {
        });


        Table<Integer, Integer, Active1yuanTemplateImpl> t = HashBasedTable.create();
        for (Active1yuanTemplateImpl template : list) {
            template.init();
            t.put(template.getDay(), template.getGround(), template);
        }

        table = ImmutableTable.copyOf(t);*/
    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(Active1yuanTemplateImpl.class);
    }
}
