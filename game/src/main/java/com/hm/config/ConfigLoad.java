package com.hm.config;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.hm.libcore.annotation.ConfigLoadIndex;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.ExcleConfig;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
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
		//对加载顺序进行排序
		List<ExcleConfig> excelConfigs = maps.values().stream().sorted(Comparator.comparing(ExcleConfig::getClass, (o1, o2) -> {
			int index1 = o1.isAnnotationPresent(ConfigLoadIndex.class) ? o1.getAnnotation(ConfigLoadIndex.class).value() : Integer.MAX_VALUE;
			int index2 = o2.isAnnotationPresent(ConfigLoadIndex.class) ? o2.getAnnotation(ConfigLoadIndex.class).value() : Integer.MAX_VALUE;
			return index2 - index1;
		})).collect(Collectors.toList());

		for (ExcleConfig config : excelConfigs) {
			try {
				config.loadConfig();
			} catch (Exception e) {
				GameConfig.addErrorConfig(new ExcleError(config.getDownloadFile(), ExceptionUtil.stacktraceToString(e)));
				log.error("加载失败",e);
				e.printStackTrace();
			}
		}
	}
}
