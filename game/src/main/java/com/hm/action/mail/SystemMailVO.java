package com.hm.action.mail;

import com.hm.libcore.language.LanguageVo;
import com.hm.enums.MailType;
import com.hm.model.mail.Mail;
import com.hm.model.player.Player;

/**
 * 系统邮件
 */
public class SystemMailVO extends MailVO {
    private LanguageVo[] params;

    public SystemMailVO(Player player, Mail mail) {
        super(player, mail);
        setType(MailType.SYSTEM.getType());
        this.params = mail.getParams();
    }
}
