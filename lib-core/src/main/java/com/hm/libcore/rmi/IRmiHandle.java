/**
 * 
 */
package com.hm.libcore.rmi;


/**
 * Title: RmiInterface.java
 * Description:
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 叶亮
 * @date 2014年10月28日 下午5:32:52
 * @version 1.0
 */
public interface IRmiHandle {

	public RmiResponse onRmiRequest(RmiRequest rmiRequest) throws Exception;
	
}
