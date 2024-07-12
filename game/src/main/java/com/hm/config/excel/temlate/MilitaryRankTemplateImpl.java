/**  
 * Project Name:SLG_GameHot.
 * File Name:MilitaryRankTemplateImpl.java  
 * Package Name:com.hm.config.excel.temlate  
 * Date:2018年4月12日下午6:35:21  
 * Copyright (c) 2018, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
 */


package com.hm.config.excel.temlate;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**  
 * ClassName: MilitaryRankTemplateImpl. <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2018年4月12日 下午6:35:21 <br/>  
 *  
 * @author zxj  
 * @version   
 */
@FileConfig("military_rank")
public class MilitaryRankTemplateImpl extends MilitaryRankTemplate{
	private List<Items> listItems = Lists.newArrayList();
	
	public void init() {
		listItems = ItemUtils.str2ItemList(this.getExp(), ",", ":");
	}
	
	public List<Items> getListItems() {
		return listItems;
	}
	public void setListItems(List<Items> listItems) {
		this.listItems = listItems;
	}

}



