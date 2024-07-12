package com.hm.action.wx;

import com.hm.config.excel.WXConfig;
import com.hm.config.excel.temlate.WxSubscribeTemplateImpl;
import com.hm.container.PlayerContainer;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.springredis.util.RedisMapperUtil;
import com.hm.libcore.util.date.DateUtil;
import com.hm.model.player.Player;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.server.GameServerManager;

import javax.annotation.Resource;
import java.util.List;

@Biz
public class WXBiz implements IObserver {
    @Resource
    private WXConfig wxConfig;

    @Override
    public void registObserverEnum() {
        ObserverRouter.getInstance().registObserver(ObservableEnum.HourEvent, this);
        ObserverRouter.getInstance().registObserver(ObservableEnum.GuildWarHerald, this);

    }

    @Override
    public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
        switch (observableEnum) {
            case HourEvent:
                for (Integer serverId : GameServerManager.getInstance().getServerIdList()) {
                    checkHourEvent(serverId);
                }
                break;
            case GuildWarHerald:
                doGuildWarHerald((int)argv[0]);
        }
    }

    public void doGuildWarHerald(int serverId) {
        checkPlayerSub(serverId,WXSubsType.GuildWarStart);
    }


    /**
     * 每小时检查 是否发送订阅
     * @param serverId
     */
    public void checkHourEvent(int serverId) {
        try {
            List<WXSubs> wxSubsList = getNoOnlineWX(serverId);

            //检查7日登录
            int hour = DateUtil.thisHour(true);
            if(hour == 12) {
                checkPlayerSub(wxSubsList,WXSubsType.SignUp);
            }
            //挂机奖励检查
            if(hour < 8) {
                checkPlayerSub(wxSubsList,WXSubsType.WarHelpReward);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查服务器通用的订阅
     * @param serverId
     * @param subsType
     */
    public void checkPlayerSub(int serverId,WXSubsType subsType) {
        List<WXSubs> wxSubsList = getNoOnlineWX(serverId);
        checkPlayerSub(wxSubsList,subsType);
    }
    public void checkPlayerSub(List<WXSubs> wxSubsList,WXSubsType subsType) {
        for (WXSubs wxSubs : wxSubsList) {
            sendWxSubEvent(wxSubs,subsType);
        }
    }


    private boolean isTimeOver(WXSubInfo info) {
        if (!info.canSend()) {
            return false;
        }
        WXSubs wxSubs = WXSubs.getWXSubs(info.getServerId(), info.getPlayerId());
        sendWxSubEvent(wxSubs, WXSubsType.getWXSubsType(info.getSubId()));
        return true;
    }
    /**
     * @param player
     */
    public void sendPlayerWXEvent(Player player, WXSubsType wxSubsType) {
        if(player == null || player.isOnline()) {
            return;
        }
        WXSubs wxSubs = WXSubs.getWXSubs(player.getServerId(),player.getId());
        if(wxSubs == null) {
            System.out.println("wxSubs == is null"+player.getId());
            return;
        }
        sendWxSubEvent(wxSubs,wxSubsType);
    }


    public void sendWxSubEvent(WXSubs wxSubs,WXSubsType subsType) {
        if (wxSubs == null || !wxSubs.isCanSend(subsType)) {
            System.out.println("wxSubs == is isCanSend"+subsType.getType());
            return;//没有订阅
        }
        if(!subsType.checkPlayerCanSend(wxSubs.getId())) {
            return;//不符合条件
        }
        wxSubs.remove(subsType);
        wxSubs.saveDB();

        WxSubscribeTemplateImpl template = wxConfig.getWxSubscribeTemplate(wxSubs.getGameId(),subsType);
        if(template == null) {
            System.out.println(wxSubs.getGameId()+"_"+subsType.getType()+" WxSubscribeTemplateImpl is null");
            return;
        }
        WXUtils.sendPlayerEvent(wxSubs,subsType,template);
    }

    /**
     * 获取服务器中没有在线的订阅玩家
     * @param serverId
     * @return
     */
    public List<WXSubs> getNoOnlineWX(int serverId) {
        List<WXSubs> wxSubsList = RedisMapperUtil.queryListAll(serverId,WXSubs.class);
        wxSubsList.removeIf(e -> PlayerContainer.isOnline(e.getId()));
        return wxSubsList;
    }

    public void playerWXSubs(Player player,String gameId, String openId, List<Integer> temps) {
        WXSubs wxSubs = WXSubs.getWXSubs(player.getServerId(),player.getId());
        if(wxSubs == null) {
            wxSubs = new WXSubs(player,openId);
        }
        wxSubs.setGameId(gameId);
        wxSubs.addTempList(temps);
        wxSubs.saveDB();
    }

}
