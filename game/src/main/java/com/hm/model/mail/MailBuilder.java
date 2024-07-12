package com.hm.model.mail;

import cn.hutool.core.convert.Convert;
import com.google.common.collect.Sets;

import com.hm.action.language.MailCustomContent;
import com.hm.libcore.language.LanguageVo;
import com.hm.action.mail.biz.MailBiz;
import com.hm.enums.MailConfigEnum;
import com.hm.enums.MailSendType;
import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.util.GameIdUtils;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

@Slf4j
@Data
@Accessors(chain=true)
public class MailBuilder {
    private MailConfigEnum mailType; //标题
    private int serverId;
    private MailSendType sendType; //发送类型MailSendType
    private Set<Long> receivers; //收件人
    private List<Items> reward; // 奖励

    private LanguageVo[] params;
    private IMailFilter mailFilter;//领取邮件的条件
    private MailExtraVo mailExtraVo;//邮件额外信息
    private MailCustomContent customContent; //自定义邮件



    public static MailBuilder player(Player player) {
        MailBuilder builder = new MailBuilder();
        builder.serverId = player.getServerId();
        builder.receivers = Sets.newHashSet(player.getId());
        builder.sendType = MailSendType.One;
        return builder;
    }

    public static MailBuilder server(List<Integer> serverList) {
        MailBuilder builder = new MailBuilder();
        builder.receivers = Sets.newHashSet();
        for (long serverId : serverList) {
            builder.receivers.add(serverId);
        }
        builder.sendType = MailSendType.All;
        return builder;
    }

    public static MailBuilder group(int serverId,Set<Long> receivers) {
        MailBuilder builder = new MailBuilder();
        builder.receivers = receivers;
        builder.serverId = serverId;
        builder.sendType = receivers.size() > 1?MailSendType.Group:MailSendType.One;
        return builder;
    }

    public MailBuilder param(LanguageVo... params) {
        this.params = params;
        return this;
    }


    public Mail buildMail() {
        if ((mailType == null && customContent == null)) {
            return null;
        }
        if(this.sendType == null) {
            this.sendType = receivers.size() == 1?MailSendType.One:MailSendType.Group;
        }
        Mail mail = new Mail(GameIdUtils.nextStrId(),serverId, reward, sendType);
        mail.setReceivers(receivers);
        mail.setParams(params);
        mail.setMailFilter(mailFilter);
        mail.setMailExtraVo(mailExtraVo);
        mail.setCustomContent(customContent);
        return mail;
    }

    public void send() {
        Mail mail = buildMail();
        if(mail == null) {
            log.error("邮件发送失败: mail is null");
            return;
        }
        SpringUtil.getBean(MailBiz.class).saveAndSendMail(mail);
    }
}
