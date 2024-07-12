package com.hm.action.captive;

import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.captive.log.AbstractCaptiveLog;
import com.hm.action.item.ItemBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.config.GameConstants;
import com.hm.config.excel.CaptiveConfig;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.templaextra.CampPrisonerResearcherTemplateImpl;
import com.hm.config.excel.templaextra.CampPrisonerTemplateImpl;
import com.hm.enums.CommonValueType;
import com.hm.enums.LogType;
import com.hm.enums.PlayerFunctionType;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.*;
import com.hm.sysConstant.SysConstant;
import com.hm.util.MathUtils;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description: tank俘虏
 * @author: chenwei
 * @create: 2020-07-02 14:41
 **/
@Action
public class CaptiveAction extends AbstractPlayerAction {

    @Resource
    private CaptiveBiz captiveBiz;
    @Resource
    private PlayerBiz playerBiz;
    @Resource
    private ItemBiz itemBiz;
    @Resource
    private CaptiveConfig captiveConfig;
    @Resource
    private CommValueConfig commValueConfig;


    /**
     * 购买研究员
     * @param player
     * @param msg
     */
    @MsgMethod(MessageComm.C2S_Captive_Buy_Researcher)
    public void buyCaptiveResearcher(Player player, JsonMsg msg){
        if (!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.TankCaptive)){
            player.sendErrorMsg(SysConstant.Function_Lock);
            return;
        }
        int id = msg.getInt("id");
        if (player.playerCaptive().checkHaveResearcher(id)){
            player.sendErrorMsg(SysConstant.Captive_Researcher_Have);
            return;
        }
        CampPrisonerResearcherTemplateImpl researcherTemplate = captiveConfig.getCampPrisonerResearcherTemplate(id);
        if (researcherTemplate == null){
            return;
        }
        if (!itemBiz.checkItemEnoughAndSpend(player,researcherTemplate.getHirePriceCost(),LogType.CaptiveBuyResearcherCost)){
            player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
            return;
        }
        //拥有时间
        long endTime = System.currentTimeMillis() + researcherTemplate.getLast_time() * GameConstants.MINUTE;
        player.playerCaptive().addResearcher(id, endTime);
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Captive_Buy_Researcher);
    }

    /**
     * 更换研究员
     * @param player
     * @param msg
     */
    @MsgMethod(MessageComm.C2S_Captive_Change_Researcher)
    public void changeCaptiveResearcher(Player player, JsonMsg msg){
        if (!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.TankCaptive)){
            player.sendErrorMsg(SysConstant.Function_Lock);
            return;
        }
        int id = msg.getInt("id");
        if (!player.playerCaptive().checkHaveResearcher(id)){
            player.sendErrorMsg(SysConstant.Captive_Researcher_Not_Have);
            return;
        }
        player.playerCaptive().changeResearcher(id);
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Captive_Change_Researcher);
    }

    /**
     * 升级
     * @param player
     * @param msg
     */
    @MsgMethod(MessageComm.C2S_Captive_Lv_Up)
    public void lvUp(Player player, JsonMsg msg){
        if (!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.TankCaptive)){
            player.sendErrorMsg(SysConstant.Function_Lock);
            return;
        }
        int maxLv = captiveConfig.getMaxPrisoner();
        int curLv = player.playerCaptive().getLv();
        if (curLv >= maxLv){
            player.sendErrorMsg(SysConstant.Captive_Lv_Max);
            return;
        }
        CampPrisonerTemplateImpl prisonerTemplate = captiveConfig.getCampPrisonerTemplate(curLv);
        if (prisonerTemplate == null){
            return;
        }
        List<Items> costItems = prisonerTemplate.getUpgradeCostItem();
        if (!itemBiz.checkItemEnoughAndSpend(player,costItems, LogType.CaptiveLvUpCost)){
            player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
            return;
        }
        int lv = Math.min(maxLv, curLv +1);
        player.playerCaptive().setLv(lv);
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Captive_Lv_Up);
    }

    /**
     * 研究俘虏tank
     * @param player
     * @param msg
     */
    @MsgMethod(MessageComm.C2S_Captive_Research)
    public void research(Player player, JsonMsg msg){
        if (!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.TankCaptive)){
            player.sendErrorMsg(SysConstant.Function_Lock);
            return;
        }
        int index = msg.getInt("index");
        CaptiveSlot captiveSlot = player.playerCaptive().getCaptiveSlot(index);
        List<Items> items = captiveBiz.doCaptiveTech(player, captiveSlot);
        JsonMsg jsonMsg = new JsonMsg(MessageComm.S2C_Captive_Research);
        jsonMsg.addProperty("rewards",items);
        player.sendUserUpdateMsg();
        player.sendMsg(jsonMsg);

    }

    /**
     * 更改自动研究设置
     * @param player
     * @param msg
     */
    @MsgMethod(MessageComm.C2S_Captive_Auto_Tech)
    public void changAutoTech(Player player, JsonMsg msg){
        if (!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.TankCaptive)){
            player.sendErrorMsg(SysConstant.Function_Lock);
            return;
        }
        boolean autoTech = msg.getBoolean("autoTech");
        if (!captiveBiz.isCanAutoTech(player)){
            player.sendErrorMsg(SysConstant.Captive_Lv_Not_Enough);
            return;
        }
        player.playerCaptive().changeAutoTech(autoTech);
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Captive_Auto_Tech);
    }


    /**
     * 赎回
     * @param player
     * @param msg
     */
    @MsgMethod(MessageComm.C2S_Captive_Redeem)
    public void redeem(Player player, JsonMsg msg){
        if (!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.TankCaptive)){
            player.sendErrorMsg(SysConstant.Function_Lock);
            return;
        }
        int tankId = msg.getInt("tankId");
        BeCaptiveTankInfo beCaptiveTank = player.playerCaptive().getBeCaptiveTank(tankId);
        if (beCaptiveTank == null){
            return;
        }
        int minutePrice = commValueConfig.getCommValue(CommonValueType.CaptiveRedeemCost);
        double leftMinute = Math.ceil(MathUtils.div(beCaptiveTank.getEndTime() - System.currentTimeMillis(),GameConstants.MINUTE));
        if (leftMinute > 0){
            long spend = MathUtils.mul(minutePrice,leftMinute);
            if (!playerBiz.checkAndSpendPlayerCurrency(player, CurrencyKind.Gold, spend, LogType.CaptiveRedeemCost)){
                player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
                return;
            }
        }
        captiveBiz.redeemTank(player,beCaptiveTank);
        player.sendUserUpdateMsg();
        //更新部队信息
        player.sendWorldTroopMessage();
        player.sendMsg(MessageComm.S2C_Captive_Redeem);
    }


    /**
     * 到期检查
     * @param player
     * @param msg
     */
    @MsgMethod(MessageComm.C2S_Captive_EndTime_Check)
    public void endTimeCheck(Player player, JsonMsg msg){
        if (!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.TankCaptive)){
            player.sendErrorMsg(SysConstant.Function_Lock);
            return;
        }
        captiveBiz.checkResearcherEndTime(player);
        captiveBiz.checkCaptiveTankEndTime(player);
        captiveBiz.checkBeCaptiveTankEndTime(player);
        player.sendUserUpdateMsg();
        //更新部队信息
        player.sendWorldTroopMessage();
        player.sendMsg(MessageComm.S2C_Captive_EndTime_Check);
    }


    /**
     * 记录
     * @param player
     * @param msg
     */
    @MsgMethod(MessageComm.C2S_Captive_Logs)
    public void captiveLogs(Player player, JsonMsg msg){
        if (!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.TankCaptive)){
            player.sendErrorMsg(SysConstant.Function_Lock);
            return;
        }
        List<AbstractCaptiveLog> logList = player.playerCaptive().getLogList();
        JsonMsg serverMsg = new JsonMsg(MessageComm.S2C_Captive_Logs);
        serverMsg.addProperty("logs", logList);
        player.sendUserUpdateMsg();
        player.sendMsg(serverMsg);
    }
}
