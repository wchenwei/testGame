/**  
 * Project Name:SLG_GameFuture.  
 * File Name:PVELogType.java  
 * Package Name:com.hm.enums  
 * Date:2018年1月23日上午11:37:24  
 * Copyright (c) 2018, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
*/  
  
package com.hm.enums;  
/**  
 * ClassName:PVELogType <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason:   TODO ADD REASON. <br/>
 * PVE类型,后续(注:对应原有log服务器type)
 * Date:     2018年1月23日 上午11:37:24 <br/>  
 * @author   zqh  
 * @version  1.1  
 * @since    
 */
public enum PVELogType {
	Monster(2,"匪军");
	
	/**
	 * @param type
	 * @param desc
	 */
	private PVELogType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	private int type;
	
	private String desc;

	public int getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}
}
  
