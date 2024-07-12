package com.hm.enums;

import com.hm.config.excel.LevelChoiceMode;
import com.hm.config.excel.temlate.CommonValueTemplate;
import com.hm.util.ItemUtils;
import com.hm.util.StringUtil;
import lombok.Getter;

import java.util.Arrays;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * 常量表2字段类型
 * @Author ljfeng
 * @Date 2023-01-05
 **/
public enum CommValueV2Type {
    None(0) { //服务端都不用类型
        @Override
        public Object parseValue(CommonValueTemplate template) {
            return null;
        }
    },
    INTEGER(1) { //取value值
        @Override
        public Object parseValue(CommonValueTemplate template) {
            return template.getValue().intValue();
        }
    },
    STRING(2) { //字符串
        @Override
        public Object parseValue(CommonValueTemplate template) {
            return template.getPara();
        }
    },
    LIST_INT(3) { //list<Integer>类型
        @Override
        public Object parseValue(CommonValueTemplate template) {
            return StringUtil.splitStr2IntegerList(template.getPara(), SplitType.SPLIT4);
        }
    },
    LIST_STRING(4) { //list<String>类型
        @Override
        public Object parseValue(CommonValueTemplate template) {
            return StringUtil.splitStr2StrList(template.getPara(), SplitType.SPLIT4);
        }
    },
    LIST_ITEM(5) { //list<Items>类型
        @Override
        public Object parseValue(CommonValueTemplate template) {
            return ItemUtils.str2DefaultItemList(template.getPara());
        }
    },
    MAP_INT_INT(6) { //map<Integer,Integer>类型
        @Override
        public Object parseValue(CommonValueTemplate template) {
            return StringUtil.strToMap(template.getPara(), SplitType.SPLIT4, SplitType.SPLIT1);
        }
    },
    MAP_INT_LONG(7) { //map<Integer,Long>类型
        @Override
        public Object parseValue(CommonValueTemplate template) {
            return StringUtil.strToMapLong(template.getPara());
        }
    },
    DOUBLE(8) { //double类型
        @Override
        public Object parseValue(CommonValueTemplate template) {
            return Double.valueOf(template.getPara());
        }
    },
    TREE_MAP_INT_LONG(9) { //tree<Integer,Long>类型
        @Override
        public Object parseValue(CommonValueTemplate template) {
            return new TreeMap<>(StringUtil.strToMapLong(template.getPara()));
        }
    },
    LIST_LIST_INT(10) { //List<List<Integer>>类型
        @Override
        public Object parseValue(CommonValueTemplate template) {
            return Arrays.stream(template.getPara().split(SplitType.SPLIT5))
                .map(s -> StringUtil.splitStr2IntegerList(s, SplitType.SPLIT4))
                .collect(Collectors.toList());
        }
    },
    LIST_DOUBLE(11) { //list<Double>类型
        @Override
        public Object parseValue(CommonValueTemplate template) {
            return StringUtil.splitStr2DoubleList(template.getPara(), SplitType.SPLIT4);
        }
    },
    Array_Int(12) { //int[]类型
        @Override
        public Object parseValue(CommonValueTemplate template) {
            return StringUtil.splitStr2IntArray(template.getPara(), SplitType.SPLIT4);
        }
    },

    Array_Double(13) { //double[]类型
        @Override
        public Object parseValue(CommonValueTemplate template) {
            return StringUtil.splitStr2DoubleArray(template.getPara(), SplitType.SPLIT4);
        }
    },
    MAP_INT_Double(14) { //map<Integer,Double>类型
        @Override
        public Object parseValue(CommonValueTemplate template) {
            return StringUtil.strToIntDoubleMap(template.getPara(),SplitType.SPLIT4,SplitType.SPLIT1);
        }
    },
    LIST_ITEM2(15) { //list<Items>类型
        @Override
        public Object parseValue(CommonValueTemplate template) {
            return ItemUtils.str2ItemList(template.getPara(),";",":");
        }
    },
    LevelChoiceMode(16) { //
        @Override
        public Object parseValue(CommonValueTemplate template) {
            return new LevelChoiceMode(template.getPara());
        }
    },

    Array_String(17) { //String[]类型
        @Override
        public Object parseValue(CommonValueTemplate template) {
            return template.getPara().split(SplitType.SPLIT4);
        }
    },
    ;

    CommValueV2Type(int type) {
        this.type = type;
    }

    @Getter
    private int type;

    public static CommValueV2Type getType(int type) {
        for (CommValueV2Type value : values()) {
            if (value.type == type) {
                return value;
            }
        }
        return null;
    }

    public abstract Object parseValue(CommonValueTemplate template);
}
