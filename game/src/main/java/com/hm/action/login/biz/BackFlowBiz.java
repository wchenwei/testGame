package com.hm.action.login.biz;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.hm.libcore.annotation.Biz;
import com.hm.action.mail.biz.MailBiz;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.MailConfig;
import com.hm.config.excel.temlate.MailTemplate;
import com.hm.enums.MailConfigEnum;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.redis.type.RedisTypeEnum;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author wyp
 * @description 老用户回流奖励
 * @date 2021/7/23 9:12
 */
@Biz
public class BackFlowBiz {
    @Resource
    private MailBiz mailBiz;
    @Resource
    private MailConfig mailConfig;
    @Resource
    private CommValueConfig commValueConfig;
    // 已发送
    private String success = "2";

    public void checkBackFlowPlayer(Player player) {
        try {
            Date lastLoginDate = player.playerBaseInfo().getLastLoginDate();
            if (lastLoginDate == null) {
                return;
            }
            DateTime dateTime = DateUtil.offsetDay(lastLoginDate, 6);
            if (DateUtil.date().before(dateTime)) {
                return;
            }
            String field = String.valueOf(player.getId());
            if (!StrUtil.equals("1", RedisTypeEnum.PlayerReword.get(field))) {
                return;
            }
            // 发送奖励
            MailTemplate mailTemplate = mailConfig.getMailTemplate(MailConfigEnum.BackFlow);
            List<Items> backFlowReward = commValueConfig.getBackFlowReward(player.getPlayerVipInfo().getVipLv());

            RedisTypeEnum.PlayerReword.put(field, success);
            if (CollUtil.isEmpty(backFlowReward)) {
                return;
            }
            mailBiz.sendSysMail(player, mailTemplate, backFlowReward);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
