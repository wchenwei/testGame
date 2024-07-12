package com.hm.util;

import cn.hutool.core.util.StrUtil;
import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.libcore.util.SensitiveWordUtil;

import java.util.regex.Pattern;

/**
 * @Description: 检查昵称是否合法
 * @author siyunlong  
 * @date 2019年9月14日 下午9:29:39 
 * @version V1.0
 */
public class NameUtils {
	public static Pattern CHINESE_XINJIANG_PATTERN =
			Pattern.compile("^[!~=&*#·:;\\[\\]_\\-<>.a-z0-9A-Z\u4e00-\u9fa5]+$");
	
	public static boolean isFitName(String name) {
		try {
			if(StrUtil.isEmpty(name)
					|| !CHINESE_XINJIANG_PATTERN.matcher(name).matches()
					|| SensitiveWordUtil.contains(name)) {
				return false;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	public static String nameClearBlank(String name) {
		int language = ServerConfig.getInstance().getLanguage();
		if(language == 1) {
			return StrUtil.cleanBlank(name);
		}else {
			return StrUtil.trim(name);
		}
	}
	
	public static void main(String[] args) {
		String name = "寄风·肖";
		System.err.println(CHINESE_XINJIANG_PATTERN.matcher(name).matches());
	}
}
