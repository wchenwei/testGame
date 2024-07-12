package com.hm.http;

import com.hm.action.ChatBiz;
import com.hm.libcore.httpserver.handler.HttpSession;
import com.hm.container.PlayerContainer;
import com.hm.enums.HttpPostStrResult;
import com.hm.enums.SpecialPlayerEnum;
import com.hm.model.Player;
import com.hm.room.RoomManager;
import com.hm.room.ServerRooms;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * ClassName: HttpChatAction. <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2018年6月28日 下午1:44:18 <br/>
 *
 * @author yanpeng
 */
@Slf4j
@Service("HttpChatAction.do")
public class HttpChatAction {
    @Resource
    private ChatBiz chatBiz;
    @Resource
    private PlayerContainer playerContainer;

    /**
     * 清空内存聊天记录
     * http://62.234.73.42:9090/?action=HttpChatAction.do&m=clearChatMsg&serverId=36
     *
     * @param session
     * @author yanpeng
     */
    public void clearChatMsg(HttpSession session) {
        try {
            Map<String, String> map = session.getParams();
            int serverId = Integer.parseInt(map.get("serverId"));
            ServerRooms rooms = RoomManager.getIntance().getServerRooms(serverId);
            rooms.clearChatMsg();
        } catch (Exception e) {
            session.Write("clear chat fail\n");
            return;
        }
        session.Write("clear chat succ\n");
    }

    /**
     * 使用http请求发送消息
     *
     * @param session
     */
    public void sendMsgToWord(HttpSession session) {
        try {
            int serverId = Integer.parseInt(session.getParams("serverId"));
            long playerId = Long.parseLong(session.getParams("playerId"));
            String content = session.getParams("content");
            SpecialPlayerEnum playerEnum = SpecialPlayerEnum.getSpecialPlayer(playerId);
            if (playerEnum != null && playerContainer.getPlayer(playerId) == null) {
                Player player = new Player();
                player.setId(playerId);
                player.setServerId(serverId);
                player.setName(playerEnum.getPlayerName());
                playerContainer.addPlayer2Map(player);
            }
            chatBiz.sendGMMsg(serverId, playerId, content);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("使用http请求发送消息失败", e);
            session.Write(HttpPostStrResult.ERROR.getType());
            return;
        }
        session.Write(HttpPostStrResult.SUCC.getType());
    }

    /**
     * 刷新聊天服房间
     *
     * @param session
     */
    public void refreshServerChatMsg(HttpSession session) {
        try {
            RoomManager.getIntance().refreshRooms();
        } catch (Exception e) {
            session.Write("refresh chat fail\n");
            return;
        }
        session.Write("refresh chat succ\n");
    }
}
