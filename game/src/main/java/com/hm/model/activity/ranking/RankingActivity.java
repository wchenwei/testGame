package com.hm.model.activity.ranking;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.hm.libcore.language.LanguageVo;
import com.hm.libcore.spring.SpringUtil;
import com.hm.action.mail.biz.MailBiz;
import com.hm.config.excel.ActivityConfig;
import com.hm.config.excel.MailConfig;
import com.hm.config.excel.temlate.MailTemplate;
import com.hm.config.excel.templaextra.ActiveNewplayerRankTemplateImpl;
import com.hm.enums.ActivityType;
import com.hm.enums.MailConfigEnum;
import com.hm.enums.RankType;
import com.hm.leaderboards.RankRedisUtils;
import com.hm.model.activity.AbstractActivity;
import com.hm.util.PubFunc;
import redis.clients.jedis.Tuple;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Description:
 * User: yang xb
 * Date: 2019-02-15
 */
public class RankingActivity extends AbstractActivity {
    public RankingActivity() {
        super(ActivityType.Ranking);
    }

    @Override
    public void doCreateActivity() {
        long now = System.currentTimeMillis();
        // 结束时间、结算时间 三天后的0点
        long end = DateUtil.offsetDay(DateUtil.beginOfDay(new Date()), 3).getTime();
        setActivityTime(now, end);
        setCalTime(new Date(end));
    }

    @Override
    public void doCalActivity() {
    	int serverId = getServerId();
        ActivityConfig activityConfig = SpringUtil.getBean(ActivityConfig.class);
        Set<Integer> allRankType = activityConfig.getNewPlayerAllRankType();
        MailConfig mailConfig = SpringUtil.getBean(MailConfig.class);
        MailBiz mailBiz = SpringUtil.getBean(MailBiz.class);

        allRankType.forEach(rankType -> {
            // 邮件模板
            MailTemplate mailTemplate = mailConfig.getMailTemplate(rankType2MailEnum(rankType));
            if (mailTemplate == null) {
                return;
            }
            // 需要发放奖励的名次
            Set<Integer> rankNumList = activityConfig.getNewPlayerRankNumSet(rankType);
            int startRank = CollUtil.min(rankNumList);
            int endRank = CollUtil.max(rankNumList);
            RankType rankEnumType = RankType.getTypeByIndex(rankType);
            if(rankEnumType == null) {
            	return;
            }
            List<Tuple> topRanks = RankRedisUtils.getRankList(serverId, rankEnumType.getRankName(), startRank, endRank);
        	for (int i = 0; i < topRanks.size(); i++) {
    			Tuple leaderboardInfo = topRanks.get(i);
    			int rank = i +1;
                ActiveNewplayerRankTemplateImpl cfg = activityConfig.getNewPlayerRankCfg(rankType, rank);
                if(cfg != null) {
                	long playerId = PubFunc.parseInt(leaderboardInfo.getElement());
                	mailBiz.sendSysMail(serverId, playerId, mailTemplate, cfg.getRewardList(), LanguageVo.createStr(rank));
                }
        	}
        });
    }

    /**
     * 通过排行榜类型获取对应的mail template
     *
     * @param rankType
     * @return
     */
    private MailConfigEnum rankType2MailEnum(int rankType) {
        if (rankType == RankType.Combat.getType()) {
            return MailConfigEnum.Combat;
        } else if (rankType == RankType.PlayerMainBattle.getType()) {
            return MailConfigEnum.PlayerMainBattle;
        }
        return null;
    }
}
