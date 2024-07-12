package com.hm.action.loginmsg;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.msg.JsonMsg;
import com.hm.message.MessageComm;
import com.hm.model.player.Player;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.MongodDB;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.stream.Collectors;

@Biz
@Slf4j
public class LoginMsgBiz implements IObserver {
    private static List<LoginMsg> loginMsgList = Lists.newArrayList();

    public static void init() {
        loginMsgList.clear();
        try {
            log.info("loading LoginMsg begin");
            MongodDB loginMongodDB = MongoUtils.getLoginMongodDB();
            Criteria criteria = Criteria.where("isdel").is(0).and("endTime").gt(System.currentTimeMillis());
            Query query = new Query(criteria);
            loginMsgList = loginMongodDB.query(query, LoginMsg.class);
            log.info("loading LoginMsg end {} {}", System.lineSeparator(), JSON.toJSONString(loginMsgList));
        } catch (Exception e) {
            log.error("loading login msg", e);
        }
    }

    public void checkLoginMsg(Player player) {
        int ch = player.playerFix().getLoginChannelId();
        List<LoginMsg> msgs = loginMsgList.stream().filter(msg -> msg.isValid(ch)).collect(Collectors.toList());
        if (CollUtil.isEmpty(msgs)) {
            return;
        }

        List<LoginMsgVO> r = msgs.stream().map(m -> new LoginMsgVO(m.getCount(), m.getContext())).collect(Collectors.toList());
        JsonMsg jsonMsg = JsonMsg.create(MessageComm.S2C_Login_Msg);
        jsonMsg.addProperty("msg", r);
        player.sendMsg(jsonMsg);
    }

    @Override
    public void registObserverEnum() {
        ObserverRouter.getInstance().registObserver(ObservableEnum.HourEvent, this);

    }

    @Override
    public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
        if (observableEnum.getEnumId() == ObservableEnum.HourEvent.getEnumId()) {
            init();
        }
    }
}
