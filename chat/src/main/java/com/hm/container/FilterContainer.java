package com.hm.container;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.util.sensitiveWord.py.HanyuPinyinHelper;
import com.hm.action.WorldPlayer;
import com.hm.config.excel.CommValueConfig;
import com.hm.db.GameServerManager;
import com.hm.enums.ChatRoomType;
import com.hm.enums.CommonValueType;
import com.hm.http.HttpBiz;
import com.hm.model.Player;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FilterContainer {
    @Resource
    private HttpBiz httpBiz;
    @Resource
    private CommValueConfig commValueConfig;

    //所有玩家屏蔽字连续发言次数
    private Map<Long, WorldPlayer> filterMap = Maps.newConcurrentMap();
    private List<Integer> noChatIds = Lists.newArrayList();
    /**
     * @param player
     * @param content       客户端上传内容
     * @param filterContent 过滤敏感字后的内容
     * @param roomType
     * @desc 发现敏感字后立即关小黑屋
     */
    public boolean doWorldPlayerNow(Player player, String content, String filterContent, ChatRoomType roomType) {
        if (!ChatRoomType.isFilterWord(roomType)) {
            return false;
        }
//        //玩家超过75级不封小黑屋
//        if (player.getLv() >= commValueConfig.getIntValue(CommonValueType.BLACK_HOUSE_LV)
//            || player.getVipLv() >= commValueConfig.getIntValue(CommonValueType.BLACK_HOUSE_VIP_LV)) {
//            return false;
//        }
//
//        //开服第1天45级不进小黑屋，此后每天涨2级，直到75级
//		int openServerDay = GameServerManager.getInstance().getServerOpenDay(player.getServerId());
//        int limitLv = commValueConfig.getBlackHouseDynamicLv(openServerDay);
//        int limitVipLv = commValueConfig.getBlackHouseDynamicVipLv(openServerDay);
//
//		if (player.getLv() >= limitLv || player.getVipLv() >= limitVipLv) {
//			return false;
//		}

//        //聊天内容中的敏感字
//        Set<String> badWords = BadWordSensitiveUtil.getInstance().getSensitiveWord(content);
//        // 触发后台敏感字立即关小黑屋
//        if (!badWords.isEmpty()) {
//            log.info(player.getId() + "触发后台敏感字:" + badWords);
//            blackHouse(player);
//            return true;
//        }
//
//        // 触发举报敏感字3次后关小黑屋
//        if (content.length() <= 5) {
//            //只有内容中有过滤字且长度大于5时才进行统计
//            return false;
//        }
//        Set<String> reportWords = ReportSensitiveUtil.getInstance().getSensitiveWord(content);
//        if (reportWords.isEmpty()) {
//            return false;
//        }
        WorldPlayer worldPlayer = getWorldPlayer(player.getId());

//        if (worldPlayer.getReportCatchInfo().getCount() >= commValueConfig.getIntValue(CommonValueType.REPORT_WORD_TIMES)) {
//            blackHouse(player);
//            return true;
//        }
        if (worldPlayer.toBlackHouse(content,commValueConfig.getMaxCounts())) {
            blackHouse(player);
            return true;
        }
        return false;
    }

    public void blackHouse(Player player) {
        player.addBlackRecord();
        int blackHouseTime = commValueConfig.getBlackHouseTime(player.getTodayBlackCount());
        if (blackHouseTime <= 0) {
            return;
        }
        httpBiz.blackHome(player.getId(), blackHouseTime);
    }

    public WorldPlayer getWorldPlayer(long playerId) {
        return filterMap.computeIfAbsent(playerId, id -> new WorldPlayer());
    }

    public void loadNoChatIds(List<Integer> ids) {
        this.noChatIds = ids;
    }

    public boolean isNoChat(long playerId) {
        return this.noChatIds.contains(playerId);
    }

    public static void main(String[] args) {
        System.out.println(HanyuPinyinHelper.toPyOnlyHz("天天向上"));
    }
}
