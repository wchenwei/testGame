/**  
 * Project Name:SLG_GameHot.
 * File Name:MonitorAction.java  
 * Package Name:com.hm.action.http  
 * Date:2018年5月17日上午9:17:56  
 * Copyright (c) 2018, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
 */


package com.hm.action.http;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.action.cmq.CmqBiz;
import com.hm.action.cmq.CmqMsg;
import com.hm.action.mission.biz.MissionRecordBiz;
import com.hm.config.GameConfig;
import com.hm.db.PlayerUtils;
import com.hm.db.dbmonitor.DbMonotor;
import com.hm.leaderboards.HdLeaderboardsService;
import com.hm.libcore.httpserver.handler.HttpSession;
import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.util.URLUtil;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.model.player.Player;
import com.hm.mq.game.GameMqConfig;
import com.hm.war.sg.statistics.DamageUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

//用于服务器监控
@Service("monitor.do")
public class MonitorAction {
	private final static String SUCC = "succ";

	//系统监控
	public void getMonitor(HttpSession session) {
		session.Write(MonitorAction.SUCC);
	}
	
	public void getGameVersion(HttpSession session) {
		try {
			Map<String,Object> map = Maps.newHashMap();
			map.put("StartDate", GameConfig.StartDate);
			map.put("GameVersion", GameConfig.GameVersion);
			map.put("errorExcelList", GameConfig.errorExcelList);
			session.Write(GSONUtils.ToJSONString(map));
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.Write("fail");
		return;
	}


	public void changeLeaderHttpType(HttpSession session) {
		try {
			int httpType = Integer.parseInt(session.getParams("type"));
			HdLeaderboardsService.getInstance().setHttpType(httpType);
			session.Write("suc"+HdLeaderboardsService.getInstance().getHttpType());
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.Write("fail");
		return;
	}
	
	
	public void changeMaxRetryNum(HttpSession session) {
		try {
			int num = Integer.parseInt(session.getParams("num"));
			URLUtil.MaxRetryNum = num;
			session.Write("suc"+URLUtil.MaxRetryNum);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.Write("fail");
		return;
	}
	
	public void changeDBMonitor(HttpSession session) {
		try {
			DbMonotor.MonotorSwitch = StrUtil.equals("1", session.getParams("switch"));
			DbMonotor.CheckMinute = Integer.parseInt(session.getParams("CheckMinute"));
			DbMonotor.CheckSaveSecond = Integer.parseInt(session.getParams("CheckSaveSecond"));
			session.Write("suc"+DbMonotor.MonotorSwitch+"_"+DbMonotor.CheckMinute+"_"+DbMonotor.CheckSaveSecond);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.Write("fail");
		return;
	}
	
	public void printDamage(HttpSession session) {
		session.Write(DamageUtils.damageInfo());
	}


	public void testMq(HttpSession session) {
		try {
			CmqBiz cmqBiz = SpringUtil.getBean(CmqBiz.class);
			CmqMsg msg = new CmqMsg(1);
			msg.addProperty("game", "tank3a");
			cmqBiz.sendMessage(GameMqConfig.getInstance().getDefaultTopic(), Lists.newArrayList(msg));
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.Write("fail");
		return;
	}

	public void showPlayer(HttpSession session) {
		int id = Integer.parseInt(session.getParams("id"));

		Player player = PlayerUtils.getPlayerFromKF(id);
		session.Write(player == null?"find not":player.getName());
	}

	public void loadPveRecord(HttpSession session) {
		SpringUtil.getBean(MissionRecordBiz.class).checkTowerBattleOldData();
		session.Write("suc");
	}

	public void saveCTest(HttpSession session) {
		String id = session.getParams("id");
		String v = session.getParams("v");

		new CTestData(id,v).saveDB();
		session.Write("suc");
	}

	public void getCTest(HttpSession session) {
		String id = session.getParams("id");
		CTestData cTestData = CTestData.getCTestData(id);
		if(cTestData != null) {
			session.Write(cTestData.getVal());
		}else{
			session.Write("");
		}
	}

}



