package com.hm.observer;


import com.google.common.collect.Lists;
import com.hm.config.excel.DailyTaskConfig;
import com.hm.config.excel.RechargeConfig;
import com.hm.config.excel.TankConfig;
import com.hm.config.excel.temlate.DailyTaskConfigTemplateImpl;
import com.hm.config.excel.temlate.RechargePriceNewTemplate;
import com.hm.enums.BattleType;
import com.hm.enums.StatisticsType;
import com.hm.enums.TankRareType;
import com.hm.libcore.spring.SpringUtil;
import com.hm.model.battle.ExperimentBattle;
import com.hm.model.battle.TowerBattle;
import com.hm.model.player.CurrencyKind;
import com.hm.model.player.Equipment;
import com.hm.model.player.Player;
import com.hm.model.task.AbstractTask;
import com.hm.model.task.ITaskConfTemplate;
import com.hm.servercontainer.troop.TroopServerContainer;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum TaskTypeEnum {
    Empty(0, Lists.newArrayList(), "null"),
    T1(1, Lists.newArrayList(ObservableEnum.ClearnceMission), "通关第1-1关") {
        @Override
        public boolean chkEffect(Player player, AbstractTask task, ITaskConfTemplate taskTemplate, ObservableEnum observableEnum, Object... argc) {
            return chkProgress(player, task, taskTemplate);
        }

        @Override
        protected boolean chkProgress(Player player, AbstractTask task, ITaskConfTemplate taskTemplate) {
            if (player.playerMission().getFbId() > taskTemplate.getTaskFinishParaInt()) {
                return incTaskProgress(task, taskTemplate, 1, false);
            }
            return false;
        }
    },
    T2(2, Lists.newArrayList(ObservableEnum.TankResearch), "驯养1次战兽"),
    T3(3, Lists.newArrayList(ObservableEnum.MissionBox), "领取1次挂机奖励"),
    T4(4, Lists.newArrayList(ObservableEnum.TankLv), "升级1次战兽"),
    T5(5, Lists.newArrayList(ObservableEnum.TroopChange), "上阵1只战兽") {
        @Override
        public boolean chkEffect(Player player, AbstractTask task, ITaskConfTemplate taskTemplate, ObservableEnum observableEnum, Object... argc) {
            return chkProgress(player, task, taskTemplate);
        }

        @Override
        protected boolean chkProgress(Player player, AbstractTask task, ITaskConfTemplate taskTemplate) {
            Set<Integer> s = TroopServerContainer.of(player.getServerId()).getWorldTroopByPlayer(player).stream()
                    .flatMap(troop -> troop.getTroopArmy().getTankMap().keySet().stream()).collect(Collectors.toSet());
            return incTaskProgress(task, taskTemplate, s.size(), false);
        }
    },
    T6(6, Lists.newArrayList(ObservableEnum.TankStarUp), "升星1次战兽"),
    T7(7, Lists.newArrayList(ObservableEnum.MissionWave), "击败x波敌人"),
    T8(8, Lists.newArrayList(ObservableEnum.BattleFight), "征战蛮荒第x关") {
        @Override
        protected boolean chkProgress(Player player, AbstractTask task, ITaskConfTemplate taskTemplate) {
            ExperimentBattle playerBattle = (ExperimentBattle) player.playerBattle().getPlayerBattle(BattleType.ExperimentBattle.getType());
            if (playerBattle == null) {
                return false;
            }
            int curId = playerBattle.getCurId();
            if (curId >= taskTemplate.getTaskFinishParaInt()) {
                return incTaskProgress(task, taskTemplate);
            }
            return super.chkProgress(player, task, taskTemplate);
        }

        @Override
        public boolean chkEffect(Player player, AbstractTask task, ITaskConfTemplate taskTemplate, ObservableEnum observableEnum, Object... argc) {
            int type = (int) argc[0];
            if (type != BattleType.ExperimentBattle.getType()) {
                return false;
            }
            int id = (int) argc[1];
            if (id >= taskTemplate.getTaskFinishParaInt()) {
                return incTaskProgress(task, taskTemplate);
            }
            return false;
        }
    },
    T9(9, Lists.newArrayList(ObservableEnum.KFRankFight), "挑战1次角斗场"),
    T10(10, Lists.newArrayList(ObservableEnum.MilitaryLvChange), "晋升1次头衔"),
    T11(11, Lists.newArrayList(ObservableEnum.FightPveBoss), "挑战1次兽王试练"),
    T12(12, Lists.newArrayList(ObservableEnum.CarLv), "升阶1次坐骑"),
    T13(13, Lists.newArrayList(ObservableEnum.MilitaryLvUp), "头衔升至游民III") {
        @Override
        public boolean chkEffect(Player player, AbstractTask task, ITaskConfTemplate taskTemplate, ObservableEnum observableEnum, Object... argc) {
            return chkProgress(player, task, taskTemplate);
        }

        @Override
        protected boolean chkProgress(Player player, AbstractTask task, ITaskConfTemplate taskTemplate) {
            if (player.playerCommander().getMilitaryLv() >= taskTemplate.getTaskFinishParaInt()) {
                return incTaskProgress(task, taskTemplate, 1, false);
            }
            return false;
        }
    },
    T14(14, Lists.newArrayList(ObservableEnum.BattleFight, ObservableEnum.BattleSweep), "挑战1次驭魂之地") {
        @Override
        public boolean chkEffect(Player player, AbstractTask task, ITaskConfTemplate taskTemplate, ObservableEnum observableEnum, Object... argc) {
            int type = (int) argc[0];
            if (type != BattleType.TowerBattle.getType()) {
                return false;
            }
            return incTaskProgress(task, taskTemplate);
        }
    },
    T15(15, Lists.newArrayList(ObservableEnum.DriverLv), "升级1次兽魂"),
    T16(16, Lists.newArrayList(ObservableEnum.CarLv), "坐骑升至1阶5级") {
        @Override
        public boolean chkEffect(Player player, AbstractTask task, ITaskConfTemplate taskTemplate, ObservableEnum observableEnum, Object... argc) {
            if((boolean) argc[0]) {
                return chkProgress(player, task, taskTemplate);
            }
            return false;
        }

        @Override
        protected boolean chkProgress(Player player, AbstractTask task, ITaskConfTemplate taskTemplate) {
            int carLv = player.playerCommander().getCarLv();
            int expect = taskTemplate.getTaskFinishParaInt();
            if (carLv >= expect) {
                return incTaskProgress(task, taskTemplate, 1, false);
            }
            return false;
        }
    },
    T17(17, Lists.newArrayList(ObservableEnum.GuildPlayerAdd, ObservableEnum.GuildCreate), "成功加入或创建部落") {
        @Override
        public boolean chkEffect(Player player, AbstractTask task, ITaskConfTemplate taskTemplate, ObservableEnum observableEnum, Object... argc) {
            return chkProgress(player,task,taskTemplate);
        }

        @Override
        protected boolean chkProgress(Player player, AbstractTask task, ITaskConfTemplate taskTemplate) {
            if (player.playerGuild().getGuildId() > 0) {
                return incTaskProgress(task, taskTemplate, 1, false);
            }
            return false;
        }
    },
    T18(18, Lists.newArrayList(ObservableEnum.GuildDonation), "部落捐献1次"),
    T19(19, Lists.newArrayList(ObservableEnum.GuildDonation), "累计部落捐献3次"),
    T20(20, Lists.newArrayList(ObservableEnum.ComposeEqu), "合成1件装备") {
        @Override
        public boolean chkEffect(Player player, AbstractTask task, ITaskConfTemplate taskTemplate, ObservableEnum observableEnum, Object... argc) {
            return chkProgress(player,task,taskTemplate);
        }
        @Override
        protected boolean chkProgress(Player player, AbstractTask task, ITaskConfTemplate taskTemplate) {
            int count = (int)Arrays.stream(player.playerEquip().getEqus()).filter(e -> e.getEquId() > 0).count();
            if (count >= taskTemplate.getTaskFinishParaInt()) {
                return incTaskProgress(task, taskTemplate, count, false);
            }
            return false;
        }
    },
    T21(21, Lists.newArrayList(ObservableEnum.ComposeEqu), "累计合成3件装备") {
        @Override
        public boolean chkEffect(Player player, AbstractTask task, ITaskConfTemplate taskTemplate, ObservableEnum observableEnum, Object... argc) {
            return chkProgress(player,task,taskTemplate);
        }
        @Override
        protected boolean chkProgress(Player player, AbstractTask task, ITaskConfTemplate taskTemplate) {
            int count = (int)Arrays.stream(player.playerEquip().getEqus()).filter(e -> e.getEquId() > 0).count();
            if (count >= taskTemplate.getTaskFinishParaInt()) {
                return incTaskProgress(task, taskTemplate, count, false);
            }
            return false;
        }
    },
    T22(22, Lists.newArrayList(ObservableEnum.StrengthenEqu), "强化1次装备"),
    T23(23, Lists.newArrayList(), "查看部落战规则"),
    T24(24, Lists.newArrayList(ObservableEnum.ChangeStone), "镶嵌1颗宝石"),
    T25(25, Lists.newArrayList(ObservableEnum.ChangeStone), "累计镶嵌3颗宝石") {
        @Override
        public boolean chkEffect(Player player, AbstractTask task, ITaskConfTemplate taskTemplate, ObservableEnum observableEnum, Object... argc) {
            return chkProgress(player, task, taskTemplate);
        }
        @Override
        protected boolean chkProgress(Player player, AbstractTask task, ITaskConfTemplate taskTemplate) {
            int n = 0;
            for (Equipment equs : player.playerEquip().getEqus()) {
                n += equs.getSortStoneIds().size();
            }
            return incTaskProgress(task, taskTemplate, n, false);
        }
    },
    T26(26, Lists.newArrayList(ObservableEnum.StoneLvUp), "升级X次宝石"),
    T27(27, Lists.newArrayList(ObservableEnum.TankTechResearch), "捕获1次战兽") {
        @Override
        public boolean chkEffect(Player player, AbstractTask task, ITaskConfTemplate taskTemplate, ObservableEnum observableEnum, Object... argc) {
            int n = (int) argc[1];
            return incTaskProgress(task, taskTemplate, n);
        }
    },
    T28(28, Lists.newArrayList(ObservableEnum.TankStarUp), "任意战兽升至X星") {
        @Override
        public boolean chkEffect(Player player, AbstractTask task, ITaskConfTemplate taskTemplate, ObservableEnum observableEnum, Object... argc) {
            return chkProgress(player, task, taskTemplate);
        }

        @Override
        protected boolean chkProgress(Player player, AbstractTask task, ITaskConfTemplate taskTemplate) {
            int star = taskTemplate.getTaskFinishParaInt();
            int n = player.playerTank().getTankByStar(star).size();
            return incTaskProgress(task, taskTemplate, n, false);
        }
    },
    T29(29, Lists.newArrayList(ObservableEnum.TankUnlockStarNode), "升星节点点亮X次"),
    T30(30, Lists.newArrayList(ObservableEnum.TankLv), "X只战兽升至X级") {
        @Override
        public boolean chkEffect(Player player, AbstractTask task, ITaskConfTemplate taskTemplate, ObservableEnum observableEnum, Object... argc) {
            return chkProgress(player, task, taskTemplate);
        }

        @Override
        protected boolean chkProgress(Player player, AbstractTask task, ITaskConfTemplate taskTemplate) {
            int lv = taskTemplate.getTaskFinishParaInt();
            int n = player.playerTank().getTankByLv(lv).size();
            return incTaskProgress(task, taskTemplate, n, false);
        }
    },
    T31(31, Lists.newArrayList(ObservableEnum.TroopChange), "上阵X只X战兽") {
        @Override
        public boolean chkEffect(Player player, AbstractTask task, ITaskConfTemplate taskTemplate, ObservableEnum observableEnum, Object... argc) {
            return chkProgress(player, task, taskTemplate);
        }

        @Override
        protected boolean chkProgress(Player player, AbstractTask task, ITaskConfTemplate taskTemplate) {
            TankConfig tankConfig = SpringUtil.getBean(TankConfig.class);
            int rareId = taskTemplate.getTaskFinishParaInt();
            List<Integer> tankList = TroopServerContainer.of(player.getServerId()).getWorldTroopByPlayer(player).stream()
                    .flatMap(troop -> troop.getTroopArmy().getTankMap().keySet().stream()).collect(Collectors.toList());
            int n = 0;
            for (Integer tankId : tankList) {
                TankRareType type = TankRareType.getType(tankConfig.getTankSetting(tankId).getRare());
                if (type.getType() >= rareId) {
                    n++;
                }
            }
            return incTaskProgress(task, taskTemplate, n, false);
        }
    },

    T101(101, Lists.newArrayList(ObservableEnum.CityBattleResult), "部落战攻城N次"),
    T102(102, Lists.newArrayList(ObservableEnum.DailyTaskComplete), "完成N个日常任务"),
    T103(103, Lists.newArrayList(ObservableEnum.CostRes), "今日消费N金砖") {
        @Override
        public boolean chkEffect(Player player, AbstractTask task, ITaskConfTemplate taskTemplate, ObservableEnum observableEnum, Object... argc) {
            int type = (int) argc[0];
            long cnt = (long) argc[1];
            CurrencyKind kind = CurrencyKind.getCurrencyByIndex(type);
            if (player.playerCurrency().isGold(kind)) {
                return incTaskProgress(task, taskTemplate, cnt);
            }
            return false;
        }
    },
    T104(104, Lists.newArrayList(ObservableEnum.BattleFight), "挑战X次征战蛮荒") {
        @Override
        public boolean chkEffect(Player player, AbstractTask task, ITaskConfTemplate taskTemplate, ObservableEnum observableEnum, Object... argc) {
            int type = (int) argc[0];
            if (type != BattleType.ExperimentBattle.getType()) {
                return false;
            }
            return incTaskProgress(task, taskTemplate);
        }
    },

    T201(201, Lists.newArrayList(ObservableEnum.TankAdd), "拥有N个战兽") {
        @Override
        public boolean chkEffect(Player player, AbstractTask task, ITaskConfTemplate taskTemplate, ObservableEnum observableEnum, Object... argc) {
            return chkProgress(player, task, taskTemplate);
        }

        @Override
        protected boolean chkProgress(Player player, AbstractTask task, ITaskConfTemplate taskTemplate) {
            int size = player.playerTank().getTankList().size();
            return incTaskProgress(task, taskTemplate, size, false);
        }
    },
    T202(202, Lists.newArrayList(ObservableEnum.TankStarUp), "拥有N个X星战兽") {
        @Override
        public boolean chkEffect(Player player, AbstractTask task, ITaskConfTemplate taskTemplate, ObservableEnum observableEnum, Object... argc) {
            return chkProgress(player, task, taskTemplate);
        }

        @Override
        protected boolean chkProgress(Player player, AbstractTask task, ITaskConfTemplate taskTemplate) {
            int star = taskTemplate.getTaskFinishParaInt();
            int size = player.playerTank().getTankByStar(star).size();
            return incTaskProgress(task, taskTemplate, size, false);
        }
    },
    T203(203, Lists.newArrayList(ObservableEnum.TankAdd), "拥有N个X品质战兽") {
        @Override
        public boolean chkEffect(Player player, AbstractTask task, ITaskConfTemplate taskTemplate, ObservableEnum observableEnum, Object... argc) {
            return chkProgress(player, task, taskTemplate);
        }

        @Override
        protected boolean chkProgress(Player player, AbstractTask task, ITaskConfTemplate taskTemplate) {
            int rare = taskTemplate.getTaskFinishParaInt();
            int size = player.playerTank().getTankByRare(rare).size();
            return incTaskProgress(task, taskTemplate, size, false);
        }
    },
    T204(204, Lists.newArrayList(ObservableEnum.CostRes), "累计消费N金砖") {
        @Override
        public boolean chkEffect(Player player, AbstractTask task, ITaskConfTemplate taskTemplate, ObservableEnum observableEnum, Object... argc) {
            return chkProgress(player, task, taskTemplate);
        }

        @Override
        protected boolean chkProgress(Player player, AbstractTask task, ITaskConfTemplate taskTemplate) {
            long n = player.getPlayerStatistics().getLifeStatistics(StatisticsType.GoldUsed);
            return incTaskProgress(task, taskTemplate, n, false);
        }
    },
    T205(205, Lists.newArrayList(ObservableEnum.KFRankFight), "角斗场累计胜利N次"),
    T206(206, Lists.newArrayList(ObservableEnum.LOGIN), "累计登录N天") {
        @Override
        public boolean chkEffect(Player player, AbstractTask task, ITaskConfTemplate taskTemplate, ObservableEnum observableEnum, Object... argc) {
            return chkProgress(player, task, taskTemplate);
        }

        @Override
        protected boolean chkProgress(Player player, AbstractTask task, ITaskConfTemplate taskTemplate) {
            long lifeStatistics = player.getPlayerStatistics().getLifeStatistics(StatisticsType.LOGIN_DAYS);
            return incTaskProgress(task, taskTemplate, lifeStatistics, false);
        }
    },


    T301(301, Lists.newArrayList(ObservableEnum.CostRes), "任务开始后累计消费N金砖"){
        @Override
        public boolean chkEffect(Player player, AbstractTask task, ITaskConfTemplate taskTemplate, ObservableEnum observableEnum, Object... argc) {
            int kind = (int) argc[0];
            long count = (long) argc[1];
            if (kind == CurrencyKind.Gold.Index || kind == CurrencyKind.SysGold.Index){
                return incTaskProgress(task, taskTemplate, count);
            }
            return false;
        }
    },
    T302(302, Lists.newArrayList(ObservableEnum.AddHonor), "部落战累计获得积分"){
        @Override
        public boolean chkEffect(Player player, AbstractTask task, ITaskConfTemplate taskTemplate, ObservableEnum observableEnum, Object... argc) {
            long honor = (long) argc[1];
            return incTaskProgress(task, taskTemplate, honor);
        }
    },
    T303(303, Lists.newArrayList(ObservableEnum.TankMasterScoreAdd), "图鉴累计积分"){
        @Override
        public boolean chkEffect(Player player, AbstractTask task, ITaskConfTemplate taskTemplate, ObservableEnum observableEnum, Object... argc) {
            return chkProgress(player, task, taskTemplate);
        }

        @Override
        protected boolean chkProgress(Player player, AbstractTask task, ITaskConfTemplate taskTemplate) {
            int totalScore = player.playerTankMaster().getTotalScore();
            return incTaskProgress(task, taskTemplate, totalScore, false);
        }
    },
    T304(304, Lists.newArrayList(ObservableEnum.FightPveBoss), "兽王试炼累计伤害"){
        @Override
        public boolean chkEffect(Player player, AbstractTask task, ITaskConfTemplate taskTemplate, ObservableEnum observableEnum, Object... argc) {
            long hurt = (long) argc[0];
            return incTaskProgress(task, taskTemplate, hurt);
        }
    },
    T305(305, Lists.newArrayList(ObservableEnum.Recharge), "累计充值金砖（1元=10金砖）"){
        @Override
        public boolean chkEffect(Player player, AbstractTask task, ITaskConfTemplate taskTemplate, ObservableEnum observableEnum, Object... argc) {
            int rechargeId = (int)argc[0];
            RechargePriceNewTemplate rechargeTemplate = SpringUtil.getBean(RechargeConfig.class).getTemplate(rechargeId);
            if (rechargeTemplate == null){
                return false;
            }
            return incTaskProgress(task, taskTemplate, rechargeTemplate.getVip_point());
        }
    },
    T306(306, Lists.newArrayList(ObservableEnum.DailyTaskComplete), "日常累计获得活跃积分"){
        @Override
        public boolean chkEffect(Player player, AbstractTask task, ITaskConfTemplate taskTemplate, ObservableEnum observableEnum, Object... argc) {
            int taskId = (int) argc[0];
            DailyTaskConfigTemplateImpl cfg = SpringUtil.getBean(DailyTaskConfig.class).getDailyTaskCfg(taskId);
            if (cfg != null) {
                return incTaskProgress(task, taskTemplate, cfg.getTask_point());
            }
            return false;
        }
    },
    T307(307, Lists.newArrayList(ObservableEnum.CostRes), "累计消耗个人功勋（类型=1，id=7）"){
        @Override
        public boolean chkEffect(Player player, AbstractTask task, ITaskConfTemplate taskTemplate, ObservableEnum observableEnum, Object... argc) {
            int kind = (int) argc[0];
            long count = (long) argc[1];
            if (kind == CurrencyKind.Contr.Index){
                return incTaskProgress(task, taskTemplate, count);
            }
            return false;
        }
    },
    T308(308, Lists.newArrayList(ObservableEnum.BattleFight), "驭魂之地最高通关"){
        @Override
        public boolean chkEffect(Player player, AbstractTask task, ITaskConfTemplate taskTemplate, ObservableEnum observableEnum, Object... argc) {
            int type = (int) argc[0];
            if (type != BattleType.TowerBattle.getType()) {
                return false;
            }
            return chkProgress(player, task, taskTemplate);
        }

        @Override
        protected boolean chkProgress(Player player, AbstractTask task, ITaskConfTemplate taskTemplate) {
            TowerBattle towerBattle = (TowerBattle) player.playerBattle().getPlayerBattle(BattleType.TowerBattle.getType());
            if (towerBattle == null){
                return false;
            }
            int missionId = taskTemplate.getTaskFinishParaInt();
            if (towerBattle.getMaxHistory() >= missionId){
                return incTaskProgress(task, taskTemplate, 1);
            }
            return false;
        }
    },
    T309(309, Lists.newArrayList(ObservableEnum.MaxCombatChange), "个人总战力达到"){
        @Override
        public boolean chkEffect(Player player, AbstractTask task, ITaskConfTemplate taskTemplate, ObservableEnum observableEnum, Object... argc) {
            return chkProgress(player, task, taskTemplate);
        }

        @Override
        protected boolean chkProgress(Player player, AbstractTask task, ITaskConfTemplate taskTemplate) {
            long combat = player.getPlayerDynamicData().getCombat();
            return incTaskProgress(task, taskTemplate, combat, false);
        }
    },


    //----------------------
    /*
    Task1(1, Lists.newArrayList(ObservableEnum.TankResearch),"驯化一次"),
    Task2(2,Lists.newArrayList(ObservableEnum.MissionBox), "领取一次挂机奖励"),
    Task3(3,Lists.newArrayList(ObservableEnum.TankLv), "坦克升级一次"),
    Task4(4,Lists.newArrayList(ObservableEnum.TroopChange), "上阵指定tank") {//ObservableEnum.TroopChange(25)

        @Override
        public boolean chkEffect(Player player, AbstractTask task, ITaskConfTemplate taskTemplate, ObservableEnum observableEnum, Object... argc) {
            return chkProgress(player, task, taskTemplate);
        }

        @Override
        protected boolean chkProgress(Player player, AbstractTask task, ITaskConfTemplate taskTemplate) {
            int tankId = taskTemplate.getTaskFinishParaInt();
            boolean isSuc = TroopServerContainer.of(player.getServerId()).getWorldTroopByPlayer(player)
                    .stream().anyMatch(e -> e.getTroopArmy().isContainTankId(tankId));
            if (isSuc) {
                return incTaskProgress(task, taskTemplate, 1, false);
            }
            return false;
        }
    },
    Task5(5, Lists.newArrayList(ObservableEnum.ClearnceMission),"通关主线副本") { // 监听 ObservableEnum.FBFightWin(37)
        @Override
        public boolean chkEffect(Player player, AbstractTask task, ITaskConfTemplate taskTemplate, ObservableEnum observableEnum, Object... argc) {
            return chkProgress(player, task, taskTemplate);
        }

        @Override
        protected boolean chkProgress(Player player, AbstractTask task, ITaskConfTemplate taskTemplate) {
            if (player.playerMission().getFbId() >= taskTemplate.getTaskFinishParaInt()) {
                return incTaskProgress(task, taskTemplate, 1, false);
            }
            return false;
        }
    },
    Task6(6, Lists.newArrayList(ObservableEnum.TankStarUp), "坦克升星一次"),
    Task7(7,Lists.newArrayList(ObservableEnum.KFRankFight), "角斗一次"),

    Task8(8,Lists.newArrayList(ObservableEnum.MilitaryLv), "军衔达到指定等级") {

        @Override
        public boolean chkEffect(Player player, AbstractTask task, ITaskConfTemplate taskTemplate, ObservableEnum observableEnum, Object... argc) {
            if((boolean) argc[0]) {
                return chkProgress(player, task, taskTemplate);
            }
            return false;
        }

        @Override
        protected boolean chkProgress(Player player, AbstractTask task, ITaskConfTemplate taskTemplate) {
            if (player.playerCommander().getMilitaryLv() >= taskTemplate.getTaskFinishParaInt()) {
                return incTaskProgress(task, taskTemplate, 1, false);
            }
            return false;
        }
    },
    Task9(9,Lists.newArrayList(ObservableEnum.FightPveBoss), "兽王试炼一次"),
    Task10(10,Lists.newArrayList(ObservableEnum.CarLv), "升阶1次坐骑") {
        @Override
        public boolean chkEffect(Player player, AbstractTask task, ITaskConfTemplate taskTemplate, ObservableEnum observableEnum, Object... argc) {
            if((boolean) argc[0]) {
                return incTaskProgress(task, taskTemplate);
            }
            return false;
        }
    },
    Task11(11,Lists.newArrayList(ObservableEnum.MilitaryLv), "晋升1次头衔") {
        @Override
        public boolean chkEffect(Player player, AbstractTask task, ITaskConfTemplate taskTemplate, ObservableEnum observableEnum, Object... argc) {
            if((boolean) argc[0]) {
                return incTaskProgress(task, taskTemplate);
            }
            return false;
        }
    },
    JoinGuild(12,Lists.newArrayList(ObservableEnum.GuildPlayerAdd), "加入部落") {
        @Override
        public boolean chkEffect(Player player, AbstractTask task, ITaskConfTemplate taskTemplate, ObservableEnum observableEnum, Object... argc) {
            return chkProgress(player,task,taskTemplate);
        }

        @Override
        protected boolean chkProgress(Player player, AbstractTask task, ITaskConfTemplate taskTemplate) {
            if (player.playerGuild().getGuildId() > 0) {
                return incTaskProgress(task, taskTemplate, 1, false);
            }
            return false;
        }
    },
    Task13(13,Lists.newArrayList(ObservableEnum.GuildDonation), "部落捐献一次"),
    Task14(14,Lists.newArrayList(ObservableEnum.ComposeEqu), "合成n件装备") {
        @Override
        public boolean chkEffect(Player player, AbstractTask task, ITaskConfTemplate taskTemplate, ObservableEnum observableEnum, Object... argc) {
            return chkProgress(player,task,taskTemplate);
        }
        @Override
        protected boolean chkProgress(Player player, AbstractTask task, ITaskConfTemplate taskTemplate) {
            int count = (int)Arrays.stream(player.playerEquip().getEqus()).filter(e -> e.getEquId() > 0).count();
            if (count >= taskTemplate.getTaskFinishParaInt()) {
                return incTaskProgress(task, taskTemplate, count, false);
            }
            return false;
        }
    },
    Task15(15,Lists.newArrayList(ObservableEnum.StrengthenEqu), "强化装备一次"),
    Task16(16,Lists.newArrayList(ObservableEnum.ChangeStone), "镶嵌宝石一次"),


    Task999(999,Lists.newArrayList(ObservableEnum.TankAdd), "拥有指定ID的坦克") {//监听 ObservableEnum.TankAdd(30)
        @Override
        public boolean chkEffect(Player player, AbstractTask task, ITaskConfTemplate taskTemplate, ObservableEnum observableEnum, Object... argc) {
            int tankId = (int) argc[0];
            if (tankId != taskTemplate.getTaskFinishParaInt()) {
                return false;
            }
            return chkProgress(player, task, taskTemplate);
        }

        @Override
        protected boolean chkProgress(Player player, AbstractTask task, ITaskConfTemplate taskTemplate) {
            boolean hasTank = player.playerTank().isHaveTank(taskTemplate.getTaskFinishParaInt());
            if (!hasTank) {
                return false;
            }
            return incTaskProgress(task, taskTemplate, 1, false);
        }
    },
    * */

    ;

    private int id;
    private String desc;
    private List<ObservableEnum> obsList;

    TaskTypeEnum(int id, List<ObservableEnum> obsList,String desc) {
        this.id = id;
        this.obsList = obsList;
        this.desc = desc;
    }


    public boolean chkEffect(Player player, AbstractTask task, ITaskConfTemplate taskTemplate, ObservableEnum observableEnum, Object... argc) {
        return incTaskProgress(task, taskTemplate);
    }

    protected boolean chkProgress(Player player, AbstractTask task, ITaskConfTemplate taskTemplate) {
        return false;
    }

    public boolean isOpenToday(Player player) {
        return true;
    }

    public boolean dealObservableNotice(Player player, AbstractTask task, ITaskConfTemplate taskTemplate, ObservableEnum observableEnum, Object... argc) {
        try {
            if (task.isEffectSelf()) {
                return task.chkEffectSelf(player, observableEnum, argc);
            }
            return chkEffect(player, task, taskTemplate, observableEnum, argc);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkTaskComplete(Player player, AbstractTask task, ITaskConfTemplate taskTemplate) {
        try {
            return chkProgress(player, task, taskTemplate);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    private static boolean incTaskProgress(AbstractTask task, ITaskConfTemplate taskTemplate) {
        return incTaskProgress(task, taskTemplate, 1, true);
    }

    private static boolean incTaskProgress(AbstractTask task, ITaskConfTemplate taskTemplate, long inc) {
        return incTaskProgress(task, taskTemplate, inc, true);
    }

    private static boolean incTaskProgress(AbstractTask task, ITaskConfTemplate taskTemplate, long inc, boolean isAdd) {
        if (inc <= 0) {
            return false;
        }
        //TODO 完成条件统一改成 A:B 或者B 总进度为A
        long oldProgress = task.getProgress();
        int oldState = task.getState();
        long expect = taskTemplate.getTaskFinish();

        if (isAdd) {
            task.setProgress(Math.min(expect, oldProgress + inc));
        } else {
            task.setProgress(Math.min(expect, Math.max(inc, oldProgress)));
        }
        if (task.getProgress() >= expect) {
            task.setState(TaskStatus.COMPLETE);
        }
        return oldProgress != task.getProgress() || oldState != task.getState();
    }

    public static TaskTypeEnum num2enum(int num) {
        return Arrays.stream(TaskTypeEnum.values()).filter(e -> e.getId() == num).findAny().orElse(Empty);
    }

    public boolean isFitObserver(ObservableEnum event) {
        return obsList.contains(event);
    }
}
