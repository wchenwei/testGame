package com.hmkf.action.level;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.google.common.collect.Lists;
import com.hm.action.troop.client.ClientTroop;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.templaextra.NpcTroopTemplate;
import com.hm.enums.ItemType;
import com.hm.libcore.msg.JsonMsg;
import com.hm.message.KFRankMessageComm;
import com.hm.model.item.Items;
import com.hm.sysConstant.SysConstant;
import com.hm.util.ItemUtils;
import com.hm.util.StringUtil;
import com.hm.war.sg.CLunWarResult;
import com.hm.war.sg.WarParam;
import com.hm.war.sg.troop.AbstractFightTroop;
import com.hm.war.sg.troop.NpcTroop;
import com.hmkf.action.KfClientTroopUtils;
import com.hmkf.action.level.vo.KfPlayerDetailVo;
import com.hmkf.action.npc.NpcPlayer;
import com.hmkf.config.KFLevelConstants;
import com.hmkf.config.KfRankConfig;
import com.hmkf.config.extra.KfPkLevelTemplate;
import com.hmkf.config.template.NpcArenaExTemplate;
import com.hmkf.container.KFNpcContainer;
import com.hmkf.db.KfDBUtils;
import com.hmkf.model.IPKPlayer;
import com.hmkf.model.KFPlayer;
import com.hmkf.model.KfCmType;
import com.hmkf.model.kfwarrecord.AtkRecord;
import com.hmkf.model.kfwarrecord.KfWarRecord;
import com.hmkf.model.kfwarrecord.SPInfoTemp;
import com.hmkf.redis.KFRankRedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class KfLevelFightBiz {
    @Resource
    private KfRankConfig kfRankConfig;
    @Resource
    private KfLevelBiz kfLevelBiz;
    @Resource
    private CommValueConfig commValueConfig;
    @Resource
    private KfTopBiz kfTopBiz;
    /**
     * @author siyunlong
     * @version V1.0
     * @Description: 战斗
     * @date 2024/4/25 14:56
     */
    public boolean doFight(KFPlayer player,List<ClientTroop> clientTroopList,KfPlayerDetailVo oppoInfo) {
        //构建防御部队列表
        List<AbstractFightTroop> defList = buildDefList(oppoInfo.getId());
        AtkRecord atkRecord = new AtkRecord(player.getPlayerId(),clientTroopList);

        return doPlayerFight(player,clientTroopList,defList,oppoInfo.getId(),false,atkRecord);
    }

    //复仇战斗
    public boolean doFightRevenge(KFPlayer player,List<ClientTroop> clientTroopList,AtkRecord atkRecord) {
        //构建防御部队列表
        List<AbstractFightTroop> defList = buildDefList(atkRecord);

        return doPlayerFight(player,clientTroopList,defList,atkRecord.getId(),true,null);
    }

    public boolean doPlayerFight(KFPlayer player,List<ClientTroop> clientTroopList,List<AbstractFightTroop> defList,String defId
        ,boolean isRevenge,AtkRecord atkRecord) {
        log.error(player.getPlayerId()+" start->"+defId+" isRevenge:"+isRevenge);
        //构建防御部队列表
        IPKPlayer defPlayer = getPKPlayer(defId);
        if(defPlayer == null) {
            log.error(player.getPlayerId()+"防守方不存在"+defId);
            return false;
        }
        if(!player.getLevelPlayerInfo().haveFightNum()) {
            //检查玩家道具
            if (!player.addOrSpendPlayerItem
                    (ItemUtils.itemToString(new Items(KFLevelConstants.FightItemCard,1, ItemType.ITEM)), false,"p")) {
                player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
                return false;
            }
        }
        //构建攻击部队列表
        List<AbstractFightTroop> atkList = KfClientTroopUtils.buildKFTroop(player,clientTroopList);


        CLunWarResult warResult = doFight(player,defPlayer,atkList,defList,atkRecord,isRevenge);

        //扣次数
        player.getLevelPlayerInfo().addKfFightNum(warResult.isAtkWin());
        //重新随机
        if(!isRevenge) {
            player.getLevelPlayerInfo().setMatchInfos(kfLevelBiz.buildMatchUser(player));
        }
        player.sendPlayerUpdate();

        //发放奖励
        boolean isWin = warResult.isAtkWin();

        log.error(player.getPlayerId()+" end->"+defId+" ->win:"+isWin+" isRevenge:"+isRevenge);

        KfPkLevelTemplate levelTemplate = kfRankConfig.getStageGroup(player.getLevelType());
        JsonMsg serverMsg = new JsonMsg(KFRankMessageComm.S2C_LevelFight);
        serverMsg.addProperty("itemList", levelTemplate.getItemList(isWin));
        serverMsg.addProperty("warResult", warResult);
        serverMsg.addProperty("isRevenge", isRevenge);
        player.sendMsg(serverMsg);
        //给游戏服务器发奖励
        player.addOrSpendPlayerItem(levelTemplate.getItemListStr(isWin),true,"pk_war");

        return true;
    }


    public CLunWarResult doFight(IPKPlayer atk,IPKPlayer def, List<AbstractFightTroop> atkList
                        ,List<AbstractFightTroop> defList,AtkRecord atkRecord,boolean isRevenge) {
        try {
            CLunWarResult warResult = fightWarResult(atkList, defList);
            //保存战报
            SPInfoTemp atkInfo = new SPInfoTemp(atk,atkList.size());
            SPInfoTemp defInfo = new SPInfoTemp(def,defList.size());
            warResult.setAtkInfo(atkInfo);
            warResult.setDefInfo(defInfo);

            boolean isWin = warResult.isAtkWin();
            if(!isWin) {
                //保存战报
//                saveRecord(warResult,atkRecord,isRevenge);
                return warResult;//失败不保存战报
            }
            int[] scores = isRevenge?StringUtil.splitStr2IntArray(commValueConfig.getStrValue(KfCmType.RevengeScores.getType()),",")
                    :KFLevelConstants.buildScores(atk.getScore(),def.getScore());
            long addScore = scores[0];
            long reduceScore = scores[1];

            kfLevelBiz.addPlayerScore(atk, addScore);
            kfLevelBiz.addPlayerScore(def, -reduceScore);
            def.sendPlayerUpdate();

            System.out.println(atk.getPlayerId()+" addscore:"+addScore +" score:"+atk.getScore());
            System.out.println(def.getPlayerId()+" losescore:"+(-reduceScore) +" score:"+def.getScore());

            atkInfo.setAddScore(addScore);
            defInfo.setAddScore(-reduceScore);
            //检查top3
            kfTopBiz.checkTop(atk,def);

            //保存战报
            saveRecord(warResult,atkRecord,isRevenge);
            return warResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveRecord(CLunWarResult warResult,AtkRecord atkRecord,boolean isRevenge) {
        if(CollUtil.isNotEmpty(warResult.getWarResults())) {
            KfWarRecord kfWarRecord = new KfWarRecord(warResult, atkRecord, isRevenge);
            kfWarRecord.save();
        }
    }

    public IPKPlayer getPKPlayer(String id) {
        return KFLevelConstants.isNpc(id)?KFNpcContainer.getNpcPlayer(id)
                :KfDBUtils.getPlayerSports(Convert.toInt(id,0));
    }


    public List<AbstractFightTroop> buildDefList(String id) {
        if(KFLevelConstants.isNpc(id)) {
            return buildNpcFightTroops(id);
        }else{
            KFPlayer defPlayer = KfDBUtils.getPlayerSports(Integer.parseInt(id));
            return KfClientTroopUtils.buildDefTroop(defPlayer);
        }
    }

    public List<AbstractFightTroop> buildDefList(AtkRecord atkRecord) {
        String id = atkRecord.getId();
        if(KFLevelConstants.isNpc(id)) {
            return buildNpcFightTroops(id);
        }else{
            KFPlayer defPlayer = KfDBUtils.getPlayerSports(atkRecord.getIntId());
            List<ClientTroop> troopList = Arrays.stream(atkRecord.getTroopInfos())
                    .map(e -> ClientTroop.build(e)).collect(Collectors.toList());
            return KfClientTroopUtils.buildKFTroop(defPlayer,troopList);
        }
    }

    public List<AbstractFightTroop> buildNpcFightTroops(String npcPlayerId) {
        NpcPlayer npcPlayer = KFNpcContainer.getNpcPlayer(npcPlayerId);
        NpcArenaExTemplate npcTemplate = kfRankConfig.getArenaNpc(npcPlayer.getNpcId());
        List<AbstractFightTroop> defList = Lists.newArrayList();

        for (NpcTroopTemplate template : npcTemplate.getNpcTemplate()) {
            NpcTroop npcTroop = new NpcTroop("arena"+template.getId());
            npcTroop.loadNpcInfo(template.createNpcArmy(), template.getId(), npcPlayer.getName(), template.getLevel(),
                    template.getHead_icon(), template.getHead_frame(), template.getPower());
            defList.add(npcTroop);
        }
        return defList;
    }

    public CLunWarResult fightWarResult(List<AbstractFightTroop> atkList, List<AbstractFightTroop> defList) {
        CLunWarResult result = new CLunWarResult();
        result.doWarFight(atkList,defList,new WarParam().setMaxFrame(30000));
        return result;
    }
}
