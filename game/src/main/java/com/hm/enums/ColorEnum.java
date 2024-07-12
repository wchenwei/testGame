package com.hm.enums;

import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.CommValueConfig;
import com.hm.util.CellUtils;

import java.util.List;

/**
 * @author wyp
 * @description
 * @date 2021/4/19 15:14
 */
public enum ColorEnum {
    RED(1, "红色"),
    BLUE(2, "蓝色"),
    GREEN(3, "绿色"),
    yellow(4, "黄色"),
    purple(5, "紫色"),
    white(6, "白色"){
        @Override
        public double weightRate(List<Integer> cellColors, boolean isIntervalTwo, int y) {
            if(!isCanParticipation(y)){
                return 0;
            }
            if(!CellUtils.isCanWhite(y)){
                return 0;
            }
            return super.weightRate(cellColors, isIntervalTwo, y);
        }
    },
    ;

    private ColorEnum(int type, String desc){
        this.type = type;
        this.desc = desc;
    }

    private int type;

    private String desc;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * @description
     * @param cellColors 格子集合  左边 、下边
     * @param isIntervalTwo  间隔两个格子
     * @param y  层数
     * @return int
     * @author wyp
     * @date 2021/4/19 15:33
     */
    public double weightRate(List<Integer> cellColors, boolean isIntervalTwo, int y){
        boolean contains = cellColors.contains(this.getType());
        if(isIntervalTwo && contains){
            return 0;
        }
        if(!isIntervalTwo && contains){
            double rate = 10;
            CommValueConfig config = SpringUtil.getBean(CommValueConfig.class);
            int commValue = config.getCommValue(CommonValueType.ArmyPressBorderRare);
            int value = commValue == 0 ? 5 : commValue;
            return value / rate;
        }
        return 1;
    }

    /**
     * @description
     *          读取策划配置
     * @param y  层数
     * @return int
     * @date 2021/4/19 17:52
     */
    public int getWeight(int y){
        CommValueConfig config = SpringUtil.getBean(CommValueConfig.class);
        // 读取配置
        boolean flag = isCanParticipation(y);
        //根据flag 取出权重库

        return config.getColorWeight(flag, this.getType());
    }

    /**
     * @description
     *      计算是否是   4-6  的倍数
     * @param y
     * @return boolean
     * @author wyp
     * @date 2021/4/20 9:41
     */
    private static boolean isCanParticipation(int y){
        return y / 3 % 2 == 1;
    }
}
