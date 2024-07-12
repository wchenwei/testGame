package com.hm.model.serverpublic;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.CommValueConfig;
import com.hm.enums.CommonValueType;
import com.hm.enums.RankType;
import com.hm.leaderboards.HdLeaderboardsService;
import com.hm.leaderboards.LeaderboardBiz;
import com.hm.model.battle.xiaochu.Cell;
import com.hm.util.CellUtils;

import java.util.Map;

public class ServerEliminate extends ServerPublicContext{

    private Map<Integer, Cell> cells = Maps.newConcurrentMap();


    public void resetWeek(){
        //发放排行榜奖励清空排行榜
        LeaderboardBiz leaderboardBiz = SpringUtil.getBean(LeaderboardBiz.class);
        leaderboardBiz.doRankReward(super.getContext().getServerId(), RankType.ArmyPressBorder);
        HdLeaderboardsService.getInstance().renameGameSet(super.getContext().getServerId(), Lists.newArrayList(RankType.ArmyPressBorder.getRankName()));
        initCells();
    }

    public void initCells(){
        int baseNpcLv = 1;
        int baseScore = 1;
        CommValueConfig config = SpringUtil.getBean(CommValueConfig.class);
        int flow = config.getCommValue(CommonValueType.ArmyPressBorderInit);
        this.cells = CellUtils.generateList(flow, baseScore, baseNpcLv);
        SetChanged();
    }

    public Cell getCell(int id){
        return cells.getOrDefault(id, new Cell(-1)).clone();
    }

    public Cell getNextCell(int id){
        //349
        if (id > 0) {
            id += 1;
        }
        return this.cells.getOrDefault(id, new Cell(-1)).clone();
    }

    public boolean isEmpty() {
        return cells.isEmpty();
    }
}
