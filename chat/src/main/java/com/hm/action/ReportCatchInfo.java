package com.hm.action;

import com.hm.config.GameConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xjt
 * @version 1.0
 * @desc 举报关键词捕获信息
 * @date 2022/3/25 9:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportCatchInfo {
    private int count;//捕获次数
    private long lastTime;//上次捕获时间


    public void doReportInfo() {
        if (lastTime > 0 && System.currentTimeMillis() - lastTime > 5 * GameConstants.MINUTE) {
            this.count = 0;
            this.lastTime = 0;
            return;
        }
        this.count++;
        this.lastTime = System.currentTimeMillis();
    }
}
