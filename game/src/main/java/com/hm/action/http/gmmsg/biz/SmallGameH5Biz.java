package com.hm.action.http.gmmsg.biz;

import com.hm.libcore.annotation.Biz;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.smallgame.SmallGameAction;
import com.hm.db.PlayerUtils;
import com.hm.model.player.Player;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @ClassName SmallGameH5Biz
 * @Deacription SmallGameH5Biz
 * @Author zxj
 * @Date 2022/2/22 11:12
 * @Version 1.0
 **/
@Biz
public class SmallGameH5Biz {
    @Resource
    private SmallGameAction smallGameAction;

    public String doMsg(Map<String, String> paramMap) {
        long playerId = Long.parseLong(paramMap.get("playerId"));
        Player player = PlayerUtils.getPlayer(playerId);
        if(null==player) {
            return "-1";
        }
        JsonMsg msg = new JsonMsg(0);
        msg.addProperty("type",Integer.parseInt(paramMap.get("gametype")));
        msg.addProperty("exp",Long.parseLong(paramMap.get("exp")));
        msg.addProperty("score",Long.parseLong(paramMap.get("score")));
        msg.addProperty("isWin",Boolean.parseBoolean(paramMap.get("isWin")));
        smallGameAction.gameEnd(player, msg);
        return "1";
    }
}
