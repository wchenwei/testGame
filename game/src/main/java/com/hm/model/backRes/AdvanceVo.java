package com.hm.model.backRes;

import com.google.common.collect.Lists;
import com.hm.enums.LogType;
import com.hm.model.item.Items;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author xjt
 * @version 1.0
 * @desc 表述
 * @date 2021/12/7 10:27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdvanceVo {
    /**
     * 用以标识 该预扣资源 对应的 id，防止在别处进行 失败资源返还
     */
    private int tagId;
    private LogType logType;
    private List<Items> rewards = Lists.newArrayList();

    public AdvanceVo(LogType logType, List<Items> rewards) {
        this.logType = logType;
        this.rewards = rewards;
    }
}
