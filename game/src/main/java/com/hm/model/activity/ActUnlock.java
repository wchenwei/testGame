package com.hm.model.activity;

import cn.hutool.core.util.StrUtil;
import com.hm.enums.UnlockType;
import com.hm.model.player.BasePlayer;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ActUnlock {
    private UnlockType type;
    private int val;


    public boolean isUnlock(BasePlayer player) {
        switch (type){
            case Function: return player.getPlayerFunction().isOpenFunction(this.val);
            case FB: return player.playerMission().getFbId() >= this.val;
            case MLv: return player.playerCommander().getMilitaryLv() >= this.val;
        }
        return false;
    }

    public static ActUnlock build(String data) {
        if(StrUtil.isEmpty(data)) {
            return null;
        }
        ActUnlock actUnlock = new ActUnlock();
        actUnlock.type = UnlockType.id2Type(Integer.parseInt(data.split(":")[0]));
        actUnlock.val = Integer.parseInt(data.split(":")[1]);
        return actUnlock;
    }

}
