/**  
 * Project Name:SLG_Game.  
 * File Name:IObserver.java  
 * Package Name:com.hm.observer  
 * Date:2017年9月19日上午10:14:31  
 * Copyright (c) 2017, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
*/  
  
package com.hm.observer;

import com.hm.model.player.Player;

/**  
 * 观察者接口
 * ClassName:IObserver <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason:   TODO ADD REASON. <br/>  
 * Date:     2017年9月19日 上午10:14:31 <br/>  
 * @author   zigm  
 * @version  1.1  
 * @since    
 */
public interface IObserver {

	/**
	 * 
	 * 注册本观察者感兴趣的被观察者信号
	 *  
	 * @author zigm    使用说明
	 */
    void registObserverEnum();

    /**
     * 
     * 在被观察者发出改变信号后的处理
     * 
     * @author zigm  
     * @param observableEnum
     * @param player
     * @param argv  使用说明
     *
     */
    void invoke(ObservableEnum observableEnum, Player player, Object... argv);

	default void registObserver(ObservableEnum... args) {
		for (ObservableEnum observableEnum : args) {
			ObserverRouter.getInstance().registObserver(observableEnum, this);
		}
	}
}
  
