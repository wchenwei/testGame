package com.hm.libcore.util.sensitiveWord.py;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: 利用汉语转换成汉语拼音处理屏蔽字
 * @author siyunlong  
 * @date 2020年11月16日 下午1:46:51 
 * @version V1.0
 */
public class HypyShieldWorldUtils {
	public static boolean isClose = false;
	private static List<ShieldWord> worldList = Lists.newArrayList();
	
	public static void loadShieldWorld(Collection<String> words) {
		for(String word:words){
            Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]{0,}$");
            Matcher matcher= pattern.matcher(word);
		    //只有全为中文才有必要检验匹配音节
		    if(matcher.matches()){
                ShieldWord shieldWord = new ShieldWord(word);
                if(shieldWord.isFit()) {
                    worldList.add(shieldWord);
                }
            }
		}
	}
	
	public static void loadShieldWorld(Collection<String> words,boolean isReload) {
		if(isReload) {
			worldList.clear();
		}
		loadShieldWorld(words);
	}
	
	/**
	 * 检查内容是否包含屏蔽字库
	 * @param content
	 * @return
	 */
	public static boolean checkContainShield(String content) {
		if(isClose) {
			return false;
		}
		content = HanyuPinyinHelper.toPyOnlyHz(content);
		for (ShieldWord shieldWord : worldList) {
			if(shieldWord.isContains(content)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 替换内容中的屏蔽字为指定字符
	 * @param sourceTxt 原字符
	 * @param replacement 替换的字符
	 * @return
	 */
	public static String checkReplaceShield(String sourceTxt,String replacement) {
		if(isClose) {
			return sourceTxt;
		}
		String infoTxt = HanyuPinyinHelper.toPyOnlyHz(sourceTxt);
		for (ShieldWord shieldWord : worldList) {
			if(shieldWord.isContains(infoTxt)) {
				sourceTxt = shieldWord.replaceTxt(sourceTxt, replacement);
				infoTxt = HanyuPinyinHelper.toPyOnlyHz(sourceTxt);
			}
		}
		return sourceTxt;
	}
	
	public static List<ShieldWord> getWorldList() {
		return worldList;
	}

	public static void main(String[] args) {
		HypyShieldWorldUtils.loadShieldWorld(Lists.newArrayList("刁近平","习近平","毛泽东"));
		long now = System.currentTimeMillis();
		System.err.println(checkReplaceShield("刁1近2!#平,毛s泽s东,我爱你毛泽东","*"));
		System.err.println(checkContainShield("刁1近2!#平,毛s泽s东,我爱你毛泽东"));
		System.err.println(System.currentTimeMillis()-now);
	}
}
