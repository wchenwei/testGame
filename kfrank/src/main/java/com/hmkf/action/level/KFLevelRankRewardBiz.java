package com.hmkf.action.level;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.ArrayListMultimap;
import com.hm.libcore.language.LanguageVo;
import com.hm.enums.KfLevelType;
import com.hm.enums.MailConfigEnum;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.rpc.GameRpcClientUtils;
import com.hm.libcore.rpc.IGameRpc;
import com.hm.libcore.rpc.KFPMail;
import com.hm.libcore.util.date.DateUtil;
import com.hm.util.ServerUtils;
import com.hmkf.config.KfRankConfig;
import com.hmkf.config.KfRankReward;
import com.hmkf.config.extra.KfLevelRewardRankTemplate;
import com.hmkf.level.LevelGroupContainer;
import com.hmkf.level.LevelWorldGroup;
import com.hmkf.level.rank.RankGroup;
import com.hmkf.redis.KFRankRedisUtils;
import com.hmkf.redis.RankItem;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 结算-重新分组
 * @date 2024/4/26 17:22
 */
@Slf4j
@Biz
public class KFLevelRankRewardBiz {
    @Resource
    private LevelGroupContainer levelGroupContainer;
    @Resource
    private KfRankConfig kfRankConfig;

    public boolean doRankReward() {
        //结算本期数据
        boolean haveWeek = DateUtil.getCsWeek() == 1;
        for (LevelWorldGroup levelWorldGroup : levelGroupContainer.getAllLevelWorldGroup()) {
            try {
                doRankReward(levelWorldGroup,haveWeek);
            } catch (Exception e) {
                log.error(levelWorldGroup.getGameTypeId() + "snum出现异常:", e);
            }
        }
        return true;
    }

    public boolean doRankReward(LevelWorldGroup levelWorldGroup,boolean haveWeek) {
        Map<Integer, List<RankGroup>> groupMap = levelWorldGroup.getGameTypeGroup().getGroupMap();

        ArrayListMultimap<Integer,KFPMail> serverMailMap = ArrayListMultimap.create();
        ArrayListMultimap<Integer,KFPMail> weekMailMap = ArrayListMultimap.create();

        log.error(levelWorldGroup.getGameTypeId()+"=========start doRankReward=========");
        for (KfLevelType levelType : KfLevelType.AllLevelTypes) {
            List<RankGroup> rankGroupList = groupMap.get(levelType.getType());
            if(CollUtil.isEmpty(rankGroupList)) {
                continue;
            }
            KfRankReward rankReward = kfRankConfig.getKfRankReward(levelType.getType());
            for (RankGroup rankGroup : rankGroupList) {
                log.error(levelWorldGroup.getGameTypeId()+"=========start doRankReward========="+rankGroup.getRankId());

                //取出当前排行数据
                List<RankItem> rankItemList = KFRankRedisUtils.getAllRankForRankItem(rankGroup.getRankId());
                for (RankItem rankItem : rankItemList) {
                    if(!rankItem.isNpc()) {//npc不加入
                        KfLevelRewardRankTemplate template = rankReward.getRankTemplate(rankItem.getRank());
                        if(template != null) {
                            long playerId = rankItem.getIntId();
                            log.error(playerId+" rank:"+rankItem.getRank()+" lv:"+levelType.getType());
                            LanguageVo[] param = LanguageVo.createStrArrays(rankItem.getRank()+"");
                            KFPMail mail = new KFPMail().setId(playerId).setParms(param)
                                    .setItems(template.getReward()).setMailType(MailConfigEnum.ArenaRankReward.getType());
                            int serverId = ServerUtils.getServerId(playerId);
                            serverMailMap.put(serverId,mail);
                            if(haveWeek) {
                                KFPMail weekMail = new KFPMail().setId(playerId).setParms(param)
                                        .setItems(template.getWeekly_reward()).setMailType(MailConfigEnum.WeekArenaReward.getType());
                                weekMailMap.put(serverId,weekMail);
                            }
                        }
                    }
                }
            }
        }
        log.error(levelWorldGroup.getGameTypeId()+"=========start run=========");

        sendSeverMail(serverMailMap);
        sendSeverMail(weekMailMap);
        return true;
    }
    
    
    public void sendSeverMail(ArrayListMultimap<Integer, KFPMail> serverMailMap) {
        for (int serverId : serverMailMap.keySet()) {
            try {
                log.error(serverId+"发送邮件奖励");
                List<KFPMail> mailList = serverMailMap.get(serverId);

                IGameRpc gameRpc = GameRpcClientUtils.getGameRpcByServerId(serverId);
                if(gameRpc != null) {
                    gameRpc.sendServerMail(serverId,mailList);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
