package com.hm.action;

import cn.hutool.core.util.StrUtil;
import com.hm.config.GameConstants;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Locale;

@Data
@NoArgsConstructor
public class BadWord {
    private String content;
    private int count;
    private long lastTime;

    public BadWord(String content) {
        this.content = content.toLowerCase(Locale.ROOT);
        this.lastTime = System.currentTimeMillis();
        this.count = 1;
    }

    public void addCount() {
        this.count++;
        this.lastTime = System.currentTimeMillis();
    }

    public boolean isOutTime(long time,int hour) {
        return time - this.lastTime > hour*GameConstants.HOUR;
    }

    public boolean isSameWorld(BadWord word,int v) {
        return StrUtil.similar(word.getContent(),this.content)*100 >= v;
    }
}
