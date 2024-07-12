package com.hm.model.activity.mergeserver;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.language.LanguageVo;
import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.util.date.DateUtil;
import com.hm.action.mail.biz.MailBiz;
import com.hm.config.GameConstants;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.MailConfig;
import com.hm.config.excel.ServerMergeConfig;
import com.hm.config.excel.temlate.MailTemplate;
import com.hm.config.excel.temlate.ServiceMergeRechargeRewardTemplate;
import com.hm.config.excel.templaextra.ServiceMergeRankRewardTemplateImpl;
import com.hm.config.excel.templaextra.ServiceMergeRechargeRewardTemplateImpl;
import com.hm.enums.ActivityType;
import com.hm.enums.CommonValueType;
import com.hm.enums.MailConfigEnum;
import com.hm.enums.RankType;
import com.hm.leaderboards.RankRedisUtils;
import com.hm.model.activity.AbstractActivity;
import com.hm.model.item.Items;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.model.serverpublic.ServerMergeData;
import com.hm.util.PubFunc;
import redis.clients.jedis.Tuple;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Description:合服
 * User: yang xb
 * Date: 2019-04-26
 *
 * @author Administrator
 */
public class MergeServerActivity extends AbstractActivity {
    /**
     * 合服活动开启时的等级
     */
    private int serverLv;

    public MergeServerActivity() {
        super(ActivityType.MergeServer);
    }

    @Override
    public void checkPlayerLogin(Player player) {
        MergeServerValue value = (MergeServerValue) player.getPlayerActivity().getPlayerActivityValue(ActivityType.MergeServer);
        if (value == null) {
            return;
        }
        // 每天重置折扣物品购买记录
        if (!value.getDiscountRec().isEmpty()) {
            value.getDiscountRec().clear();
            player.getPlayerActivity().SetChanged();
        }
    }

    @Override
    public void doCreateActivity() {
        long now = System.currentTimeMillis();
        long end = DateUtil.beginOfDay(new Date()).getTime() + GameConstants.MergeServerActivityDays * GameConstants.DAY;
        setActivityTime(now, end);
        setCalTime(new Date(end));

        ServerMergeData serverMergeData = ServerDataManager.getIntance().getServerData(this.getServerId()).getServerMergeData();
        serverLv = serverMergeData.getAvgLv();
    }

    @Override
    public void doCalActivity() {
        // 活动结束后结算
        //至尊排行 奖励发放
        // ServiceMergeRankRewardTemplateImpl::getType
        // 类型
        // 1	荣誉榜
        // 2	战车榜
        // 3	战力榜
        // 4	充值榜
        // 5	金砖消耗榜

        Map<Integer, RankType> type2RankType = Maps.newConcurrentMap();
        type2RankType.put(1, RankType.TotalHonor);
        type2RankType.put(3, RankType.Combat);
        type2RankType.put(4, RankType.Recharge);
        type2RankType.put(5, RankType.ActMergeServerGold);

        Map<Integer, MailConfigEnum> type2MailType = Maps.newConcurrentMap();
        type2MailType.put(1, MailConfigEnum.MergeServerHonor);
        type2MailType.put(2, MailConfigEnum.MergeServerTankCombat);
        type2MailType.put(3, MailConfigEnum.MergeServerCombat);
        type2MailType.put(4, MailConfigEnum.MergeServerRecharge);
        type2MailType.put(5, MailConfigEnum.MergeServerGold);

        // type:common value 里的每个排行榜限制条件
        Map<Integer, CommonValueType> type2RankLimit = Maps.newConcurrentMap();
        type2RankLimit.put(1, CommonValueType.MergerServerHonor);
        type2RankLimit.put(2, CommonValueType.MergerServerTankCombat);
        type2RankLimit.put(3, CommonValueType.MergerServerCombat);
        type2RankLimit.put(4, CommonValueType.MergerServerRecharge);
        type2RankLimit.put(5, CommonValueType.MergerServerGold);

        ServerMergeConfig config = SpringUtil.getBean(ServerMergeConfig.class);
        if (config == null) {
            return;
        }
        CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
        MailConfig mailConfig = SpringUtil.getBean(MailConfig.class);
        MailBiz mailBiz = SpringUtil.getBean(MailBiz.class);

        int serverId = getServerId();
        // 合服后平均等级
        ServerMergeData serverMergeData = ServerDataManager.getIntance().getServerData(this.getServerId()).getServerMergeData();
        int serverLv = Math.max(1, serverMergeData.getAvgLv());

        Set<Integer> allKeys = type2RankType.keySet();
        for (Integer type : allKeys) {
            try {
                RankType rankType = type2RankType.get(type);
                MailConfigEnum mailType = type2MailType.get(type);
                // 限制发奖励的数值下限
                int limitValue = commValueConfig.getCommValue(type2RankLimit.get(type));

                MailTemplate mailTemplate = mailConfig.getMailTemplate(mailType);
                String title = mailTemplate.getMail_title();

                List<ServiceMergeRankRewardTemplateImpl> rankList = config.getRankList(serverLv, type);
                List<Tuple> topRanks = RankRedisUtils.getRankList(serverId, rankType.getRankName(), 1, Integer.MAX_VALUE);
                for (int i = 0; i < topRanks.size(); i++) {
                    Tuple leaderboardInfo = topRanks.get(i);
                    if (leaderboardInfo.getScore() < limitValue) {
                        break;
                    }
                    int rank = i + 1;
                    ServiceMergeRankRewardTemplateImpl cfg = rankList.stream().filter(e -> e.isFitRank(rank))
                            .findFirst().orElse(null);
                    if (cfg != null) {
                        long playerId = PubFunc.parseInt(leaderboardInfo.getElement());
                        System.err.println(playerId + "+" + rankType.getRankName() + "==" + rank + "=" + cfg.getRank1() + "_" + cfg.getRank2());
                        mailBiz.sendSysMail(serverId, playerId, mailTemplate, cfg.getRewardList(), LanguageVo.createStr(rank));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public boolean checkCondition(BasePlayer player, int id) {
        // 充值惊喜逻辑放通用流程处理
        ServerMergeConfig config = SpringUtil.getBean(ServerMergeConfig.class);
        if (config == null) {
            return false;
        }

        ServiceMergeRechargeRewardTemplate cfg = config.getServiceMergeRechargeRewardTemplate(id);
        if (cfg == null) {
            return false;
        }

        ServerMergeData serverMergeData = ServerDataManager.getIntance().getServerData(this.getServerId()).getServerMergeData();
        int serverLv = serverMergeData.getAvgLv();
        // 验证等级区间
        if (serverLv < cfg.getService_level_lower() || cfg.getService_level_upper() < serverLv) {
            return false;
        }

        MergeServerValue value = (MergeServerValue) player.getPlayerActivity().getPlayerActivityValue(ActivityType.MergeServer);
        if (value == null) {
            return false;
        }

        // 校验充值金额是否满足
        if (value.getRechargeCount() < cfg.getRecharge_num()) {
            return false;
        }

        return super.checkCondition(player, id);
    }

    @Override
    public void checkPlayerActivityValue(Player player) {
        MergeServerValue value = (MergeServerValue) player.getPlayerActivity().getPlayerActivityValue(ActivityType.MergeServer);
        if (value == null) {
            return;
        }
        if (value.getPlayerLv() > 0) {
            return;
        }
        value.setPlayerLv(player.playerLevel().getLv());
        player.getPlayerActivity().SetChanged();
    }

    @Override
    public List<Items> getRewardItems(BasePlayer player, int id) {
        List<Items> itemsList = Lists.newArrayList();

        ServerMergeConfig config = SpringUtil.getBean(ServerMergeConfig.class);
        if (config == null) {
            return itemsList;
        }

        ServiceMergeRechargeRewardTemplateImpl cfg = config.getServiceMergeRechargeRewardTemplate(id);
        if (cfg == null) {
            return itemsList;
        }

        return cfg.getRewardItems();
    }
}
