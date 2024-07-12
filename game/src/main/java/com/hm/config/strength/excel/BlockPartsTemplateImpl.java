package com.hm.config.strength.excel;

import com.hm.libcore.annotation.FileConfig;

import java.util.Objects;

@FileConfig("block_parts")
public class BlockPartsTemplateImpl extends BlockPartsTemplate{

    public boolean checkStarAndGrid(BlockPartsTemplateImpl template){
        if(template == null){
            return false;
        }
        if(!Objects.equals(this.getGird(), template.getGird())){
            return false;
        }
        return Objects.equals(this.getStar(), template.getStar());
    }
}
