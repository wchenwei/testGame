/**  
 * Project Name:SLG_GameHot.
 * File Name:PushSetConfig.java  
 * Package Name:com.hm.config  
 * Date:2018年6月29日下午6:00:05  
 * Copyright (c) 2018, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
 */


package com.hm.config;

/**  
 * ClassName: PushSetConfig. <br/>  
 * Function: 推送配置开关. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2018年6月29日 下午6:00:05 <br/>  
 *  
 * @author zxj  
 * @version   
 */
public class PushSetConfig {
	//推送开关
	public static final int CLOSE = 0;	//关闭
	public static final int OPEN = 1;  //开启
	
	//推送时间（推送时间段开关）
	public static final int sendStart = 7;
	public static final int sendEnd = 22;
	
	//ios推送用， 图标小红圈的数值
	public static final int badge = 1;//

}
