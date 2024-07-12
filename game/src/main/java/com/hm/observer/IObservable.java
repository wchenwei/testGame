/**  
 * Project Name:SLG_Game.  
 * File Name:IObservable.java  
 * Package Name:com.hm.observer  
 * Date:2017年9月19日上午10:17:28  
 * Copyright (c) 2017, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
*/  
  
package com.hm.observer;

import com.hm.model.player.Player;

/**  
 * 被观察者接口
 * ClassName:IObservable <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason:   TODO ADD REASON. <br/>  
 * Date:     2017年9月19日 上午10:17:28 <br/>  
 * @author   zigm  
 * @version  1.1  
 * @since    
 */
public interface IObservable {
	
	/**
	 * 发出改变信号
	 * notifyChanges:(这里用一句话描述这个方法的作用). <br/>  
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>  
	 *  
	 * @author zigm  
	 * @param observableEnum
	 * @param user
	 * @param argv  使用说明
	 *
	 */
	 void notifyChanges(ObservableEnum observableEnum, Player player, Object... argv);
}
  
