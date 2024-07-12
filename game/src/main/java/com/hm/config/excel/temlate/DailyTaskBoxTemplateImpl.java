package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * Description:
 * User: yang xb
 * Date: 2018-08-10
 */
@FileConfig("daily_task_box")
public class DailyTaskBoxTemplateImpl extends DailyTaskBoxTemplate {
    private List<Items> boxList;
    public void init() {
        boxList = ItemUtils.str2ItemList(this.getBox_id(),",", ":");
    }

    public List<Items> getBoxList() {
        return boxList;
    }
}
