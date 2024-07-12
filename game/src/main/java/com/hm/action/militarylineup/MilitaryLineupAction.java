package com.hm.action.militarylineup;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.message.MessageComm;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;
import com.hm.util.StringUtil;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * User: yang xb
 * Date: 2019-09-03
 */
@Action
public class MilitaryLineupAction extends AbstractPlayerAction {
    @Resource
    private MilitaryLineupBiz militaryLineupBiz;


    @MsgMethod(MessageComm.C2S_MilitaryLineup_Update)
    public void update(Player player, JsonMsg msg) {
        int id = msg.getInt("id");
        String position = msg.getString("position");
        // 1:1001,2:1002,3:345
        Map<Integer, Integer> map = StringUtil.strToMap(position, ",", ":");
        boolean bSuccess = militaryLineupBiz.update(player, id, map);
        if (bSuccess) {
            player.notifyObservers(ObservableEnum.MilitaryLineup);
            player.sendUserUpdateMsg();
        }
        player.sendMsg(MessageComm.S2C_MilitaryLineup_Update, bSuccess);
    }

    @MsgMethod(MessageComm.C2S_MilitaryLineup_UpdateAll)
    public void updateAll(Player player, JsonMsg msg) {
        // 1&1:1001,2:1002,3:345@2&1:1001,2:1002,3:345
        String param = msg.getString("param");

        Table<Integer, Integer, Integer> table = HashBasedTable.create();
        List<String> allLines = StringUtil.splitStr2StrList(param, "@");
        String beforeMsg = player.playerMilitaryLineup().getMilitartLineMsg(player);

        for (String line : allLines) {
            List<String> s = StringUtil.splitStr2StrList(line, "&");
            if (s.size() != 2) {
                continue;
            }
            Integer id = Integer.parseInt(s.get(0));
            // 1:1001,2:1002,3:345
            Map<Integer, Integer> map = StringUtil.strToMap(s.get(1), ",", ":");
            map.forEach((k, v) -> table.put(id, k, v));
        }
        boolean bSuccess = militaryLineupBiz.updateAll(player, table);
        if (bSuccess) {
            player.notifyObservers(ObservableEnum.MilitaryLineup);
            player.sendUserUpdateMsg();
        }
        String afterMsg = player.playerMilitaryLineup().getMilitartLineMsg(player);
        player.notifyObservers(ObservableEnum.CHMilitaryLineup, beforeMsg, afterMsg);
        player.sendMsg(MessageComm.S2C_MilitaryLineup_UpdateAll, bSuccess);
    }

    @MsgMethod(MessageComm.C2S_MilitaryLineup_Update_Assistance)
    public void updateAssistance(Player player, JsonMsg msg) {
        int id = msg.getInt("id");
        int before = player.playerMilitaryLineup().getMilitaryLineup(id).getAssistanceLv();
        if (militaryLineupBiz.updateAssistance(player, id)) {
            player.notifyObservers(ObservableEnum.MilitaryLineup);
            player.notifyObservers(ObservableEnum.MilitaryAssistanceLvup, id, before, player.playerMilitaryLineup().getMilitaryLineup(id).getAssistanceLv());
            player.sendUserUpdateMsg();
        }
    }

    @MsgMethod(MessageComm.C2S_MilitaryLineup_LvUp)
    public void lvUp(Player player, JsonMsg msg) {
        int id = msg.getInt("id");
        if (!militaryLineupBiz.lvUp(player, id)) {
            return;
        }
        player.notifyObservers(ObservableEnum.MilitaryLineup);
        player.sendUserUpdateMsg();

        HashMap<String, Integer> map = new HashMap<>();
        int lv = player.playerMilitaryLineup().getMilitaryLineup(id).getLv();
        map.put("id", id);
        map.put("nowLv", lv);
        player.sendMsg(MessageComm.S2C_MilitaryLineup_LvUp, map);

    }
}
