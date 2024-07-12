package com.hm.libcore.util.sensitiveWord;

import java.util.Set;

/**
 * @Description: 敏感字代理处理器
 * @author siyunlong  
 * @date 2020年11月16日 下午3:25:25 
 * @version V1.0
 */
public class SensitiveProxy {
	private AbstractSensitiveProcessor sensitiveProcessor = new CnSensitiveProcessor();
	public void setSensitiveProcessor(AbstractSensitiveProcessor newSensitiveProcessor) {
		sensitiveProcessor = newSensitiveProcessor;
	}
	
	public void loadWord(Set<String> sensitiveWordSet) {
		sensitiveProcessor.loadWord(sensitiveWordSet);
	}
	
	public void loadWord(Set<String> sensitiveWordSet,boolean isReload) {
		sensitiveProcessor.loadWord(sensitiveWordSet,isReload);
	}
    public void loadWord(Set<String> sensitiveWordSet,boolean isReload,boolean checkPinyin) {
        sensitiveProcessor.loadWord(sensitiveWordSet,isReload,checkPinyin);
    }

	public String replaceSensitiveWord(String txt, String replaceStr) {
		return sensitiveProcessor.replaceSensitiveWord(sensitiveProcessor.correctContent(txt), replaceStr);
	}

	public boolean contains(String txt) {
		return sensitiveProcessor.contains(txt);
	}
	public Set<String> getSensitiveWord(String txt) {
		return sensitiveProcessor.getSensitiveWord(txt);
	}
	
}
