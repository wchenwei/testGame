package com.hm.model.activity.qqprivilege;

import com.hm.enums.ActivityType;
import com.hm.model.activity.AbstractActivity;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import com.hm.util.bluevip.QQBlueVip;

/**
 * @author wyp
 * @description
 * @date 2021/10/8 9:33
 */
public class PrivilegeActivity extends AbstractActivity {

    public PrivilegeActivity() {
        super(ActivityType.QQPrivilege);
    }

    @Override
    public void checkPlayerLogin(Player player) {
        PrivilegeValue privilegeValue = (PrivilegeValue) player.getPlayerActivity().getPlayerActivityValue(ActivityType.QQPrivilege);
        privilegeValue.resetDay();
        player.getPlayerActivity().SetChanged();
    }

    @Override
    public boolean isCloseForPlayer(BasePlayer player) {
        QQBlueVip blueVip = player.playerBlueVip().getBlueVip();
        return blueVip == null;
    }
}
