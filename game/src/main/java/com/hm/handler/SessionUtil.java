package com.hm.handler;

import com.hm.libcore.soketserver.handler.HMSession;
import com.hm.db.PlayerUtils;
import com.hm.model.player.Player;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

@Slf4j
public class SessionUtil {

	public static Player getPlayer(HMSession session) {
		Object attachment = session.getAttachment();
		try {
			if(attachment == null) {
				return null;
			}
			long playerId = (long)attachment;
			return PlayerUtils.getOnlinePlayer(playerId);
		} catch (Exception e) {
			log.error("SessionUtil:服务器异常"+attachment,e);
		}
		return null;
	}
}
