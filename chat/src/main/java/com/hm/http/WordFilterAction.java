package com.hm.http;

import com.hm.libcore.httpserver.handler.HttpSession;
import com.hm.container.ForbidWordManager;
import com.hm.enums.HttpPostStrResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * ClassName: WordFilterAction. <br/>
 * Function: 敏感字，关键词过滤. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2018年5月23日 下午5:49:09 <br/>
 *
 * @author zxj
 */
//关键词过滤的操作
@Slf4j
@Service("wordFilterAction.do")
public class WordFilterAction {

    //同步关键词到聊天服
    public void ansyc(HttpSession session) {
//        int type = Convert.toInt(session.getParams("type"));
//        switch (type){
//            case 1:
//                ForbidWordManager.initBadWord();
//                break;
//            case 2:
//                ForbidWordManager.initReportWord();
//                break;
//            case 3:
//                ForbidWordManager.initSysWord();
//                break;
//            default:
//                session.Write(HttpPostStrResult.ERROR.getType());
//                return;
//        }
        ForbidWordManager.initWordFromDB();
        session.Write(HttpPostStrResult.SUCC.getType());
    }
}









