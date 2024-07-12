package com.hm.db.dbmonitor;

import cn.hutool.core.date.DateUtil;
import com.hm.config.GameConstants;
import com.hm.model.player.BasePlayer;
import com.hm.util.RandomUtils;

public class DbMonotor {
	public static boolean MonotorSwitch = true;
	public static int CheckMinute = 60;
	public static int CheckSaveSecond = 40;
	public static int CheckSaveSecondFree = 20;
	
	public static boolean checkCanSavePlayer(BasePlayer player) {
		if(!MonotorSwitch) {
			return true;
		}
		//判断时间
		int curMinute = DateUtil.thisMinute();
		if(curMinute <= CheckMinute) {
			int curHour = DateUtil.thisHour(true);
			if(curHour == 12 || curHour == 18 
					|| curHour == 13 || curHour == 19 || curHour == 20 || curHour == 0) {
				if(curMinute <= 5) {
					if(RandomUtils.randomIsRate(0.1)) {
						return System.currentTimeMillis() - player.lastDBTime > CheckSaveSecond*GameConstants.SECOND;
					}
					return false;
				}
				return System.currentTimeMillis() - player.lastDBTime > CheckSaveSecond*GameConstants.SECOND;
			}else{
				return System.currentTimeMillis() - player.lastDBTime > CheckSaveSecondFree*GameConstants.SECOND;
			}
		}
		return true;
	}
}
