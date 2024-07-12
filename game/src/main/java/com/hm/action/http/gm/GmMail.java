package com.hm.action.http.gm;


import com.hm.action.language.MailCustomContent;
import com.hm.action.language.MailCustomItem;
import com.hm.enums.MailSendType;
import com.hm.enums.SplitType;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.model.mail.Mail;
import com.hm.model.mail.RegisterTimeMailFilter;
import com.hm.util.ItemUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 后台邮件
 *
 * @author 司云龙
 * @version 1.0
 * @date 2022/12/16 10:29
 */
@Data
@NoArgsConstructor
@Document(collection = "MailInfo")
public class GmMail {
    private String _id;
    private int send_type;
    private Date send_date;

    private String reward;
    private String title; //标题
    private String content; //内容

    private String serverInfo;
    private long startTime;
    private long endTime;



    public boolean isFitServer(int serverId) {
        if (!isFitTime()) {
            return false;
        }
        return new ServerIDFit(serverInfo).isFitServer(serverId);
    }

    public boolean isFitTime() {
        long now = System.currentTimeMillis();
        return now <= endTime;
    }

    public Mail buildMail(int serverId) {
        Mail mail = new Mail();
        mail.setId("gm_" + this._id);
        mail.setServerId(serverId);

        mail.setSendType(MailSendType.All.getType());
        mail.setSendDate(this.send_date.getTime());
        mail.setReward(ItemUtils.str2HTItemList(this.reward,",",":"));
        mail.setMailFilter(new RegisterTimeMailFilter(startTime, endTime));
        mail.setCustomContent(MailCustomContent.buildDefault(title,content));

        return mail;
    }

}
