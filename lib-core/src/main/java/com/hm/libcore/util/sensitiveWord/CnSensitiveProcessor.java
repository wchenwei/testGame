package com.hm.libcore.util.sensitiveWord;

import com.hm.libcore.util.SensitiveWordHelper;

import java.util.Set;


/**
 * @Description: 中文敏感字处理器
 * @author siyunlong  
 * @date 2020年11月16日 下午3:22:51 
 * @version V1.0
 */
public class CnSensitiveProcessor extends AbstractSensitiveProcessor {
    private SensitiveWordHelper helper = new SensitiveWordHelper();
	@Override
	public void loadWord(Set<String> sensitiveWordSet) {
        helper.add(sensitiveWordSet);
	}
	@Override
	protected void loadWord(Set<String> sensitiveWordSet, boolean isReload) {
        helper.add(sensitiveWordSet,isReload);
	}

    @Override
    protected void loadWord(Set<String> sensitiveWordSet, boolean isReload, boolean checkPinyin) {
        helper.add(sensitiveWordSet,isReload,checkPinyin);
    }

    @Override
	public String replaceSensitiveWord(String txt, String replaceStr) {
		return helper.replaceSensitiveWord(txt, replaceStr);
	}

	@Override
	public boolean contains(String txt) {
		return helper.contains(txt);
	}

	@Override
	protected Set<String> getSensitiveWord(String txt) {
		return helper.getSensitiveWord(txt);
	}
	@Override
	protected String correctContent(String txt) {
		return txt.replace(" ", "").replace("&", "");
	}
	
}
