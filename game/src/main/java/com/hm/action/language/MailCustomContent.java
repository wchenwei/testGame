package com.hm.action.language;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.model.player.Player;
import lombok.Data;

import java.util.Map;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 邮件自定义内容
 * @date 2020年5月15日 下午8:22:15
 */
@Data
public class MailCustomContent {
    private Map<String, MailCustomItem> lanMap = Maps.newHashMap();

    public MailCustomItem getMailCustomItem(Player player) {
        MailCustomItem temp = this.lanMap.get(player.playerFix().getLanguage());
        if (temp != null) {
            return temp;
        }
        if (lanMap.isEmpty()) {
            return null;
        }
        return Lists.newArrayList(lanMap.values()).get(0);
    }

    public void addMailCustomItem(Player player, String title, String content) {
        MailCustomItem mailCustomItem = new MailCustomItem(player.playerFix().getLanguage(), title, content);
        this.lanMap.put(mailCustomItem.getId(), mailCustomItem);
    }


    public static MailCustomContent buildDefault(String title, String content) {
        MailCustomContent customContent = new MailCustomContent();
        customContent.getLanMap().put("1",new MailCustomItem("1",title,content));
        return customContent;
    }
}
