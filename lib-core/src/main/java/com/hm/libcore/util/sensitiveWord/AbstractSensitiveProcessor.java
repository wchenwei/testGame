package com.hm.libcore.util.sensitiveWord;

import java.util.Set;

/**
 * @Description: 抽象敏感字过滤器
 * @author siyunlong  
 * @date 2020年11月16日 下午3:20:33 
 * @version V1.0
 */
public abstract class AbstractSensitiveProcessor {
	//加载敏感字库
	public abstract void loadWord(Set<String> sensitiveWordSet);
	//加载敏感字库
	protected abstract void loadWord(Set<String> sensitiveWordSet, boolean isReload);
	protected abstract void loadWord(Set<String> sensitiveWordSet, boolean isReload,boolean checkPinyin);

	//替换敏感字
	public abstract String replaceSensitiveWord(String txt, String replaceStr);
	//判断是否包含敏感字
	public abstract boolean contains(String txt);
	//获取文本中包含的敏感字
	protected abstract Set<String> getSensitiveWord(String txt);
	protected abstract String correctContent(String txt);
	
}
