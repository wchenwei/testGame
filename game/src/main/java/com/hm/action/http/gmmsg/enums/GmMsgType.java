package com.hm.action.http.gmmsg.enums;

import com.hm.libcore.spring.SpringUtil;
import com.hm.action.http.gmmsg.biz.RecucePlayerCurrencyBiz;
import com.hm.action.http.gmmsg.biz.SmallGameH5Biz;

import java.util.Map;

/**
 * @ClassName GmMsgType
 * @Deacription gm消息类型
 * @Author zxj
 * @Date 2022/2/22 10:01
 * @Version 1.0
 **/
public enum GmMsgType {
    SmallGameH5(1, "h5小游戏"){
        @Override
        public String doMsg(Map<String, String> paramMap) {
            SmallGameH5Biz tempBiz = SpringUtil.getBean(SmallGameH5Biz.class);
            return tempBiz.doMsg(paramMap);
        }
    },

    ReducePlayerCurrency(2, "扣用户资源信息"){
        @Override
        public String doMsg(Map<String, String> paramMap) {
            RecucePlayerCurrencyBiz tempBiz = SpringUtil.getBean(RecucePlayerCurrencyBiz.class);
            return tempBiz.reduce(paramMap);
        }
    },
    ;
    private int type;
    private String desc;

    private GmMsgType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public static GmMsgType getMsgType(int type) {
       for (GmMsgType tempType : GmMsgType.values()) {
            if(tempType.getType() == type) {
                return tempType;
            }
        }
        return null;
    }

    public abstract String doMsg(Map<String, String> paramMap);
}
