package com.hm.action.guild;

import com.google.common.collect.Lists;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.guild.biz.GuildBiz;
import com.hm.action.guild.biz.GuildCityBiz;
import com.hm.action.guild.biz.GuildMemberBiz;
import com.hm.action.guild.util.GuildCheckException;
import com.hm.action.guild.util.GuildCheckUtil;
import com.hm.action.guild.vo.GuildVo;
import com.hm.action.item.ItemBiz;
import com.hm.action.mail.biz.MailBiz;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.GuildConfig;
import com.hm.config.excel.MailConfig;
import com.hm.enums.*;
import com.hm.libcore.annotation.Action;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.log.LogBiz;
import com.hm.message.MessageComm;
import com.hm.model.guild.Guild;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;
import com.hm.servercontainer.guild.GuildContainer;
import com.hm.sysConstant.SysConstant;
import com.hm.util.HFUtils;
import com.hm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Action
public class GuildManagerAction extends AbstractPlayerAction {

    @Resource
    private GuildBiz guildBiz;
    @Resource
    private ItemBiz itemBiz;
    @Resource
    private GuildConfig guildConfig;
    @Resource
    private CommValueConfig commValueConfig;
    @Resource
    private GuildMemberBiz guildMemberBiz;
    @Resource
    private LogBiz logBiz;
    @Resource
    private GuildCityBiz guildCityBiz;
    @Resource
    private MailBiz mailBiz;
    @Resource
    private MailConfig mailConfig;

    //创建部落 #msg:30001,name=123,guildFlag=1,flagName=牛
    @MsgMethod(MessageComm.C2S_Guild_Create)
    public void create(Player player, JsonMsg msg) {
        String upName = msg.getString("name");	//部落名字
        //检查是否能创建部落
        // 合服服务器用户名字自动加上服务器前缀
        String name = HFUtils.checkName(player, upName);
        try {
            GuildCheckUtil.checkCreate(player, upName, name);
        } catch (GuildCheckException e) {
            player.sendErrorMsg(e.getErrorCode());
            return;
        }

        int createCost = commValueConfig.getCommValue(CommonValueType.GuidCreate);
        Items createItems = new Items(PlayerAssetEnum.Gold.getTypeId(), createCost, ItemType.CURRENCY.getType());
        List<Items> items = Lists.newArrayList(createItems);
        if (!itemBiz.checkItemEnoughAndSpend(player, items, LogType.GuildCreateCost)){
            player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
            return;
        }

        Guild guild = guildBiz.create(player, name);
        player.notifyObservers(ObservableEnum.GuildChangeJob);
        player.sendUserUpdateMsg();
        //重新发送部落信息
        guild.sendPlayerAllGuild(player);
        player.sendMsg(MessageComm.S2C_Guild_Create, guild);
    }

    //部落列表信息
    @MsgMethod(MessageComm.C2S_Guild_List)
    public void list(Player player, JsonMsg msg) {
        String excludeIds = msg.getString("excludeIds");
        List<Integer> excludeGuildIds = StringUtil.splitStr2IntegerList(excludeIds, ",");
        AtomicBoolean isEnough = new AtomicBoolean();
        List<GuildVo> listGuild = guildBiz.getActiveGuildRankList(player.getServerId(), excludeGuildIds, isEnough);
        JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_Guild_List);
        serverMsg.addProperty("guilds", listGuild);
        serverMsg.addProperty("isEnough", isEnough);
        player.sendMsg(serverMsg);
    }

    //申请加入部落
    @MsgMethod(MessageComm.C2S_Guild_Join)
    public void join(Player player, JsonMsg msg) {
        int guildId = msg.getInt("guildId");
        Guild guildReq = GuildContainer.of(player.getServerId()).getGuild(guildId);
        try {
            GuildCheckUtil.checkAddGuild(player, guildReq);
            if(guildMemberBiz.isGuildMemberMax(guildReq)) {
                player.sendErrorMsg(SysConstant.Guild_PlayerNumErr);
                return;
            }
            if(guildReq.isAutoAdd()) {
                guildMemberBiz.playerAddGuild(guildReq, player, GuildJob.NONE);
                guildReq.sendPlayerAllGuild(player);
                player.sendUserUpdateMsg();
                player.sendMsg(MessageComm.S2C_Guild_Join, guildReq);
            }else {
                guildMemberBiz.joinGild(player, guildReq);
                player.sendUserUpdateMsg();
                player.sendMsg(MessageComm.S2C_Guild_Join, guildReq);
                //给部落长，副部落长，发送用户申请红点
                JsonMsg tempMsg = JsonMsg.create(MessageComm.S2C_Guild_PlayerAdd);
                tempMsg.addProperty("reqSize", guildReq.getGuildReqs().getNum());
                guildBiz.broadGuildManager(guildReq, tempMsg);
            }
        } catch (GuildCheckException e) {
            player.sendErrorMsg(e.getErrorCode());
        }
    }

    //玩家取消部落申请
    @MsgMethod(MessageComm.C2S_Guild_Cancel)
    public void cancel(Player player, JsonMsg msg) {
        int guildId = msg.getInt("guildId");
        Guild guild = GuildContainer.of(player.getServerId()).getGuild(guildId);
        if(null==guild) {
            player.sendErrorMsg(SysConstant.Guild_NoExist);
            return;
        }
        guildMemberBiz.cancel(player, guild);
        player.sendUserUpdateMsg();

        //给部落长，副部落长，发送用户申请红点
        JsonMsg tempMsg = JsonMsg.create(MessageComm.S2C_Guild_PlayerAdd);
        tempMsg.addProperty("reqSize", guild.getGuildReqs().getNum());
        guildBiz.broadGuildManager(guild, tempMsg);
        player.sendMsg(MessageComm.S2C_Guild_Cancel);
    }

    //玩家同意部落加入邀请 #msg:30039,guilId=100133
    @MsgMethod(MessageComm.C2S_Guild_PlayerAgree)
    public void playerAgree(Player player, JsonMsg msg) {
        int guildId = msg.getInt("guilId");
        Guild guild = GuildContainer.of(player.getServerId()).getGuild(guildId);
        if(null==guild) {
            player.sendErrorMsg(SysConstant.Guild_NoExist);
            return ;
        }
        if(!guild.getGuildInvite().containsInvite(player.getId())) {
            player.sendErrorMsg(SysConstant.Guild_NoneInvite);
            return ;
        }
        //校验部落人数上限
        if(guildMemberBiz.isGuildMemberMax(guild)) {
            player.sendErrorMsg(SysConstant.Guild_PlayerNumErr);
            return ;
        }
        //将用户加入到部落中
        log.error("玩家同意加入部落：{}", player.getId());
        guildMemberBiz.playerAddGuild(guild, player, GuildJob.NONE);
        guild.sendPlayerAllGuild(player);
        player.sendMsg(MessageComm.S2C_Guild_PlayerAgree);
        player.sendUserUpdateMsg();
    }

    //玩家拒绝部落加入邀请 #msg:30041,guilId=100167
    @MsgMethod(MessageComm.C2S_Guild_PlayerRefuse)
    public void playerRefuse(Player player, JsonMsg msg) {
        int guildId = msg.getInt("guilId");
        Guild guild = GuildContainer.of(player.getServerId()).getGuild(guildId);
        if(null==guild) {
            player.sendErrorMsg(SysConstant.Guild_NoExist);
            return ;
        }
        log.error("玩家拒绝加入部落：{}", player.getId());
        player.playerGuild().removeReq(guildId);
        guild.getGuildInvite().removeInvite(player.getId());
        guild.saveDB();
        player.sendUserUpdateMsg();
    }

}
