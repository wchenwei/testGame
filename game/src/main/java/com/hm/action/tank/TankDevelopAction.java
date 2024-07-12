 /**  
 * Project Name:SLG_GameFuture.
 * File Name:MailAction.java  
 * Package Name:com.hm.action.mail  
 * Date:2017年12月18日下午3:50:23  
 * Copyright (c) 2017, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
 */  
  
package com.hm.action.tank;

 import com.hm.action.AbstractPlayerAction;
 import com.hm.action.item.ItemBiz;
 import com.hm.action.player.PlayerBiz;
 import com.hm.action.tank.biz.FactoryBiz;
 import com.hm.action.tank.biz.ResearchBiz;
 import com.hm.action.tank.biz.TankBiz;
 import com.hm.config.excel.CommValueConfig;
 import com.hm.config.excel.ItemConfig;
 import com.hm.config.excel.TankConfig;
 import com.hm.config.excel.TankFettersConfig;
 import com.hm.libcore.annotation.Action;

 import javax.annotation.Resource;

/**  
 * desc:坦克养成 
 * date: 2020年1月7日09:41:17
 *  
 * @author xjt  
 * @version   
 */
@Action
public class TankDevelopAction extends AbstractPlayerAction {
	@Resource
	private TankBiz tankBiz;
	@Resource
	private ItemConfig itemConfig; 
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private PlayerBiz playerBiz;
	@Resource
	private TankConfig tankConfig;
	@Resource
	private ResearchBiz researchBiz;
	@Resource
	private FactoryBiz factoryBiz;
	@Resource
    private CommValueConfig commValueConfig;
	@Resource
	private TankFettersConfig tankFettersConfig;
	
	
	
}
  







