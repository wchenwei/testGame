package com.hm.action.smallgame;

import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.item.ItemBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.action.smallgame.biz.SmallGameBiz;
import com.hm.config.SmallGameConfig;
import com.hm.config.excel.CommValueConfig;
import com.hm.enums.*;
import com.hm.log.LogBiz;
import com.hm.message.MessageComm;
import com.hm.model.cd.CdData;
import com.hm.model.item.Items;
import com.hm.model.player.BaseSamllGame;
import com.hm.model.player.Player;
import com.hm.sysConstant.SysConstant;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xjt
 * @version 1.0
 * @desc 表述
 * @date 2022/2/11 14:31
 */
@Action
public class SmallGameAction extends AbstractPlayerAction {

    @Resource
    private SmallGameConfig smallGameConfig;
    @Resource
    private CommValueConfig commValueConfig;
    @Resource
    private PlayerBiz playerBiz;
    @Resource
    private ItemBiz itemBiz;
    @Resource
    private SmallGameBiz smallGameBiz;
    @Resource
    private LogBiz logBiz;

    /**
     * 战斗结束
     * @param player
     * @param msg
     * msg:332233,type=1,exp=30,time=100,isWin=true
     */
    @MsgMethod(MessageComm.C2S_SmallGame_End)
    public void gameEnd(Player player, JsonMsg msg) {
        int type = msg.getInt("type");
        long exp = msg.getLong("exp");//经验
        long score = msg.getLong("score");//分数
        boolean isWin = msg.getBoolean("isWin");
        //校验体力
        if(!player.getPlayerCDs().checkCanCd(CDType.GameEnergy)){
            //没有次数
            return;
        }
        //校验小游戏
        GameType gameType = GameType.getGameType(type);
        if(null==gameType) {
            return;
        }
        //校验游戏是否开启
        BaseSamllGame game = player.playerGame().getGame(gameType);
        if(null==game) {
            return;
        }
        //校验经验上限
        if(!smallGameConfig.checkExpScore(game.getLv(), exp, score)) {
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }
        //消耗体力
        int costEn = commValueConfig.getCommValue(CommonValueType.SmallGameCostEn);
        CdData data = player.getPlayerCDs().getCdDataByCdType(CDType.GameEnergy);
        if(data.getCount()<costEn) {
            return;
        }
        //记录日志
        logBiz.addPlayerActionLog(player, ActionType.SmallGame.getCode(),String.format("%s_%s_%s", type, costEn, game.getLv()));
        data.touchCdEvent(player, costEn);
        player.getPlayerCDs().SetChanged();
        //增加及经验信息
        smallGameBiz.addExp(player, game, exp, score);
        //更新排行榜
        smallGameBiz.updateRank(player);

        JsonMsg returnMsg = JsonMsg.create(MessageComm.S2C_SmallGame_End);
        //增加通关奖励
        List<Items> rewards = smallGameConfig.getGroundPassReward(game.getLv());
        if(isWin) {
            returnMsg.addProperty("reward", rewards);
            itemBiz.addItem(player, rewards, LogType.SmallGame.value(String.format("%s_%s", type, game.getLv())));
        }
        returnMsg.addProperty("type", type);
        player.sendUserUpdateMsg();
        player.sendMsg(returnMsg);
    }
}







