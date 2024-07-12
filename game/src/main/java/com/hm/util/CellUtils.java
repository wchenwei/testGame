package com.hm.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.ArmyPressBorderConfig;
import com.hm.config.excel.CommValueConfig;
import com.hm.enums.ColorEnum;
import com.hm.enums.CommonValueType;
import com.hm.model.battle.xiaochu.Cell;
import com.hm.model.serverpublic.ServerData;
import com.hm.model.serverpublic.ServerDataManager;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author wyp
 * @description
 * @date 2021/4/19 17:34
 */
public class CellUtils {
    // 生成数据 最大层数
    public static final Integer maxIndex = 1000;
    // 权重数据
    private static Map<Integer, Integer> weightMap = Maps.newConcurrentMap();
    // 格子实体数据
    private static List<Cell> cellEntityList = Lists.newArrayList();

    private static List<Integer> npcLvs = Lists.newArrayList();
    private static List<Integer> scores = Lists.newArrayList();
    /**
     * @description
     *          生成数据
     * @param index 需要生成的层级数
     * @param score 1级的军功数
     * @param baseNpcLv 1级调用的npc等级
     * @return java.util.List<com.hm.model.ceshi.Cell>
     * @author wyp
     * @date 2021/4/19 17:12
     */
    public static Map<Integer, Cell> generateList(int index, int score, int baseNpcLv){
        try {
            getCommValue();
            for (int y = 0; y < index; y++) {
                for (int x = 0; x < 7; x++) {
                    Cell cell = new Cell(x, y);
                    getColorEnum(x, y);
                    Integer random = RandomUtils.buildWeightMeta(weightMap).random();
                    cell.setColorId(random);
                    cell.setScore(getScore(y, score));
                    cell.setNpcId(getNpcId(y, baseNpcLv, random));
                    cellEntityList.add(cell);
                    System.out.print(cell.getColorId() + " ");
                }
                System.out.println();
            }
            Map<Integer, Cell> collect = cellEntityList.stream().collect(Collectors.toMap(Cell::getId, Function.identity()));
            return collect;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cellEntityList.clear();
            weightMap.clear();
            npcLvs.clear();
            scores.clear();
        }
        return Maps.newConcurrentMap();
    }

    private static void getCommValue() {
        CommValueConfig config = SpringUtil.getBean(CommValueConfig.class);
        int[] values = config.getCommonValueByInts(CommonValueType.ArmyPressBorderNpcLv);
        int[] score = config.getCommonValueByInts(CommonValueType.ArmyPressBorderScore);
        npcLvs = Arrays.stream(values).boxed().collect(Collectors.toList());
        scores = Arrays.stream(score).boxed().collect(Collectors.toList());
    }

    private static int getNpcLv(int y, int baseNpcLv){
        return Math.min(npcLvs.get(3), (y / npcLvs.get(0) + 1) * npcLvs.get(1) + npcLvs.get(2));
    }

    private static int getNpcId(int y, int baseNpcLv,int colorId){
        int lv = getNpcLv(y,baseNpcLv);
        if(colorId==ColorEnum.white.getType()){
            return 0;
        }
        ArmyPressBorderConfig config = SpringUtil.getBean(ArmyPressBorderConfig.class);
        //从配置文件选取npcId
        return config.getRandomNpcId(colorId,lv);
    }

    private static int getScore(int y, int score){
        return Math.min(scores.get(3), (y / scores.get(0) + 1) * scores.get(1) + scores.get(2));
    }

    public static void getColorEnum(int x, int y){
        weightMap.clear();
        getColor(x, y, false);
        getColor(x, y, true);
    }

    public static void getColor(int x, int y, boolean isIntervalTwo) {
        List<Integer> cellColors = getCell(x, y, isIntervalTwo);
        for(ColorEnum colorEnum : ColorEnum.values()){
            double v = colorEnum.weightRate(cellColors, isIntervalTwo, y);
            Double weight = colorEnum.getWeight(y) * v;
            if(v == 0){
                weightMap.put(colorEnum.getType(), weight.intValue());
                continue;
            } else if (weightMap.getOrDefault(colorEnum.getType(), 0) != 0) {
                continue;
            }else {
                weightMap.put(colorEnum.getType(), weight.intValue());
            }
        }
    }

    /**
     * @description
     *      先判断 连着两个数据
     * @param x
     * @param y
     * @param isIntervalTwo
     * @return java.util.List<com.hm.model.ceshi.Cell>
     * @author wyp
     * @date 2021/4/19 18:13
     */
    public static List<Integer> getCell(int x, int y, boolean isIntervalTwo){
        List<Integer> collect = Lists.newArrayList();
        if(isIntervalTwo){
            Map<Integer, Long> collect1 = cellEntityList.stream()
                    .filter(e -> ((e.getX() == x - 2 || e.getX() == x - 1) && e.getY() == y)).collect(Collectors.groupingBy(Cell::getColorId, Collectors.counting()));
            Map<Integer, Long> collect2 = cellEntityList.stream()
                    .filter(e -> ((e.getY() == y - 2 || e.getY() == y - 1) && e.getX() == x)).collect(Collectors.groupingBy(Cell::getColorId, Collectors.counting()));
            // 判断连着的 两个颜色是否一致   左边，下面
            getColor(collect, collect1);
            getColor(collect, collect2);
        }else {
            collect = cellEntityList.stream().filter(e -> (e.getX() == x - 1 && e.getY() == y) || (e.getX() == x && e.getY() == y - 1)).map(Cell::getColorId).collect(Collectors.toList());
        }
        return collect;
    }

    private static void getColor(List<Integer> collect, Map<Integer, Long> map){
        for(Integer color: map.keySet()){
            if(map.get(color) >= 2){
                collect.add(color);
            }
        }
    }

    public static boolean isCanWhite(int y){
        long count = cellEntityList.stream().filter(e -> y - e.getY() <= 2)
                .filter(e -> e.getColorId() == ColorEnum.white.getType()).count();
        return count < 2;
    }

    public static void main(String[] args) {
//        int index = 100;
//        int score = 100;
//        Map<Integer, Cell> integerCellMap = generateList(index, 100, 50);

        for(int y =0;y<=5;y++){
            System.out.println((y/5+1)*5+100);
        }


    }

    //type  0-单点  1-连击
    public static int getScores(int serverId, List<Integer> ids, int type) {
        CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
        ServerData serverData = ServerDataManager.getIntance().getServerData(serverId);
        int score = ids.stream().mapToInt(t -> {
            Cell cell = serverData.getServerEliminate().getCell(t);
            return cell==null?0:cell.getScore();
        }).sum();
        if (type == 0) return score;
        //连击根基连击个数算分
        double[] rareArrays = commValueConfig.getConvertObj(CommonValueType.ArmyPressBorderScoreRare);
        double rare = rareArrays[Math.min(2, ids.size() - 3)];
        return (int) Math.ceil(MathUtils.mul(score, rare));
    }
}
