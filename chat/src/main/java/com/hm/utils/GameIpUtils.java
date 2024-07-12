package com.hm.utils;

import cn.hutool.core.util.StrUtil;
import com.hm.libcore.enums.EnvType;
import com.hm.libcore.serverConfig.GameServerMachine;
import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.redis.RedisTypeEnum;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class GameIpUtils {

    public void registerGameIp() {
        EnvType envType = EnvType.getType(ServerConfig.getInstance().getEnvtype());
        String outIp = envType.getOutIp();
        String innerIp = envType.getInnerIp();
        log.error("本机ip:"+outIp+"->"+innerIp);
        if(isInnerIp(outIp)) {
            return;
        }
        RedisTypeEnum.GameInnerIp.put(outIp,innerIp);
    }

    public String getInnerIp(String ip) {
        if(isInnerIp(ip)) {
            return ip;
        }
        String result = RedisTypeEnum.GameInnerIp.get(ip);
        if(StrUtil.isNotEmpty(result)) {
            return result;
        }
        return ip;
    }

    public static boolean isInnerIp(String ip) {
        return StrUtil.startWith(ip,"192.168");
    }

    public static String getGameRpcUrl(GameServerMachine serverMachine) {
        String innerIp = getInnerIp(serverMachine.getHost());
        return innerIp+":"+serverMachine.getRpcPort();
    }

    public static String getGameHttpUrl(GameServerMachine serverMachine) {
        String innerIp = getInnerIp(serverMachine.getHost());
        return "http://"+innerIp+":"+serverMachine.getHttp_port();
    }
}
