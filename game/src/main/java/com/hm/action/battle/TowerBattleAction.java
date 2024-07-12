package com.hm.action.battle;

import com.hm.action.AbstractPlayerAction;
import com.hm.action.battle.Handler.TowerBattleHandler;
import com.hm.enums.BattleType;
import com.hm.libcore.annotation.Action;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.util.string.StringUtil;
import com.hm.message.MessageComm;
import com.hm.model.battle.TowerBattle;
import com.hm.model.player.Player;
import com.hm.sysConstant.SysConstant;
import com.hm.util.ArrayUtils;

import javax.annotation.Resource;

/**
 * @Author chenwei
 * @Date 2024/5/14
 * @Description: 驭魂之地
 */
@Action
public class TowerBattleAction extends AbstractPlayerAction {

    @Resource
    private TowerBattleHandler towerBattleHandler;

    /**
     * 随机buff列表
     * @param player
     * @param msg
     */
    @MsgMethod(MessageComm.C2S_Player_Battle_Tower_Random_Buff)
    public void randomBuffs(Player player, JsonMsg msg){
        TowerBattle battle = (TowerBattle) player.playerBattle().getPlayerBattle(BattleType.TowerBattle.getType());
        if (battle == null){
            return;
        }
        // 检查是否可以随机buff
        if (!towerBattleHandler.checkCanRandomBuff(battle)){
            player.sendErrorMsg(SysConstant.Battle_Tower_Random_Att_Not);
            return;
        }
        towerBattleHandler.randomBuffIds(player, battle);
        player.playerBattle().SetChanged();
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Player_Battle_Tower_Random_Buff);
    }

    /**
     * 添加buff
     * @param player
     * @param msg
     */
    @MsgMethod(MessageComm.C2S_Player_Battle_Tower_Add_Buff)
    public void addBuff(Player player, JsonMsg msg){
        int id = msg.getInt("id");
        int index = msg.getInt("index");
        TowerBattle battle = (TowerBattle) player.playerBattle().getPlayerBattle(BattleType.TowerBattle.getType());
        if (battle == null){
            return;
        }
        // 检查是否可以添加buff
        if (!towerBattleHandler.checkCanAddBuff(battle, id, index)){
            player.sendErrorMsg(SysConstant.Battle_Tower_Random_AddAtt_Not);
            return;
        }
        towerBattleHandler.addBuff(player, battle, id, index);
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Player_Battle_Tower_Add_Buff);
    }

    /**
     * 预设buff类型
     * @param player
     * @param msg
     */
    @MsgMethod(MessageComm.C2S_Player_Battle_Tower_PreSet_Buff)
    public void preBuff(Player player, JsonMsg msg){
        String types = msg.getString("types");// 类型1,类型2,类型3,类型4
        TowerBattle battle = (TowerBattle) player.playerBattle().getPlayerBattle(BattleType.TowerBattle.getType());
        if (battle == null){
            return;
        }
        int[] typeArr = StringUtil.splitStr2IntArray(types, ",");
        if (typeArr.length != 4 || ArrayUtils.haveDupElement(typeArr)){
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }
        battle.setPreBuffType(typeArr);
        player.playerBattle().SetChanged();
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Player_Battle_Tower_PreSet_Buff);
    }

    /**
     * 是否一键设置buff
     * @param player
     * @param msg
     */
    @MsgMethod(MessageComm.C2S_Player_Battle_Tower_Buff_Auto)
    public void auto(Player player, JsonMsg msg){
        int auto = msg.getInt("auto");// 0 不自动设置  1 自动设置
        TowerBattle battle = (TowerBattle) player.playerBattle().getPlayerBattle(BattleType.TowerBattle.getType());
        if (battle == null){
            return;
        }
        battle.setAuto(auto);
        player.playerBattle().SetChanged();
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Player_Battle_Tower_Buff_Auto);
    }

    /**
     * 一键设置buff
     * @param player
     * @param msg
     */
    @MsgMethod(MessageComm.C2S_Player_Battle_Tower_AddAll_Buff)
    public void addBuffAll(Player player, JsonMsg msg){
        TowerBattle battle = (TowerBattle) player.playerBattle().getPlayerBattle(BattleType.TowerBattle.getType());
        if (battle == null){
            return;
        }
        if (battle.getAuto() <= 0){
            return;
        }
        towerBattleHandler.autoAddAllBuff(player, battle);
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Player_Battle_Tower_AddAll_Buff);
    }

}
