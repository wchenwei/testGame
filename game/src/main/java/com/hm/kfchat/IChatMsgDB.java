package com.hm.kfchat;

import com.hm.enums.KfType;

import java.util.List;

/**
 * @author 司云龙
 * @version 1.0
 * @date 2022/3/3 15:49
 */
public interface IChatMsgDB {
    void saveMsg(KFChatMsg msg);//保存

    default void delMsg(KFChatMsg msg) {

    }

    List<KFChatMsg> queryAll();//查询

    KfType getKFType();

    String getDBName();
}
