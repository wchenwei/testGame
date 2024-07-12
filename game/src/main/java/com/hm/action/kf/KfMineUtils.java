package com.hm.action.kf;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.hm.libcore.msg.JsonMsg;
import com.hm.message.MessageComm;
import com.hm.model.player.BasePlayer;
import com.hm.redis.type.RedisTypeEnum;

/**
 * @Description: 跨服资源红点
 * @author siyunlong  
 * @date 2019年6月20日 下午10:07:25 
 * @version V1.0
 */
public class KfMineUtils {
	public static void sendPlayerMineChange(BasePlayer player,boolean haveMine) {
		int hour = DateUtil.thisHour(true);
		if(hour >= 9 && hour <= 20) {
			JsonMsg showMsg = JsonMsg.create(MessageComm.S2C_KfMineInfo);
	    	showMsg.addProperty("haveMine", haveMine);
	        player.sendMsg(showMsg);
		}
	}

	public static void sendPlayerMineChange(BasePlayer player) {
		int hour = DateUtil.thisHour(true);
		if(hour >= 9 && hour <= 20) {
			JsonMsg showMsg = JsonMsg.create(MessageComm.S2C_KfMineInfo);
			showMsg.addProperty("haveMine", playerHaveMine(player.getId()));
			player.sendMsg(showMsg);
		}
	}
	
	public static boolean playerHaveMine(long playerId) {
		return StrUtil.isNotEmpty(RedisTypeEnum.KfResMine.get(String.valueOf(playerId)));
	}
	public static void playerAddMine(long playerId){
		RedisTypeEnum.KfResMine.put(playerId, 1);
	}
	public static void playerDelMine(long playerId){
		RedisTypeEnum.KfResMine.del(playerId);
	}
	public static void clearKfMine(){
		RedisTypeEnum.KfResMine.dropColl();
	}
	
}
