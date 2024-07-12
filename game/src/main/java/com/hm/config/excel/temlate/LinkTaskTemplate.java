package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("link_task")
public class LinkTaskTemplate {
    private int id;
    private int task_type;
    private String task_finish;
    private int finish_num;
    private String task_reward;
    private int pre_task;
    private int order;
}
