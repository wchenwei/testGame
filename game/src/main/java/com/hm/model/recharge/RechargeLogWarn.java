/**  
 * Project Name:SLG_GameHot.
 * File Name:RechargeLogWarn.java  
 * Package Name:com.hm.model.recharge  
 * Date:2018年6月13日下午3:34:35  
 * Copyright (c) 2018, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
 */


package com.hm.model.recharge;

import com.hm.model.item.Items;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RechargeLogWarn {
	private int id;
	private int rechargeGiftId;
	private List<Items> items;
}
