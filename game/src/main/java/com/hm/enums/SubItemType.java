/**  
 * Project Name:SLG_GameFuture.  
 * File Name:ItemType.java  
 * Package Name:com.hm.enums  
 * Date:2017年12月28日下午4:37:27  
 * Copyright (c) 2017, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
*/  
  
package com.hm.enums;

/**  
 * 道具类型类型枚举
 * ClassName:ItemType <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason:   TODO ADD REASON. <br/>  
 * Date:     2017年12月28日 下午4:37:27 <br/>  
 * @author   zigm  
 * @version  1.1  
 * @since    
 */
public enum SubItemType {
	MATERIAL(60,"材料"),
	PACKAGE(61,"掉落包"),
	CHOICEBOX(62,"选择包"),
	EQUPIECE(63, "装备碎片	集齐一定数量后可以使用获得对应装备"),
	GEMS(64,"宝石	在指挥官装备上镶嵌可提供属性"),
	FUNCTIONITEM(66,"功能道具"),
	AGENTPIECE(67,"特工碎片"),
	DISCOUNT_TICKET(68,"折扣优惠券"),
	DEDUCTION_TICKET(69,"抵扣优惠券"),
	RANDOM_DROP(70,"随机掉落道具"),

	;
	
	private SubItemType(int type,  String desc) {
		this.type = type;
		this.desc = desc;
	}
	
	private final int type;
	
	private final String desc;

	public int getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}
	public static SubItemType getType(int type) {
		for (SubItemType buildType : SubItemType.values()) {
			if(buildType.getType() == type) {
				return buildType;
			}
		}
		return null;
	}
	
}
  
