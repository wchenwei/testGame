package com.hm.util;

import com.google.common.collect.Lists;

import java.util.List;

public class CollUtils {
    //合并有交集的集合
    public static List<List<Integer>> checkColltonIntersection (List<List<Integer>> lists) {
        List<List<Integer>> list = Lists.newArrayList(); // 定义一个接收结果的空大集合B
        for (int x = 0; x < lists.size(); x++) { // 大集合A，遍历自己里面的的小集合
            int n = 0; // AB俩集合第一次出现相同项时，B集合索引位置
            int a = 0; // AB最后一次次出现相同项时，B集合的索引位置
            int m = 0; // 统计AB集合在A的某个小集和在B集合整体遍历出现相同项的次数
            boolean isB = true; // 开关 ， 控制两个集合中的元素是否至少有一次相同，有就记录为false，用于 ：如果没有相同项就把A集合当前的小集和体添加到B集合的最后索引位置（合并的是在大集合B追加A的小集合）
            boolean isBoolean = true; // 定义开关 ， 如果只要小集合里出现一次有相同的就为false，用于：如果出现相同，就把A集当前存在同类项的小集合于B集合中对比的小集合的元素合并同类项，（合并的是俩 者小集合内元素）
            for (int i = 0; i < list.size(); i++) { // 遍历大集合B
                isBoolean = true; // 定义开关 ， 如果只要集合里出现一次有相同的就为false
                for (int j = 0; j < list.get(i).size(); j++) { // 大集合B里的小集合遍历元素
                    for (int y = 0; y < lists.get(x).size(); y++) { // A集合里的小集合遍历元素
                        // System.out.println("x"+x+"y"+y+"i"+i+"j"+j);
                        if (lists.get(x).get(y).equals(list.get(i).get(j))) { // 比较俩小集合元素是否相等
                            // 删除大集合A的当前小集合中与大集合B相等的元素
                            // System.out.println("sc" + lists.get(x).get(y));
                            lists.get(x).remove(y);
                            y--; // 删除后，需要下标前移
                            isBoolean = false; // 只要出现同类项,就为false，说明俩集合是有同类项的
                            isB = false; // 只要出现一次,就为false，说明俩集合是有同类项的，那么大集合A的当前小集合不能整体添加到到集合B，只能合并同类项元素
                            // 如果是第一次出现同类项进去
                            if (m == 0) {
                                // 记录第一次出现在，大集合B的那个小集合索引位置
                                n = i;
                            }
                            a = i;// 最后一次出现相同项，AB最后一次次出现相同项时，B集合的索引位置
                            m++; // 统计AB集合在A的某个小集和在B集合整体遍历出现相同项的次数
                            if (m > 1 && a != n) { // 相同项出现多次且两次出现在大集合B位置不一样，那么进入
                                // 把大集合B中存在相同项的在第二次小集和出现的相同的元素删除
                                // System.out.println("相同项删除"+list.get(i).get(j));
                                list.get(i).remove(j);
                                // 大元素B的本小集和的元素长度减一，下标集体向前移动
                                if (j < 0) {
                                    break;
                                } else {
                                    j--;
                                    break;
                                }
                            }
                        } else {
                        }
                    }
                }
                // isBoolean == false 时，说明存在于大集合B有相同项的小集合，需要合并
                if (isBoolean == false) { // 集合B本身被第三者连理存在三者之间有相同项需要合并
                    if (m > 1 && a != n) { // 大集合相同项出现多次且两次出现在大集合位置不一样，那么进入（说明大集合B中存在俩个小集合有同类项，需要合并）
                        // 合并大集合中具有相同项的两个小集和
                        for (int l = 0; l < list.get(a).size(); l++) {
                            // System.out.println(""+list.get(a).get(l));
                            list.get(n).add(list.get(a).get(l));
                        }
                        // 把相同的小集合删除
                        list.remove(a);
                        // 大集合的小集和索引集体向前移动，那么就需要把索引向前移
                        i--;
                    } else {// 集合A与集合B存在相同项
                        // 把大集合A的小集合的其他项添加到大集合B相对应的这项当中
                        for (int l = 0; l < lists.get(x).size(); l++) {
                            list.get(i).add(lists.get(x).get(l));
                        }
                    }
                }
            }
            // isBoolean == true 时，说明俩个集合没有同类项 可以吧大集合A的小集合添加到大集合B内
            if (isB == true) {
                // 直接在大集合B后面添加小集合
                list.add(lists.get(x));
            }
            // 处理小集合里的元素乱序 排序为 a,b,c,d
            //System.out.println("第" + (x + 1) + "次集合合并项结果：" + list);
        }
        return list;
    }
}
