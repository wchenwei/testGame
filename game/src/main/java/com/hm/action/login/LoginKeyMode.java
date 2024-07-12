package com.hm.action.login;


import com.hm.config.GameConstants;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.util.aes.CryptAES;
import lombok.Data;

/**
 * 登录加密
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/7/3 13:55
 */
@Data
public class LoginKeyMode {
    private String loginKey;

    private long uid;//账号
    private int channelId;//渠道
    private long timeOut;
    private String account;

    public static LoginKeyMode buildLoginKeyMode(JsonMsg msg) {
        String key = msg.getString("sessionKey");
        return buildLoginKeyMode(key);
    }

    public static LoginKeyMode buildLoginKeyMode(String key) {

        String param[] = CryptAES.AES_Decrypt(GameConstants.AES_KEY, key).split(",");

        LoginKeyMode result = new LoginKeyMode();
        result.loginKey = key;
        result.uid = Long.parseLong(param[0]);
        result.timeOut = Long.parseLong(param[1]);
        result.channelId = Integer.parseInt(param[2]);
        result.account = param[4];

        return result;
    }
}
