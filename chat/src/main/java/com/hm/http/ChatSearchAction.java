package com.hm.http;

import com.alibaba.fastjson.JSON;
import com.hm.libcore.httpserver.handler.HttpSession;
import com.hm.config.GameConstants;
import com.hm.db.ChatMsgUtils;
import com.hm.db.Page;
import com.hm.db.PageUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


//查询聊天信息
@Service("chatSearch.do")
public class ChatSearchAction {
    //删除聊天服的关键词过滤
    @SuppressWarnings("rawtypes")
    public void search(HttpSession session) {
        Integer currentPage = 1;
        if (!StringUtils.isEmpty(session.getParams("page"))) {
            currentPage = Integer.parseInt(session.getParams("page"));
        }
        Integer pageSize = 10;
        if (!StringUtils.isEmpty(session.getParams("limit"))) {
            pageSize = Integer.parseInt(session.getParams("limit"));
        }
        if (StringUtils.isEmpty(session.getParams("type")) || StringUtils.isEmpty(session.getParams("server"))) {
            session.Write(JSON.toJSONString(new PageUtils(new Page(0, 0, 0))));
        } else {
            String type = session.getParams("type");
            String serverId = session.getParams("server");
            // 起止时间
            String beginTime = session.getParams("beginTime");
            String endTime = session.getParams("endTime");
            PageUtils pageUtis = ChatMsgUtils.getChatListPage(pageSize, currentPage, session, this.getRoomId(Integer.parseInt(type)), Integer.parseInt(serverId), beginTime, endTime);
            String jsonStr = JSON.toJSONString(pageUtis);
            session.Write(jsonStr);
        }
    }

    private String getRoomId(int type) {//0-世界 1-军团
        switch(type){
            case 0:
                return "room_world";
            case 1:
                return "room_guild";
        }
        // TODO 待处理
        return GameConstants.RoomSys;
    }
}









