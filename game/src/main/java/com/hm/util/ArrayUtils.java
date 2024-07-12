package com.hm.util;

import cn.hutool.core.util.ArrayUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author chenwei
 * @Date 2024/5/15
 * @Description:
 */
public class ArrayUtils extends ArrayUtil {

    // 数组是否为空或者全部为0
    public static boolean isEmptyOrAllZero(int [] arrays){
        if (isEmpty(arrays)){
            return true;
        }
        return Arrays.stream(arrays).anyMatch(e -> e == 0);
    }

    // 数组中是否有不等于0的重复元素
    public static boolean haveDupElement(int [] arrays){
        List<Integer> list = Arrays.stream(arrays)
                .filter(e -> e != 0).boxed().collect(Collectors.toList());
        return list.stream()
                .distinct()
                .count() != list.size();
    }


    public static void main(String[] args) {
        int [] arr = new int[]{1,2,0,0};
        System.err.println(haveDupElement(arr));
    }
}
