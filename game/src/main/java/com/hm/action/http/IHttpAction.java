package com.hm.action.http;

import com.hm.libcore.httpserver.handler.HttpSession;
import com.hm.server.GameServerManager;

/**
 * Description:
 * User: yang xb
 * Date: 2018-09-29
 */
interface IHttpAction {
    /**
     * 解析http session 里的 server id 信息
     * 出错时返回-1
     *
     * @param session
     * @return
     */
    default int getServerId(HttpSession session) {
        if (session == null || !session.getParams().containsKey("serverId")) {
            return -1;
        }

        try {
            int serverId = Integer.parseInt(session.getParams("serverId"));
            boolean isHave = GameServerManager.getInstance().getServerIdList().stream().anyMatch(i -> i == serverId);
            if (isHave) {
                return serverId;
            }
        } catch (NumberFormatException e) {
            return -1;
        }
        return -1;
    }
}
