package com.hmkf.action.fight;

import com.hm.action.troop.client.ClientTroop;
import com.hm.model.guild.Guild;
import com.hm.model.player.Player;
import com.hm.war.sg.troop.AbstractPlayerFightTroop;
import com.hmkf.guild.KfGuildUtils;
import com.hmkf.model.KfTroops;

public class KfPlayerTroop extends AbstractPlayerFightTroop {
    private Player player;

    public KfPlayerTroop(Player player, ClientTroop clientTroop) {
        super(player.getId(), "kftroop" + player.getId());
        setTankList(clientTroop.getArmyList());
        setFormationId(clientTroop.getAircraftId());
        this.player = player;
    }

    public KfPlayerTroop(Player player, KfTroops defTroops) {
        super(player.getId(), "kftroop" + player.getId());
        setTankList(defTroops.getTanks());
        setFormationId(defTroops.getAircraftId());
        this.player = player;
    }

//    @Override
//    public Map<Integer, Double> buildAllTankAddAttr(Player player) {
//        return Maps.newHashMap(KFLevelConstants.AllTankAttrMap);
//    }

//    @Override
//    public ArrayListMultimap<Integer, Integer> loadTankExtraSkill() {
//        ArrayListMultimap<Integer, Integer> extraSkillMap = ArrayListMultimap.create();
//        KfCenterContainer centerContainer = SpringUtil.getBean(KfCenterContainer.class);
//        KfRankConfig rankConfig = SpringUtil.getBean(KfRankConfig.class);
//        KfCenterData centerData = centerContainer.getCenterData();
//        KfLevelSkillTemplate template = rankConfig.getKfLevelSkillTemplate(centerData.getCenterLevel().getSkilTypeId());
//        if (template != null) {
//            int skillId = template.getSkill_id();
//            List<Integer> tankId = getTankList().stream().map(e -> e.getId()).collect(Collectors.toList());
//            List<Integer> luckIds = template.getLuckTankId(tankId);
//            for (Integer id : luckIds) {
//                extraSkillMap.put(id, skillId);
//            }
//        }
//        return extraSkillMap;
//    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public Guild getPlayerGuild(Player player) {
        return KfGuildUtils.getGuild(player);
    }
}
