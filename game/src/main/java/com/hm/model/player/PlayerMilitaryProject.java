package com.hm.model.player;

import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.CommanderConfig;

/**
 * ClassName: PlayerMilitaryProject. <br/>  
 * Function: 指挥官--军工系统. <br/>  
 * date: 2019年8月9日 下午1:55:12 <br/>  
 * @author zxj  
 * @version
 */
public class PlayerMilitaryProject {
	private int lv;
	private long exp;
	
	public void addExp(long exp) {
		this.exp+=exp;
		CommanderConfig config = SpringUtil.getBean(CommanderConfig.class);
		this.lv = config.getMilitaryProLv(this.exp);
	}

	public int getLv() {
		return lv;
	}
}


