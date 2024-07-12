package com.hm.model.task.Random;

import com.google.common.collect.Lists;
import com.hm.libcore.msg.JsonMsg;
import com.hm.enums.RandomTaskType;
import com.hm.model.player.Player;

import java.util.Collections;
import java.util.List;

/**
 * Description:答题、算术事件公用这个
 * User: yang xb
 * Date: 2019-04-12
 *
 * @author Administrator
 */
public class RandomTaskQuiz extends BaseRandomTask {
    /**
     * random_task_library1.xlsx id
     * random_task_library2.xlsx id
     */
    private int id;
    private int[] ansArray;

    public RandomTaskQuiz() {
        super();
    }

    public RandomTaskQuiz(RandomTaskType taskType) {
        super(taskType);
        ansArray = randomAnswer();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int[] getAnsArray() {
        return ansArray;
    }

    /**
     * 答题活动信任客户端提交的结果
     *
     * @param player
     * @return
     */
    @Override
    public boolean doFinish(Player player, JsonMsg msg) {
        int ans = msg.getInt("ans");
        boolean isCorrect = false;
        if (ans >= 0 && ans < ansArray.length) {
            isCorrect = ansArray[ans] == 0;
        }

        if (!isCorrect) {
            return false;
        }

        return true;
    }

    //生成答案顺序
    private int[] randomAnswer() {
        int[] ansArray = new int[4];
        List<Integer> answer = Lists.newArrayList(0, 1, 2, 3);
        Collections.shuffle(answer);
        for (int i = 0; i < answer.size(); i++) {
            ansArray[i] = answer.get(i);
        }
        return ansArray;
    }
}
