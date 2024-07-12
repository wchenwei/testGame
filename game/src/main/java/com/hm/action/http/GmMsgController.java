package com.hm.action.http;

import cn.hutool.json.JSONUtil;
import com.hm.libcore.httpserver.handler.HttpSession;
import com.hm.action.http.gmmsg.enums.GmMsgType;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @ClassName GmMsgController
 * @Deacription gm发过来的消息接口
 * @Author zxj
 * @Date 2022/2/22 9:54
 * @Version 1.0
 **/
@Slf4j
@Service("gmMsg.do")
public class GmMsgController {

    public void msg(HttpSession session) {
        int msgType = Integer.parseInt(session.getParams("type"));
        GmMsgType type = GmMsgType.getMsgType(msgType);
        Map<String, String> paramMap = session.getParams();
        if(null==type) {
            log.error("gmMsg.do,msg,type is null："+ JSONUtil.toJsonStr(paramMap));
            session.Write("-1");
        }
        session.Write(type.doMsg(paramMap));
    }

}
