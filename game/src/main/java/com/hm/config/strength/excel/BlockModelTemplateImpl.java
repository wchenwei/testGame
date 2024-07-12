package com.hm.config.strength.excel;

import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.util.string.StringUtil;
import lombok.Data;

import java.util.List;
import java.util.Map;

@FileConfig("block_model")
@Data
public class BlockModelTemplateImpl extends BlockModelTemplate{
    // 0、90、180、270
   private Map<Integer,List<Integer>> map = Maps.newConcurrentMap();

    public void init() {
        map.put(0, StringUtil.splitStr2IntegerList(this.getShape(), ":"));
        map.put(90, StringUtil.splitStr2IntegerList(this.getShape90(), ":"));
        map.put(180, StringUtil.splitStr2IntegerList(this.getShape180(), ":"));
        map.put(270, StringUtil.splitStr2IntegerList(this.getShape270(), ":"));
    }

    public List<Integer> getModelShape(int type){
        return map.get(type);
    }
}
