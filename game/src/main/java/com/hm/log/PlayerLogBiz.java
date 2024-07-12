package com.hm.log;

import com.hm.libcore.annotation.Biz;
import com.hm.action.cmq.CmqBiz;
import com.hm.action.http.biz.HttpBiz;
import com.hm.model.player.Player;

import javax.annotation.Resource;

@Biz
public class PlayerLogBiz {
	@Resource
	private LogBiz logBiz;
	@Resource
	private CmqBiz cmqBiz;
	@Resource
	private HttpBiz httpBiz;
	
	public void logLogin(Player player) {
		try {
			logBiz.savePlayerLoginLog(player);
			//cmqBiz.sendPlayerLogin(player);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void logRegister(Player player) {
		try {
			logBiz.savePlayerRegisterLog(player);
			cmqBiz.sendPlayerRegister(player);
			httpBiz.sendLoginCreateServer(player);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
}
