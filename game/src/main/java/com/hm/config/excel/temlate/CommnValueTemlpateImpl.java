/**  
 * Project Name:SLG_GameHot.
 * File Name:CommnValueImple.java  
 * Package Name:com.hm.config.excel  
 * Date:2018年4月12日下午6:09:17  
 * Copyright (c) 2018, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
 */


package com.hm.config.excel.temlate;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;
import java.util.stream.Collectors;

/**  
 * ClassName: CommnValueImple. <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2018年4月12日 下午6:09:17 <br/>  
 *  
 * @author zxj  
 * @version   
 */
@FileConfig("common_value")
public class CommnValueTemlpateImpl extends CommonValueTemplate{
	private List<Items> listItems = Lists.newArrayList();
	
	public void init() {
		listItems = ItemUtils.str2ItemList(this.getPara(), ",", ":");
	}
	
	public List<Items> getListItems() {
		return listItems.stream().map(e -> e.clone()).collect(Collectors.toList());
	}
}





