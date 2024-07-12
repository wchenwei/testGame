package com.hm.enums;

import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.RandomTaskConfig;
import com.hm.config.excel.templaextra.RandomTaskConfigTemplateImpl;
import com.hm.model.task.Random.BaseRandomTask;
import com.hm.model.task.Random.RandomTaskCost;
import com.hm.model.task.Random.RandomTaskNormal;
import com.hm.model.task.Random.RandomTaskQuiz;

import java.util.Arrays;

/**
 * Description:random_task_config.xlsx
 * User: yang xb
 * Date: 2019-04-11
 *
 * @author Administrator
 */
public enum RandomTaskType {
    /*
    类型	事件标题
	1	拯救受难民众
	2	清缴恐怖分子
	3	帮助重建家园
	4	慰问前线士兵
	5	拉近城市关系
	6	保障后勤补给
	7	获取坦克图纸
	8	算数摸底考
	9	战地问答题
    * */
	RR_1(1, "拯救受难民众") {
        @Override
        public BaseRandomTask getRandomTask() {
            RandomTaskNormal task = new RandomTaskNormal(RandomTaskType.RR_1);
            return task;
        }
    },
    RR_2(2, "清缴恐怖分子") {
        @Override
        public BaseRandomTask getRandomTask() {
            RandomTaskNormal task = new RandomTaskNormal(RandomTaskType.RR_2);
            return task;
        }
    },
    RR_3(3, "帮助重建家园") {
        @Override
        public BaseRandomTask getRandomTask() {
            RandomTaskCost task = new RandomTaskCost(RandomTaskType.RR_3);
            RandomTaskConfig taskConfig = SpringUtil.getBean(RandomTaskConfig.class);
            RandomTaskConfigTemplateImpl cfg = taskConfig.getRandomTaskConfigTemplateImpl(RR_3.getType());
            task.setCostItems(cfg.pickARandomTargetItems());
            return task;
        }
    },
    RR_4(4, "慰问前线士兵") {
        @Override
        public BaseRandomTask getRandomTask() {
            RandomTaskCost task = new RandomTaskCost(RandomTaskType.RR_4);
            RandomTaskConfig taskConfig = SpringUtil.getBean(RandomTaskConfig.class);
            RandomTaskConfigTemplateImpl cfg = taskConfig.getRandomTaskConfigTemplateImpl(RR_4.getType());
            task.setCostItems(cfg.pickARandomTargetItems());
            return task;
        }
    },
    RR_5(5, "拉近城市关系") {
        @Override
        public BaseRandomTask getRandomTask() {
            RandomTaskNormal task = new RandomTaskNormal(RandomTaskType.RR_5);
            return task;
        }
    },
    RR_6(6, "保障后勤补给") {
        @Override
        public BaseRandomTask getRandomTask() {
            RandomTaskCost task = new RandomTaskCost(RandomTaskType.RR_6);
            RandomTaskConfig taskConfig = SpringUtil.getBean(RandomTaskConfig.class);
            RandomTaskConfigTemplateImpl cfg = taskConfig.getRandomTaskConfigTemplateImpl(RR_6.getType());
            task.setCostItems(cfg.pickARandomTargetItems());
            return task;
        }
    },
    RR_7(7, "获取坦克图纸") {
        @Override
        public BaseRandomTask getRandomTask() {
            RandomTaskCost task = new RandomTaskCost(RandomTaskType.RR_7);
            RandomTaskConfig taskConfig = SpringUtil.getBean(RandomTaskConfig.class);
            RandomTaskConfigTemplateImpl cfg = taskConfig.getRandomTaskConfigTemplateImpl(RR_7.getType());
            task.setCostItems(cfg.pickARandomTargetItems());
            return task;
        }
    },
    RR_8(8, "算数摸底考") {
        @Override
        public BaseRandomTask getRandomTask() {
            RandomTaskQuiz quiz = new RandomTaskQuiz(RandomTaskType.RR_8);
            RandomTaskConfig taskConfig = SpringUtil.getBean(RandomTaskConfig.class);
            quiz.setId(taskConfig.random8());
            return quiz;
        }
    },
    RR_9(9, "战地问答题") {
        @Override
        public BaseRandomTask getRandomTask() {
            RandomTaskQuiz quiz = new RandomTaskQuiz(RandomTaskType.RR_9);
            RandomTaskConfig taskConfig = SpringUtil.getBean(RandomTaskConfig.class);
            quiz.setId(taskConfig.random9());
            return quiz;
        }
    },
    ;

    private int type;
    private String desc;

    RandomTaskType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static RandomTaskType id2Type(int id) {
        return Arrays.stream(RandomTaskType.values()).filter(e -> e.getType() == id).findFirst().orElse(null);
    }

    public abstract BaseRandomTask getRandomTask();
}
