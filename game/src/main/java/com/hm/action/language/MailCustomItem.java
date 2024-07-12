package com.hm.action.language;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 邮件自定义内容
 * @date 2020年5月15日 下午8:22:15
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MailCustomItem {
    private String id;//代表语言id
    private String title;
    private String content;
}
