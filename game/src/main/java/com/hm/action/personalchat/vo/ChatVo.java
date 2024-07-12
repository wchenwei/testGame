package com.hm.action.personalchat.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wyp
 * @description
 * @date 2021/12/15 9:49
 */
@Data
@NoArgsConstructor
public class ChatVo {
    private long time;

    private long sender;

    private long receiver;

    private String content;

    private int type;


    public ChatVo(long fid, int type, String content, long receiver) {
        this.sender = fid;
        this.receiver = receiver;
        this.content = content;
        this.type = type;
        this.time = System.currentTimeMillis();
    }

}
