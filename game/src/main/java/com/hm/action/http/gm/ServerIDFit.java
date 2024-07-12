package com.hm.action.http.gm;

import com.hm.util.StringUtil;
import lombok.NoArgsConstructor;

/**
 * 服务器id
 *
 * @author 司云龙
 * @version 1.0
 * @date 2023/1/3 16:30
 */
@NoArgsConstructor
public class ServerIDFit {
    private String serverInfo;

    public ServerIDFit(String serverInfo) {
        this.serverInfo = serverInfo;
    }

    public boolean isFitServer(int serverId) {
        if (serverInfo == null || "-1".equals(serverInfo)) {
            return true;
        }
        return isFitServerForStr(serverInfo, serverId);
    }

    public static boolean isFitServerForStr(String extend, int serverId) {
        //extend:1_20,101_120;21_40,121_140
        //如果serverId满足该组的条件则将分组id命名为起始id,如例子中，满足条件
        try {
            for (String s : StringUtil.splitStr2StrList(extend, ";")) {
                int startId = Integer.parseInt(s.split("_")[0]);
                int endId = Integer.parseInt(s.split("_")[1]);
                if (serverId >= startId && serverId <= endId) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }
}
