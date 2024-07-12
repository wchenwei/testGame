package com.hm.mq.msg;

import com.hm.libcore.msg.DefaultMsg;
import com.hm.libcore.soketserver.handler.HMSession;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

/**
 * 内网通讯码
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/6/21 17:15
 */
@Data
@NoArgsConstructor
public class InnerMsg extends DefaultMsg {
    private long id;
    @Transient
    private transient HMSession session;

    public InnerMsg(int msgId) {
        super(msgId);
    }

    public InnerMsg addMsg(String key, Object value) {
        if (value == null) {
            return this;
        }
        this.paramMap.put(key, value);
        return this;
    }

    public static InnerMsg create(int msgId) {
        return new InnerMsg(msgId);
    }
}
