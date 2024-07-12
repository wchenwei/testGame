package com.hm.rpc;

import com.alipay.sofa.rpc.config.ProviderConfig;
import com.alipay.sofa.rpc.config.ServerConfig;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.guild.biz.GuildWorldBiz;
import com.hm.action.http.kf.KfMsgBiz;
import com.hm.action.item.ItemBiz;
import com.hm.action.kf.kfexpedition.KfExpeditionServerResult;
import com.hm.action.mail.biz.MailBiz;
import com.hm.action.troop.biz.TroopBiz;
import com.hm.action.troop.biz.WorldTroopBiz;
import com.hm.actor.ActorDispatcherType;
import com.hm.db.PlayerUtils;
import com.hm.enums.LogType;
import com.hm.enums.MailConfigEnum;
import com.hm.libcore.action.IAction;
import com.hm.libcore.actor.IRunner;
import com.hm.libcore.inner.InnerMsgRouter;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.msg.Router;
import com.hm.libcore.protobuf.HMProtobuf;
import com.hm.libcore.rpc.IGameRpc;
import com.hm.libcore.rpc.KFPMail;
import com.hm.libcore.rpc.KFServerMail;
import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.util.date.DateUtil;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.model.guild.Guild;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.servercontainer.guild.GuildContainer;
import com.hm.sysConstant.SysConstant;
import com.hm.util.ItemUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
public class GameRpc implements IGameRpc {

    @Override
    public void sendInnerMsg(JsonMsg msg) {
        System.out.println("==========inner msg==================");
        System.out.println(GSONUtils.ToJSONString(msg));
        InnerMsgRouter.getInstance().process(msg);
    }

    @Override
    public void sendPBRequestMsg(byte[] data) {

    }


    public static void startRpc() {
        int rpcPort = com.hm.libcore.serverConfig.ServerConfig.getInstance().getRpcPort();
        log.info("start game rpc start port : {}", rpcPort);
        ServerConfig serverConfig = new ServerConfig()
                .setProtocol("bolt") // 设置一个协议，默认bolt
                .setPort(rpcPort) // 设置一个端口，默认12200
                .setDaemon(false); // 非守护线程

        ProviderConfig<IGameRpc> providerConfig = new ProviderConfig<IGameRpc>()
                .setInterfaceId(IGameRpc.class.getName()) // 指定接口
                .setRef(new GameRpc()) // 指定实现
                .setServer(serverConfig); // 指定服务端

        providerConfig.export(); // 发布服务
        log.info("game rpc start port : {}", serverConfig.getPort());
    }

    @Override
    public void sendPlayerHMResponse(long playerId,byte[] data) {
        try {
            HMProtobuf.HMResponse response = HMProtobuf.HMResponse.parseFrom(data);
            Player player = PlayerUtils.getOnlinePlayer(playerId);
            if(player == null) {
                return;
            }
            player.write(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean addOrSpendPlayerItem(long playerId, String items, boolean isAdd,String log) {
        Player player = PlayerUtils.getPlayer(playerId);
        if(player == null) {
            return false;
        }
        ItemBiz itemBiz = SpringUtil.getBean(ItemBiz.class);
        List<Items> itemsList = ItemUtils.str2DefaultItemList(items);
        if(isAdd) {
            itemBiz.addItem(player,itemsList, LogType.KfActivity.value(log));
        }else{
            if(!itemBiz.checkItemEnoughAndSpend(player,itemsList,LogType.KfActivity.value(log))) {
                player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
                return false;
            }
        }
        player.sendUserUpdateMsg();
        return true;
    }

    @Override
    public void sendServerMail(int serverId, List<KFPMail> mailList) {
        MailBiz mailBiz = SpringUtil.getBean(MailBiz.class);
        for (KFPMail kfpMail : mailList) {
            try {
                log.error(serverId+" get kfmail:"+GSONUtils.ToJSONString(kfpMail));
                long playerId = kfpMail.getId();
                List<Items> itemsList = ItemUtils.str2DefaultItemList(kfpMail.getItems());
                mailBiz.sendSysMail(serverId,playerId,kfpMail.getMailType(),itemsList,kfpMail.getParms());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void sendPlayerMail(long playerId, KFPMail kfpMail) {
        Player player = PlayerUtils.getPlayer(playerId);
        if(player == null) {
            return;
        }
        MailBiz mailBiz = SpringUtil.getBean(MailBiz.class);
        log.error(playerId+" player get kfmail:"+GSONUtils.ToJSONString(kfpMail));
        List<Items> itemsList = ItemUtils.str2DefaultItemList(kfpMail.getItems());
        mailBiz.sendSysMail(player.getServerId(),playerId,kfpMail.getMailType(),itemsList,kfpMail.getParms());
    }

    @Override
    public void sendServerMail(KFServerMail mail) {
        MailBiz mailBiz = SpringUtil.getBean(MailBiz.class);
        mailBiz.sendSysMail(mail.getServerId(),mail.getPlayerIds(),MailConfigEnum.getMailType(mail.getMailType())
                ,ItemUtils.str2DefaultItemList(mail.getItems()),mail.getParms());
    }


    @Override
    public void sendPlayerObs(long playerId,int obId, Object... args) {
        Player player = PlayerUtils.getPlayer(playerId);
        if(player == null) {
            return;
        }
        ObservableEnum observableEnum = ObservableEnum.getObservableEnum(obId);
        player.notifyObservers(observableEnum,args);
    }

    @Override
    public void sendServerObs(int serverId, int obId, Object... args) {
        ObservableEnum observableEnum = ObservableEnum.getObservableEnum(obId);
        ObserverRouter.notifyObservers(observableEnum, args);
    }

    @Override
    public void sendGameMsg(JsonMsg msg) {
        IAction action = Router.getInstance().getAction(msg.getMsgId());
        if(action instanceof AbstractPlayerAction) {
            ActorDispatcherType.Msg.putTask(msg.getPlayerId(), new IRunner() {
                @Override
                public Object runActor() {
                    AbstractPlayerAction playerAction = (AbstractPlayerAction)action;
                    Player player = PlayerUtils.getOnlinePlayer(msg.getPlayerId());
                    playerAction.doProcess(msg,player);
                    return null;
                }
            });
        }
    }

    @Override
    public boolean handFullTankHp(long playerId, long totalSecond) {
        Player player = PlayerUtils.getPlayer(playerId);
        if(player == null) {
            return false;
        }
        WorldTroopBiz worldTroopBiz = SpringUtil.getBean(WorldTroopBiz.class);
        return worldTroopBiz.checkSpendHandFullTroop(player,totalSecond).isSuc();
    }

    @Override
    public void sendGuildFinanceReward(long playerId, int cityId, String items) {
        Player player = PlayerUtils.getPlayer(playerId);
        if(player == null) {
            return;
        }
        int guildId = player.playerGuild().getGuildId();
        if (guildId <= 0){
            return;
        }
        Guild guild =  GuildContainer.of(player).getGuild(player.playerGuild().getGuildId());
        if (guild == null){
            return;
        }
        GuildWorldBiz guildWorldBiz = SpringUtil.getBean(GuildWorldBiz.class);

        List<Items> itemList = ItemUtils.str2DefaultItemList(items);
        if(itemList.size() > 0) {
            guildWorldBiz.doMailOccupyCityReward(guild, cityId, itemList.get(0), DateUtil.today());
        }
    }

    @Override
    public String calCloneTroopSpend(long playerId) {
        Player player = PlayerUtils.getPlayer(playerId);
        if(player == null) {
            return null;
        }
        Items item = SpringUtil.getBean(TroopBiz.class).calPlayerCloneItem(player);
        if(!SpringUtil.getBean(ItemBiz.class).checkItemEnough(player, item)) {
            return null;
        }
        return ItemUtils.itemToString(item);
    }

    @Override
    public void sendKfExpeditionServerResult(String obj) {
        KfExpeditionServerResult serverResult = GSONUtils.FromJSONString(obj, KfExpeditionServerResult.class);
        SpringUtil.getBean(KfMsgBiz.class).sendKfExpeditionServerResult(serverResult);
    }

    @Override
    public void sendKfBackOilServerResult(String obj) {
        KfExpeditionServerResult serverResult = GSONUtils.FromJSONString(obj, KfExpeditionServerResult.class);
        SpringUtil.getBean(KfMsgBiz.class).sendKfBackOilServerResult(serverResult);
    }

    @Override
    public void doKFNoMatch(int serverId, int kfType) {
        System.out.println(serverId+"_"+kfType+" kf no match");
        ObserverRouter.getInstance().notifyObservers(ObservableEnum.KFNoMatch,serverId,kfType);
    }
}
