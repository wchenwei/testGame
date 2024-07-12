package com.hm.action.http;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.hm.libcore.httpserver.handler.HttpSession;
import com.hm.util.StringUtil;
import com.hm.util.UpdateLockUtil;
import com.hm.libcore.annotation.Action;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wyp
 * @description
 * @date 2022/6/30 16:59
 */
@Service("lockMsgId.do")
public class UpdateLockAction {

    public void setParam(HttpSession session) {
        String startTime = session.getParams("startTime");
        String endTime = session.getParams("endTime");
        UpdateLockUtil.startTime = Convert.toLong(startTime);
        UpdateLockUtil.endTime = Convert.toLong(endTime);
        String msgIds = session.getParams("msgIds");
        if(StrUtil.isNotBlank(msgIds)){
            List<Integer> msgIdList = StringUtil.splitStr2IntegerList(msgIds, ",");
            UpdateLockUtil.lockMsgIdList = msgIdList;
        }
        session.Write("succ");
    }
}
