package com.hm.action.event;

import com.hm.action.AbstractPlayerAction;
import com.hm.libcore.annotation.Action;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.message.MessageComm;
import com.hm.model.player.Player;
import lombok.extern.slf4j.Slf4j;

/**
 * 记录统计 打印到日志文件里
 * @Author chenwei
 * @Date 2024/6/20
 * @Description:
 */
@Slf4j
@Action
public class EventAction extends AbstractPlayerAction {


    @MsgMethod(MessageComm.C2S_Player_Event_Statistic)
    public void saveRecord(Player player, JsonMsg msg){
        int militaryLv = player.playerCommander().getMilitaryLv();
        log.info("playerId:{}, 头衔等级:{}, 参数:{}",player.getId(), militaryLv, msg.getString("param"));
        player.sendErrorMsg(MessageComm.S2C_Player_Event_Statistic);
    }

}
