package com.hm.action.mail.vo;

import com.hm.action.language.MailCustomContent;
import com.hm.action.language.MailCustomItem;
import com.hm.action.mail.MailVO;
import com.hm.enums.MailType;
import com.hm.model.mail.Mail;
import com.hm.model.player.Player;

/**
 * 后台邮件
 */
public class GMMailVO extends MailVO {
    private MailCustomItem customItem;

    public GMMailVO(Player player, Mail mail) {
        super(player, mail);
        setType(MailType.GM.getType());
        //可以优化成按照当前系统展示
        MailCustomContent customContent = mail.getCustomContent();
        if (customContent != null) {
            this.customItem = customContent.getMailCustomItem(player);
        }
    }
}
