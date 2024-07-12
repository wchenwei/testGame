package com.hm.libcore.util.sensitiveWord.py;

import cn.hutool.core.util.StrUtil;

/**
 * @Description: 屏蔽字单元
 * @author siyunlong  
 * @date 2020年11月16日 上午11:58:23 
 * @version V1.0
 */
public class ShieldWord {
	private String source;//原始屏蔽字
	private String py;//转换成拼音之后的屏蔽字
	private String[] sourArray;
	
	public ShieldWord(String source) {
		this.source = source.trim();
		this.py = HanyuPinyinHelper.toHanyuPinyin(this.source);
		this.sourArray = toStrArray();
	}
	
	public boolean isContains(String info) {
		return info.contains(py);
	}
	
	public String replaceTxt(String sourceTxt,String replacement) {
		for (String oneStr : sourArray) {
			sourceTxt = sourceTxt.replaceAll(oneStr, replacement);
		}
		return sourceTxt;
	}
	
	public String[] toStrArray() {
		char[] sscs = this.source.toCharArray();
		String[] strArr = new String[sscs.length];
		for (int i = 0; i < strArr.length; i++) {
			strArr[i] = String.valueOf(sscs[i]);
		}
		return strArr;
	}
	
	public boolean isFit() {
		return StrUtil.isNotEmpty(py) && py.length() > 1 && source.length()>1;
	}

	public String getSource() {
		return source;
	}
	
}
