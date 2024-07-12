package com.hm.config.excel.templaextra;

import com.hm.config.excel.temlate.StarUnlockTemplate;
import com.hm.libcore.annotation.FileConfig;
import lombok.Getter;

import java.util.List;

@Getter
@FileConfig("star_unlock")
public class StarUnlockTemplateImpl extends StarUnlockTemplate {
    private int maxPosition;

    public void init(List<StarUnlockTemplateImpl> templateList){
        this.maxPosition = templateList.stream().filter(e -> e.getSkill_unlock() > 0)
                .filter(e -> e.getStar() <= getStar())
                .mapToInt(StarUnlockTemplate::getSkill_unlock).max().orElse(0);
    }
}
