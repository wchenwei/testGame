package com.hm.message;

/**
 * Title: SysConstant.java
 * Description:
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 *
 * @author 李飞
 * @version 1.0
 * @date 2015年4月23日 上午11:04:35
 */
public final class SysConstant {

    public final static int NO = 0;

    public final static int YES = 1;

    public final static int PARAM_ERROR = 9998;//参数错误

    public final static int SYS_ERROR = 9999;//系统错误

    public final static int SESSIONKEY_ERROR = 1000001;//sessionKey非法

    public final static int SESSION_TIMEOUT = 1000002;//无效SESSION 需要重连

    public static final int SERVER_CLOSE = 1000003;//服务器维护中

    public static final int NOT_CHAT = 27001;//禁言中
    public static final int SENSITIVE_WORD = 27002; // 发言包含敏感词

    public static final int NO_GUILD = 56007; //没有军团
}
