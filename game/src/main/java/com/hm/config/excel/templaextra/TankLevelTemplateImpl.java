package com.hm.config.excel.templaextra;

import com.hm.config.excel.temlate.TankLevelTemplate;
import com.hm.libcore.annotation.FileConfig;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@FileConfig("tank_level")
public class TankLevelTemplateImpl extends TankLevelTemplate {
    private Integer needReLv;

    public void init(List<TankLevelTemplateImpl> templateList){
        List<TankLevelTemplateImpl> collect = templateList.stream()
                .filter(e -> e.getLevel() >= getLevel()).collect(Collectors.toList());
        this.needReLv =
                    collect.stream().mapToInt(TankLevelTemplate::getReform_level_unclock)
                    .filter(e -> e > 0)
                    .min()
                    .orElse(1) - 1;

    }
}
