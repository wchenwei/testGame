package com.hm.action.http.gm;

import com.google.common.collect.Lists;
import com.hm.enums.MailSendType;
import com.hm.model.mail.Mail;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.server.GameServerManager;
import com.hm.servercontainer.mail.MailServerContainer;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台邮件处理器
 *
 * @author 司云龙
 * @version 1.0
 * @date 2022/12/16 10:43
 */
public class GmMailManager {
    private static GmMailManager instance = new GmMailManager();

    public static GmMailManager getInstance() {
        return instance;
    }

    private List<GmMail> gmMailList = Lists.newArrayList();

    //启动物理机时初始化
    public void loadMail() {
        gmMailList = getGmMailList();
    }

    //启动服务器时加载
    public List<Mail> getGmMailForServer(int serverId) {
        return gmMailList.stream().filter(e -> e.isFitServer(serverId))
                .map(e -> e.buildMail(serverId))
                .collect(Collectors.toList());
    }

    //后台通过后加载
    public void loadGmMail(int mailId) {
        GmMail gmMail = MongoUtils.getLoginMongodDB().get(mailId, GmMail.class);
        if (gmMail == null) {
            return;
        }
        for (int serverId : GameServerManager.getInstance().getServerIdList()) {
            if (gmMail.isFitServer(serverId)) {
                Mail mail = gmMail.buildMail(serverId);
                MailServerContainer.of(serverId).addMail(mail);
            }
        }
        this.gmMailList.add(gmMail);
    }

    public static List<GmMail> getGmMailList() {
        Query query = new Query(Criteria.where("send_type").is(MailSendType.RegisterDate.getType())
                .and("check_state").is(1));
        List<GmMail> mailList = MongoUtils.getLoginMongodDB().query(query, GmMail.class);
        mailList.removeIf(e -> !e.isFitTime());
        return mailList;
    }
}
