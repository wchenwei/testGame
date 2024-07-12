package com.hm.action.kf;

import cn.hutool.core.util.StrUtil;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.item.ItemBiz;
import com.hm.action.kf.kfworldwar.WorldWarGroupCache;
import com.hm.action.kf.kfworldwar.WorldWarGroupEndCache;
import com.hm.action.kf.kfworldwar.sum.KfWorldWarServerTeamUtils;
import com.hm.action.kf.kfworldwar.sum.KfWorldWarSnumTime;
import com.hm.action.kf.kfworldwar.sum.KfWorldWarSumUtils;
import com.hm.action.kf.kfworldwar.winteam.WinTeamGroupVo;
import com.hm.action.kfseason.kftask.PlayerKFTaskBiz;
import com.hm.config.excel.KfConfig;
import com.hm.config.excel.templaextra.KfPeaceShopTemplateImpl;
import com.hm.enums.ActivityType;
import com.hm.enums.LogType;
import com.hm.message.MessageComm;
import com.hm.model.activity.kfactivity.KfWorldWarActivity;
import com.hm.model.activity.kfactivity.KfWorldWarServerGroup;
import com.hm.model.activity.kfworldwarshop.KfWorldWarShopActivity;
import com.hm.model.activity.kfworldwarshop.KfWorldWarShopValue;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.servercontainer.activity.ActivityServerContainer;
import com.hm.sysConstant.SysConstant;
import lombok.extern.slf4j.Slf4j;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author syl
 * @version V1.0
 * @Description: 跨服世界大战
 * @date 2020年11月10日15:52:31
 */
@Slf4j
@Action
public class KfWorldWarAction extends AbstractPlayerAction {
    @Resource
    private ItemBiz itemBiz;
    @Resource
    private KfConfig kfConfig;
    @Resource
    private PlayerKFTaskBiz playerKFTaskBiz;


    @MsgMethod(MessageComm.C2S_KfWorldWarUrl)
    public void getKFWorldWarUrl(Player player, JsonMsg msg) {
        KfWorldWarActivity activity = (KfWorldWarActivity) ActivityServerContainer.of(player).getAbstractActivity(ActivityType.KfWorldWar);
        if (!activity.isOpenForServer()) {
            return;
        }
        KfWorldWarSnumTime worldWarSnumTime = KfWorldWarSumUtils.getCurSnum();
        if (!worldWarSnumTime.isFightWarTime()) {
            log.error(player.getId() + "赛季已经结束");
            return;//正在结算
        }
        //判断是否是前12小时
        if (worldWarSnumTime.isReadyTime() && !player.playerTemp().isGm()) {
            log.error(player.getId() + "赛季当前备战状态");
            return;
        }
        int teamId = KfWorldWarServerTeamUtils.getPlayerTeamID(player.getId());
        if (teamId <= 0) {
            log.error(player.getId() + "找不到跨服世界大战分组");
            return;
        }
        String warUrl = WorldWarGroupCache.getWarUrl(teamId);
        if (StrUtil.isEmpty(warUrl)) {
            log.error(teamId + "warUrl is null");
            return;
        }

        JsonMsg serverMsg = new JsonMsg(MessageComm.S2C_KfWorldWarUrl);
        serverMsg.addProperty("url", warUrl);
        player.sendMsg(serverMsg);
    }

    @MsgMethod(MessageComm.C2S_KfWorldWarEndData)
    public void getKfWorldWarEndData(Player player, JsonMsg msg) {
        KfWorldWarActivity activity = (KfWorldWarActivity) ActivityServerContainer.of(player).getAbstractActivity(ActivityType.KfWorldWar);
        if (!activity.isOpenForServer()) {
            return;
        }
        KfWorldWarSnumTime worldWarSnumTime = KfWorldWarSumUtils.getCurSnum();
        if (worldWarSnumTime.isFightWarTime()) {
            log.error(player.getId() + "赛季没有结束");
            return;//没有结束
        }
        int teamId = KfWorldWarServerTeamUtils.getPlayerTeamID(player.getId());
        if (teamId <= 0) {
            log.error(player.getId() + "找不到跨服世界大战分组");
            return;
        }
        KfWorldWarServerGroup worldWarServerGroup = WorldWarGroupCache.getWarServerGroup(teamId);
        if (worldWarServerGroup == null) {
            log.error(player.getId() + "找不到分组");
            return;
        }
        WinTeamGroupVo winTeamGroupVo = WorldWarGroupEndCache.getWinTeamGroupVo(worldWarServerGroup.getGroupId(), worldWarSnumTime.getId());
        if (winTeamGroupVo == null) {
            log.error(player.getId() + "找不到获胜vo");
            player.sendErrorMsg(SysConstant.KfWorldWar_Cal);
            return;
        }
        JsonMsg serverMsg = new JsonMsg(MessageComm.S2C_KfWorldWarEndData);
        serverMsg.addProperty("winInfo", winTeamGroupVo);
        player.sendMsg(serverMsg);
    }

    @MsgMethod(MessageComm.C2S_KfWorldWar_shop)
    public void buy(Player player, JsonMsg msg) {
        int id = msg.getInt("id");
        KfWorldWarShopActivity activity = (KfWorldWarShopActivity)ActivityServerContainer.of(player).getAbstractActivity(ActivityType.KfWorldWarShop);
        if(activity==null||activity.isClose()) {
            return;
        }
        KfPeaceShopTemplateImpl template = kfConfig.getKwWorldWarShopTemplate(id);
        if(template==null) {
            return;
        }
        KfWorldWarShopValue value = (KfWorldWarShopValue)player.getPlayerActivity().getPlayerActivityValue(ActivityType.KfWorldWarShop);
        if(!value.isCanBuy(player, id)) {
            return;
        }
        if(!itemBiz.checkItemEnoughAndSpend(player, template.getPrices(), LogType.ActivityShop.value(ActivityType.KfWorldWarShop.getType()))){
            player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
            return;
        };
        value.buy(player, id);
        player.getPlayerActivity().SetChanged();
        List<Items> rewards = template.getRewards();
        itemBiz.addItem(player, rewards, LogType.ActivityShop.value(ActivityType.KfWorldWarShop.getType()));
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_KfWorldWar_shop,rewards);
    }
}
