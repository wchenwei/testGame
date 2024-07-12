package com.hm.db;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.MongodDB;
import com.hm.libcore.util.sensitiveWord.sensitive.SysWordSensitiveUtil;
import org.springframework.data.mongodb.core.query.Query;

import com.google.common.collect.Sets;
import com.hm.model.WordfilterEntity;

/**
 * ClassName: WordFilterUtils. <br/>
 * Function: 敏感，关键词过滤. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2018年5月23日 下午5:48:01 <br/>
 *
 * @author zxj
 */
//关键词过滤的操作工具类
public class WordFilterUtils extends CommonDbUtil<Object> {
    //删除关键词
    public static boolean delWordFilter(String[] strArr) {
        return WordFilterUtils.delWordFilter(stringToInts(strArr));
    }

    public static boolean delWordFilter(Integer[] intStr) {
        MongodDB mongo = ChatMongoUtils.getChatMongoDB();
        return mongo.deleteBatchIds(Arrays.asList(intStr), WordfilterEntity.class);
    }

    public static List<WordfilterEntity> getWordfilterList() {
        MongodDB mongo = ChatMongoUtils.getChatMongoDB();
        return mongo.query(new Query(), WordfilterEntity.class);
    }

    //str数组转integer数组
    private static Integer[] stringToInts(String[] s) {
        Integer[] n = new Integer[s.length];
        for (int i = 0; i < s.length; i++) {
            n[i] = Integer.parseInt(s[i]);
        }
        return n;
    }

    //根据关键词bean，获取id数组
    public static Integer[] getIdArr(List<WordfilterEntity> wordFilterList) {
        Integer[] intArr = new Integer[wordFilterList.size()];
        for (int i = 0; i < wordFilterList.size(); i++) {
            intArr[i] = wordFilterList.get(i).getId();
        }
        return intArr;
    }

    //加到内存里
    public static void addWordsToMap(List<WordfilterEntity> wordFilterList) {
        Set<String> wordSet = Sets.newHashSet();
        for (WordfilterEntity word : wordFilterList) {
            wordSet.add(word.getContent());
        }
        SysWordSensitiveUtil.getInstance().loadWord(wordSet, false);
    }

}








