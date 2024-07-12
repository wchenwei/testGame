/**
 * 
 */
package com.hm.model;

import java.io.Serializable;


/**
 * Title: HasObj2Ser.java
 * Description:
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 叶亮
 * @date 2015年7月1日 上午9:37:31
 * @version 1.0
 */
public interface HasObj2Ser extends Serializable{
	
	public SerObject getSerObject();
	
	public void setSerObject(SerObject serObj);
	
	public byte[] getSerObjectArr();
	
	public void setSerObjectArr(byte[] serObjectArr);
}
