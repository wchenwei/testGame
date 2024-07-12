package com.hm.libcore.language;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 语言本地化模板
 *
 * @author xjt
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LanguageVo {
    private int type;//0-正常显示 1-languageId 2-可点击的坐标
    private String[] content;

    public static LanguageVo createStr(Object... v) {
        return new LanguageVo(LanConfType.NORMAL.getType(), change(v));
    }

    public static LanguageVo createLan(String... lan) {
        return new LanguageVo(LanConfType.CONFIG.getType(), lan);
    }

    public static LanguageVo createTypeLan(LanConfType lanConfType, String... lan) {
        return new LanguageVo(lanConfType.getType(), lan);
    }

    public static LanguageVo[] createStrArrays(Object... params) {
        LanguageVo[] results = new LanguageVo[params.length];
        for (int i = 0; i < params.length; i++) {
            results[i] = createStr(params[i]);
        }
        return results;
    }

    public static LanguageVo[] createLanArrays(String... params) {
        LanguageVo[] results = new LanguageVo[params.length];
        for (int i = 0; i < params.length; i++) {
            results[i] = createLan(params[i]);
        }
        return results;
    }

    public static LanguageVo[] mergeLanguageVo(LanguageVo[] first, LanguageVo[] next) {
        LanguageVo[] result = new LanguageVo[first.length + next.length];
        for (int i = 0; i < first.length; i++) {
            result[i] = first[i];
        }
        for (int i = 0; i < next.length; i++) {
            result[first.length + i] = next[i];
        }
        return result;
    }

    public static String[] change(Object... v) {
        String[] strs = new String[v.length];
        for (int i = 0; i < v.length; i++) {
            strs[i] = v[0].toString();
        }
        return strs;
    }
}
