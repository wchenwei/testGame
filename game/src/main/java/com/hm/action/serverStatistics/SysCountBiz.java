/**  
 * Project Name:SLG_GameHot.
 * File Name:SysCountBiz.java  
 * Package Name:com.hm.action.serverStatistics  
 * Date:2018年4月8日下午2:37:00  
 * Copyright (c) 2018, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
 */


package com.hm.action.serverStatistics;

import com.hm.libcore.annotation.Biz;
import com.hm.action.notice.NoticeBiz;
import com.hm.config.excel.LanguageCnTemplateConfig;
import com.hm.model.player.Player;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;

import javax.annotation.Resource;

/**  
 * ClassName: SysCountBiz. <br/>  
 * Function: 监控一些系统信息变化，并更新. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2018年4月8日 下午2:37:00 <br/>  
 *  
 * @author zxj  
 * @version   
 */
@Biz
public class SysCountBiz implements IObserver{
	@Resource
	private LanguageCnTemplateConfig langeConfig;
	@Resource
	private NoticeBiz noticeBiz;
	
	/**  
	 * TODO 简单描述该方法的实现功能（可选）.  
	 * @see IObserver#invoke(ObservableEnum, Player, Object[])
	 */
	@Override
	public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
		
	}
	
	
	/**  
	 * TODO 简单描述该方法的实现功能（可选）.  
	 * @see IObserver#registObserverEnum()
	 */
	@Override
	public void registObserverEnum() {
	}
}











