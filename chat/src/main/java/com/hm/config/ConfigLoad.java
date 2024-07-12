package com.hm.config;

import java.util.List;
import java.util.Map;

import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.ExcleConfig;

public class ConfigLoad {
    /**
     * 启动服务器时调用
     * 1,下载所有配置文件
     * 2,加载配置文件到内存
     */
    public static void loadAllConfig() {
        ResourceReader.getInstance().downloadAllProp();
        //加载类
        Map<String, ExcleConfig> maps = SpringUtil.getBeanMap(ExcleConfig.class);
        for (ExcleConfig config : maps.values()) {
            try {
                config.loadConfig();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (BaseConfig config : SpringUtil.getBeanMap(BaseConfig.class).values()) {
            config.loadConfig();
        }
    }

}
