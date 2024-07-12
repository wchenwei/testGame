package com.hm.action;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@NoArgsConstructor
@Data
public class WorldPlayer {
    private ArrayList<BadWord> badWords = Lists.newArrayList();
    private ReportCatchInfo reportCatchInfo = new ReportCatchInfo();


    /**
     * 判断发广告
     * @param content
     * @return
     */
    public boolean toBlackHouse(String content,int[] params) {
        BadWord word = new BadWord(content);
        badWords.removeIf(e -> e.isOutTime(word.getLastTime(),params[3]));
        long sameSize = badWords.stream().filter(e -> e.isSameWorld(word,params[2])).count();
        if(sameSize >= params[1]) {
            return true;
        }
        if(badWords.size() > params[0]) {
            this.badWords.remove(0);
        }
        this.badWords.add(word);
        return false;
    }
}
