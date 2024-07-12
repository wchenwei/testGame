package com.hm.model.task;

import cn.hutool.core.util.StrUtil;
import com.hm.enums.SplitType;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;
import com.hm.observer.TaskTypeEnum;
import com.hm.util.StringUtil;

/**
 * 任务配置
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/8/11 16:46
 */
public interface ITaskConfTemplate {
    int getTaskFinish(); //完成需要的数量

    TaskTypeEnum getTaskTypeEnum();//任务类型

    String getTaskFinishPara(); // 解析参数

    default boolean isFitObserver(ObservableEnum event) {
        TaskTypeEnum e = getTaskTypeEnum();
        return  e != null && e.isFitObserver(event);
    }

    default int[] getTaskFinishParaArray() {
        return StringUtil.splitStr2IntArray(getTaskFinishPara(), SplitType.SPLIT1);
    }


    default int getTaskFinishParaInt() {
        if (StrUtil.isBlank(getTaskFinishPara())) {
            return 0;
        }
        return Integer.parseInt(getTaskFinishPara());
    }

    default boolean checkComplete(Player player, AbstractTask task){
        TaskTypeEnum taskTypeEnum = getTaskTypeEnum();
        if(taskTypeEnum == null) {
            return false;
        }
        return taskTypeEnum.checkTaskComplete(player, task, this);
    }
}
