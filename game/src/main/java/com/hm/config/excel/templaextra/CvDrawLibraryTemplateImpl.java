package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.CvDrawLibraryTemplate;
import com.hm.model.item.Items;
import com.hm.model.weight.Drop;

/**
 * @description:
 * @author: chenwei
 * @create: 2020-12-15 11:47
 **/
@FileConfig("cv_draw_library")
public class CvDrawLibraryTemplateImpl extends CvDrawLibraryTemplate {
	private Drop drops;
    public void init(){
    	this.drops = new Drop(this.getDrop());
    }
    
    public Items random() {
    	return drops.randomItem();
    } 
    
    public boolean isFit(int libId,int lv) {
    	return this.getLibrary_id()==libId&&lv>=this.getPlayer_lv_down()&&lv<=this.getPlayer_lv_up();
    }
}
