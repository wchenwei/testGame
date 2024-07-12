/**  
 * Project Name:SLG_ChatHot.  
 * File Name:SensitiveWordUtil.java  
 * Package Name:com.hm.utils  
 * Date:2018年5月2日下午2:36:35  
 * Copyright (c) 2018, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
*/  

package com.hm.libcore.util;

import com.google.common.collect.Maps;
import com.hm.libcore.util.sensitiveWord.SensitiveWordInit;
import com.hm.libcore.util.sensitiveWord.SensitivewordFilterUtil;
import com.hm.libcore.util.sensitiveWord.py.HypyShieldWorldUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * 敏感词处理工具 - IKAnalyzer中文分词工具 - 借助分词进行敏感词过滤
 * ClassName:SensitiveWordUtil <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason:   TODO ADD REASON. <br/>  
 * Date:     2018年5月2日 下午2:36:35 <br/>  
 * @author   Administrator  
 * @version  1.1  
 * @since    
 */

public class SensitiveWordHelper {

	@SuppressWarnings("rawtypes")
	private Map sensitiveWordMap = null;
	public int minMatchTYpe = 1;      //最小匹配规则
	public int maxMatchType = 2;      //最大匹配规则
	public void loadKeyWord(Set<String> keyWordSet){
		sensitiveWordMap = new SensitiveWordInit().initKeyWord(keyWordSet);
	}

    /**
     * 初始化敏感词库
     *
     * @param sensitiveWordSet 敏感词库
     */
    public synchronized void add(Set<String> sensitiveWordSet) {
    	sensitiveWordMap = new SensitiveWordInit().initKeyWord(sensitiveWordSet);
    	HypyShieldWorldUtils.loadShieldWorld(sensitiveWordSet);
    }
    
    /**
     * 初始化敏感词库
     *
     * @param sensitiveWordSet 敏感词库
     */
    public synchronized void add(Set<String> sensitiveWordSet,boolean reload) {
    	if(reload){
    		sensitiveWordMap = new SensitiveWordInit().initKeyWord(sensitiveWordSet);
    		HypyShieldWorldUtils.loadShieldWorld(sensitiveWordSet,reload);
    		return;
    	}
    	if(null==sensitiveWordMap) {
    		sensitiveWordMap=Maps.newConcurrentMap();
    	}
    	Map tempMap = new SensitiveWordInit().initKeyWord(sensitiveWordSet);
    	sensitiveWordMap.putAll(tempMap);
    	HypyShieldWorldUtils.loadShieldWorld(sensitiveWordSet,reload);
    }


    public synchronized void add(Set<String> sensitiveWordSet,boolean reload,boolean checkPinyin) {
        if(reload){
            sensitiveWordMap = new SensitiveWordInit().initKeyWord(sensitiveWordSet);
            if(checkPinyin){
                HypyShieldWorldUtils.loadShieldWorld(sensitiveWordSet,reload);
            }
            return;
        }
        if(null==sensitiveWordMap) {
            sensitiveWordMap=Maps.newConcurrentMap();
        }
        Map tempMap = new SensitiveWordInit().initKeyWord(sensitiveWordSet);
        sensitiveWordMap.putAll(tempMap);
        if(checkPinyin){
            HypyShieldWorldUtils.loadShieldWorld(sensitiveWordSet,reload);
        }
    }
    
    
    public synchronized void add(String sensitiveWord) {
           sensitiveWordMap.put(sensitiveWord, sensitiveWord);
    }
    
    public synchronized void del(String[] words){
    	for(String word:words){
    		sensitiveWordMap.remove(word);
    	}
    }
    
    public synchronized void del(String word){
    	sensitiveWordMap.remove(word);
    }

    /**
     * 判断文字是否包含敏感字符
     *
     * @param txt 文字
     * @return 若包含返回true，否则返回false
     */
	public boolean contains(String txt) {
		for(int i = 0 ; i < txt.length() ; i++){
			int matchFlag = CheckSensitiveWord(txt, i, maxMatchType); //判断是否包含敏感字符
			if(matchFlag > 0){    //大于0存在，返回true
				return true;
			}
		}
		return HypyShieldWorldUtils.checkContainShield(txt);
	}
	/**
	 * 检查文字中是否包含敏感字符，检查规则如下：<br>
	 * @author chenssy 
	 * @date 2014年4月20日 下午4:31:03
	 * @param txt
	 * @param beginIndex
	 * @param matchType
	 * @return，如果存在，则返回敏感词字符的长度，不存在返回0
	 * @version 1.0
	 */
	public int CheckSensitiveWord(String txt,int beginIndex,int matchType){
		boolean  flag = false;    //敏感词结束标识位：用于敏感词只有1位的情况
		int matchFlag = 0;     //匹配标识数默认为0
		char word = 0;
		Map nowMap = sensitiveWordMap;
		for(int i = beginIndex; i < txt.length() ; i++){
			word = txt.charAt(i);
			nowMap = (Map) nowMap.get(word);     //获取指定key
			if(nowMap != null){     //存在，则判断是否为最后一个
				matchFlag++;     //找到相应key，匹配标识+1 
				if("1".equals(nowMap.get("isEnd"))){       //如果为最后一个匹配规则,结束循环，返回匹配标识数
					flag = true;       //结束标志位为true   
					if(SensitivewordFilterUtil.minMatchTYpe == matchType){    //最小规则，直接返回,最大规则还需继续查找
						break;
					}
				}
			}
			else{     //不存在，直接返回
				break;
			}
		}
		if(matchFlag < 1 || !flag){        //长度必须大于等于1，为词
			matchFlag = 0;
		}
		return matchFlag;
	}

    /**
     * 获取文字中的敏感词
     *
     * @param txt 文字
     * @return
     */
    public Set<String> getSensitiveWord(String txt){
    	Set<String> sensitiveWordList = new HashSet<String>();
		
		for(int i = 0 ; i < txt.length() ; i++){
			int length = CheckSensitiveWord(txt, i, maxMatchType);    //判断是否包含敏感字符
			if(length > 0){    //存在,加入list中
				sensitiveWordList.add(txt.substring(i, i+length));
				i = i + length - 1;    //减1的原因，是因为for会自增
			}
		}
		
		return sensitiveWordList;
    }

    /**
     * 替换敏感字字符
     *
     * @param txt         文本
     * @param replaceChar 替换的字符，匹配的敏感词以字符逐个替换，如 语句：我爱中国人 敏感词：中国人，替换字符：*， 替换结果：我爱***
     * @return
     */
    /*public static String replaceSensitiveWord(String txt, char replaceChar) {
    	String resultTxt = txt;
        //获取所有的敏感词
        Set<String> sensitiveWordList = getSensitiveWord(txt);
        String replaceString;
        for (String sensitiveWord : sensitiveWordList) {
            replaceString = getReplaceChars(replaceChar, sensitiveWord.length());
            resultTxt = resultTxt.replaceAll(sensitiveWord, replaceString);
        }
        return resultTxt;
    }*/

    /**
     * 替换敏感字字符
     *
     * @param txt        文本
     * @param replaceStr 替换的字符串，匹配的敏感词以字符逐个替换，如 语句：我爱中国人 敏感词：中国人，替换字符串：[屏蔽]，替换结果：我爱[屏蔽]
     * @return
     */
    public String replaceSensitiveWord(String txt, String replaceStr) {
    	String resultTxt = txt;
        //获取所有的敏感词
        Set<String> sensitiveWordList = getSensitiveWord(txt);
        String replaceString;
        for (String sensitiveWord : sensitiveWordList) {
            replaceString = getReplaceChars(replaceStr, sensitiveWord.length());
            resultTxt = resultTxt.replaceAll(sensitiveWord, replaceString);
        }
        return HypyShieldWorldUtils.checkReplaceShield(resultTxt, replaceStr);
    }

    /**
     * 获取替换字符串
     *
     * @param replaceChar
     * @param length
     * @return
     */
    private String getReplaceChars(String replaceChar,int length){
		String resultReplace = replaceChar;
		for(int i = 1 ; i < length ; i++){
			resultReplace += replaceChar;
		}
		
		return resultReplace;
	}

}

