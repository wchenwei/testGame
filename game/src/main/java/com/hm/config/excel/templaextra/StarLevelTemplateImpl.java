package com.hm.config.excel.templaextra;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.hm.config.excel.temlate.StarLevelTemplate;
import com.hm.enums.TankAttrType;
import com.hm.libcore.annotation.ConfigInit;
import com.hm.libcore.annotation.FileConfig;
import com.hm.util.TankAttrUtils;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@FileConfig("star_level")
public class StarLevelTemplateImpl extends StarLevelTemplate {
    private Map<TankAttrType, Double> noteAttrMap = Maps.newHashMap();
    private Map<TankAttrType, Double> totalAttrMap = Maps.newHashMap();

    @ConfigInit
    public void init(){
        if (StrUtil.isNotEmpty(getNode_attr())){
            this.noteAttrMap = TankAttrUtils.str2TankAttrMap(getNode_attr(), ",", ":");
        }
    }

    public void calAttr(List<StarLevelTemplateImpl> templateList){
        templateList.stream().filter(e -> e.getStar() <= getStar())
                .filter(e -> {
                    if (e.getStar() > getStar()) {
                        return false;
                    }
                    if (e.getStar() == getStar()){
                        return e.getNode() <= getNode();
                    }
                    return true;
                }).forEach(e -> e.getNoteAttrMap().forEach((tankAttrType, val) -> totalAttrMap.merge(tankAttrType, val, Double::sum)));
    }

    public boolean isStarUp(){
        return getStar_upgrade() > 0;
    }
}
