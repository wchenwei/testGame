package com.hm.model.personalchat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalChatMsg {
    private long playerId;
    private String content;
    private long time;

    public PersonalChatMsg(long playerId, String content) {
        this.playerId = playerId;
        this.content = content;
        this.time = System.currentTimeMillis();
    }
}
