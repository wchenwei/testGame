package com.hm.timer;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.hm.db.ChatMsgUtils;

@Service
public class ClearChatTimer {
    /**
     * 每天0点执行
     */
    @Scheduled(cron = "0 0 4 * * ?")
    public void clearChat() {
        ChatMsgUtils.clearChat();
    }
}
