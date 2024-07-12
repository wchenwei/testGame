/**
 * Project Name:SLG_GameFuture.
 * File Name:CommonValueType.java
 * Package Name:com.hm.enums
 * Date:2018年1月5日下午5:08:07
 * Copyright (c) 2018, 北京中科奥科技有限公司 All Rights Reserved.
 */

package com.hmkf.model;

/**
 * ClassName:CommonValueType <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason:   TODO ADD REASON. <br/>  
 * Date:     2018年1月5日 下午5:08:07 <br/>  
 * @author zqh
 * @version 1.1
 * @since
 */
public enum KfCmType {
    AreanFreeTimes(26,"竞技场免费挑战次数"),

    KfRankRefreshSpend(555, "【跨服排位赛】刷新消耗"),

    RevengeScores(5056, "跨服排位赛】积分"),
    KfRankBuySpend(5057, "跨服排位赛】购买次数"),
    NormalScoresParma(5061, "跨服排位赛】积分"),
    NormalScores(5062, "跨服排位赛】积分"),

    ;

    /**
     * @param type
     * @param desc
     */
    private KfCmType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    private int type;

    private String desc;

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static KfCmType getCommonType(int type) {
        for (KfCmType commonType : KfCmType.values()) {
            if (commonType.getType() == type) {
                return commonType;
            }
        }
        return null;
    }

}
  
