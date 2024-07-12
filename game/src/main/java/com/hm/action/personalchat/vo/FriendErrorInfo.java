package com.hm.action.personalchat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xjt
 * @version 1.0
 * @desc 表述
 * @date 2022/3/9 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendErrorInfo {
    private long playerId;
    private int errorCode;
}
