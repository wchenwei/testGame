package com.hm.action.language.vo;

import com.hm.libcore.language.LanguageVo;
import lombok.Data;

/**
 * 公告内容
 */
@Data
public class NoticeContentVo {
    private String id; // 公告表broadcast的key字段，跟语言表对应
    private LanguageVo[] params;
    private int camp;
    private int areaId;
}
