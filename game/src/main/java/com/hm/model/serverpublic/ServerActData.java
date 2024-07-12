package com.hm.model.serverpublic;

import cn.hutool.core.collection.CollUtil;

import java.util.List;

/**
 * 服务器活动共用数据
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/3/15 14:59
 */
public class ServerActData extends ServerPublicContext {
    private List<Integer> hideTypes;//隐藏的活动类型

    public List<Integer> getHideTypes() {
        return hideTypes;
    }

    public void setHideTypes(List<Integer> hideTypes) {
        if (CollUtil.isNotEmpty(hideTypes)) {
            this.hideTypes = hideTypes;
        } else {
            this.hideTypes = null;
        }
        SetChanged();
    }

}
