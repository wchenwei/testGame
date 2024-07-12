package com.hm.action.kf;

import cn.hutool.core.util.StrUtil;
import com.hm.libcore.msg.JsonMsg;
import com.hm.message.MessageComm;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import com.hm.redis.type.RedisTypeEnum;

/**
 * @Description: 跨服建筑征讨红点
 * @author siyunlong  
 * @date 2019年6月20日 下午10:07:25 
 * @version V1.0
 */
public class KfBuildExtortUtils {
	public static void sendPlayerChange(BasePlayer player) {
		boolean haveRed = playerHaveRed(player.getId());
		if(haveRed) {
			JsonMsg showMsg = JsonMsg.create(MessageComm.S2C_KfBuildExtortRed);
			showMsg.addProperty("haveRed", true);
			player.sendMsg(showMsg);
		}
	}
	
	public static boolean playerHaveRed(long playerId) {
		return StrUtil.isNotEmpty(RedisTypeEnum.KfBuildExtort.get(String.valueOf(playerId)));
	}
	public static void playerAdd(Player player){
		RedisTypeEnum.KfBuildExtort.put(player.getId(), 1);
		JsonMsg showMsg = JsonMsg.create(MessageComm.S2C_KfBuildExtortRed);
    	showMsg.addProperty("haveRed", true);
        player.sendMsg(showMsg);
	}
	public static void playerDel(long playerId){
		RedisTypeEnum.KfBuildExtort.del(playerId);
	}
}
