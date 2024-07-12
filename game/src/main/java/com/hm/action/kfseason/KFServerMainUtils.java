package com.hm.action.kfseason;

import cn.hutool.core.util.StrUtil;
import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.enums.KfType;
import com.hm.model.activity.kfactivity.KfServerInfo;

import java.util.List;

/**
 * TODO
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/5/24 15:12
 */
public class KFServerMainUtils {
    public static boolean isMainServer(KfType kfType) {
        List<KfServerInfo> kfServerList = KfServerInfo.getKfServerInfo(kfType.getType());
        return kfServerList.size() > 0 && StrUtil.startWith(kfServerList.get(0).getUrl(), getCurUrl());
    }

    public static String getCurUrl() {
        String url = ServerConfig.getInstance().getHostName();
        return url + "#" + ServerConfig.getInstance().getPort();
    }
}
