package com.hm.observer;

/**
 * @author xjt
 * @version V1.0
 * @Description: 观察者排序
 * @date 2018年5月24日 下午3:13:09
 */
public class SortMethodObserver {
    private int index;
    private String methodName;
    private int sort;

    public SortMethodObserver(int index, String methodName, int sort) {
        this.index = index;
        this.methodName = methodName;
        this.sort = sort;
    }

    public String getMethodName() {
        return methodName;
    }

    public int getIndex() {
        return index;
    }

    public int getSort() {
        return sort;
    }

}
