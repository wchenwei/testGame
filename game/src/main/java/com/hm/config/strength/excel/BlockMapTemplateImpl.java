package com.hm.config.strength.excel;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.util.string.StringUtil;
import com.hm.enums.TankAttrType;
import com.hm.model.tank.TankAttr;
import com.hm.util.TankAttrUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@FileConfig("block_map")
@Data
public class BlockMapTemplateImpl extends BlockMapTemplate{
    // 空白格子
    private List<Integer> blockGride = Lists.newArrayList();

    private List<StarAttr> attrList = Lists.newArrayList();

    public void init() {
        this.blockGride = StringUtil.splitStr2IntegerList(this.getGride(), ":");
        this.attrList = StringUtil.splitStr2StrList(this.getRing_attri(), "#").stream().map(str -> {
            List<String> list = StringUtil.splitStr2StrList(str, "_");
            if(list.size() != 2){
                return null;
            }
            return new StarAttr(Convert.toInt(list.get(0)), list.get(1));
        }).filter(Objects::nonNull).collect(Collectors.toList());

    }

    public TankAttr getTankAttrByStar(int star){
        TankAttr tankAttr = new TankAttr();
        attrList.stream().filter(e->e.getStar() <= star).forEach(starAttr -> {
            tankAttr.addAttr(starAttr.getAttrMap());
        });
        return tankAttr;
    }

    public boolean checkAllUsed(List<Integer> gridList){
        return CollUtil.containsAll(gridList, blockGride);
    }

    public boolean checkBlank(List<Integer> gridList){
        return CollUtil.containsAll(blockGride, gridList);
    }

    @Data
    @NoArgsConstructor
    class StarAttr{
        private int star;
        private Map<TankAttrType, Double> attrMap;

        public StarAttr(int star, String attr){
            this.star = star;
            this.attrMap = TankAttrUtils.str2TankAttrMap(attr, ",", ":");
        }
    }
}
