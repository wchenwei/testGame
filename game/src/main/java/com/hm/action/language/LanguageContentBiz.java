package com.hm.action.language;

import com.hm.libcore.language.LanguageVo;
import com.hm.action.language.vo.NoticeContentVo;

/**
 * @author xjt
 * @Data 2020年5月19日16:15:36
 */
public class LanguageContentBiz {

    public static NoticeContentVo createBroadVo(String id, int campId, int areaId, LanguageVo... args) {
        NoticeContentVo vo = new NoticeContentVo();
        vo.setId(id);
        vo.setParams(args);
        vo.setCamp(campId);
        vo.setAreaId(areaId);
        return vo;
    }

    public static NoticeContentVo createBroadVo(String id, LanguageVo... args) {
        NoticeContentVo vo = new NoticeContentVo();
        vo.setId(id);
        vo.setParams(args);
        return vo;
    }
}
