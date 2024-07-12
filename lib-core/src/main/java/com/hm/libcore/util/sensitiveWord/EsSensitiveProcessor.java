package com.hm.libcore.util.sensitiveWord;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description: 英文敏感字处理器
 * @author siyunlong  
 * @date 2020年11月16日 下午3:22:51 
 * @version V1.0
 */
public class EsSensitiveProcessor extends AbstractSensitiveProcessor {
	private Joiner joiner = Joiner.on(" ");
	private List<String> wordList = Lists.newArrayList();
	
	@Override
	public void loadWord(Set<String> sensitiveWordSet) {
		this.wordList.addAll(sensitiveWordSet);
	}

	@Override
	protected void loadWord(Set<String> sensitiveWordSet, boolean isReload) {
		if(isReload) {
			this.wordList.clear();
		}
		loadWord(sensitiveWordSet);
	}

    @Override
    protected void loadWord(Set<String> sensitiveWordSet, boolean isReload, boolean checkPinyin) {
        loadWord(sensitiveWordSet,isReload);
	}

    @Override
	public String replaceSensitiveWord(String txt, String replaceStr) {
		boolean isChange = false;
		String[] trems = txt.split(" ");
		for (int i = 0; i < trems.length; i++) {
			if(isFitWord(trems[i])) {
				trems[i] = replaceStr;
				isChange = true;
			}
		}
		if(!isChange) {
			return txt;
		}
		return joiner.join(trems);
	}

	@Override
	public boolean contains(String txt) {
		String[] trems = txt.split(" ");
		for (String word : trems) {
			if(isFitWord(word)) {
				return true;
			}
		}
		return false;
	}
	
	
	public boolean isFitWord(String word) {
		word = word.toLowerCase();
		for (String line : wordList) {
			if(line.equals(word)) {
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		EsSensitiveProcessor esSensitive = new EsSensitiveProcessor();
		esSensitive.loadWord(Sets.newHashSet("fuck","es","good"));
		System.err.println(esSensitive.replaceSensitiveWord("Fucker FUck  ms", "*"));
		System.err.println(esSensitive.contains("funck Fuck  ms"));
	}

	@Override
	protected Set<String> getSensitiveWord(String txt) {
		Set<String> badWords = new HashSet<String>(); 
		String[] trems = txt.split(" ");
		for (String word : trems) {
			if(isFitWord(word)) {
				badWords.add(word);
			}
		}
		return badWords;
	}
	
	@Override
	protected String correctContent(String txt) {
		return txt.trim();
	}
}

