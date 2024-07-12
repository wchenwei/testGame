package com.hmkf.action;

import com.google.common.collect.Lists;
import com.hm.action.troop.redis.PlayerTroopRedisDB;
import com.hm.action.troop.redis.TroopRDB;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.troop.client.ClientTroop;
import com.hm.action.troop.client.ClientTroopGroup;
import com.hm.model.player.Player;
import com.hm.util.ArmyUtils;
import com.hm.war.sg.troop.AbstractFightTroop;
import com.hm.war.sg.troop.AbstractPlayerFightTroop;
import com.hm.war.sg.troop.TankArmy;
import com.hmkf.action.fight.KfPlayerTroop;
import com.hmkf.db.KfPlayerUtils;
import com.hmkf.model.KFPlayer;

import java.util.List;

public class KfClientTroopUtils {
    public static List<ClientTroop> buildClientTroop(KFPlayer player) {
        List<ClientTroop> troopList = Lists.newArrayList();
        PlayerTroopRedisDB troopRedisDB = PlayerTroopRedisDB.getPlayerTroopRedisDB(player.getId());
        if(troopRedisDB == null) {
            return troopList;
        }
        for (TroopRDB troopRDB : troopRedisDB.getTroopList()) {
            ClientTroop clientTroop = new ClientTroop();
            clientTroop.setAircraftId(troopRDB.getAircraftId());
            clientTroop.setArmyList(troopRDB.buildTankArmy());

            troopList.add(clientTroop);
        }
        player.getPlayerTemp().setCombat(troopRedisDB.getCombat());
        return troopList;
    }

    public static List<AbstractFightTroop> buildKFTroop(KFPlayer player,List<ClientTroop> troopList) {
        Player gamePlayer = KfPlayerUtils.getPlayerFromDB(player.getId());
        List<AbstractFightTroop> kfTroopList = Lists.newArrayList();
        for (ClientTroop clientTroop : troopList) {
            kfTroopList.add(new KfPlayerTroop(gamePlayer,clientTroop));
        }
        return kfTroopList;
    }


    public static List<AbstractFightTroop> buildDefTroop(KFPlayer player) {
        Player gamePlayer = KfPlayerUtils.getPlayerFromDB(player.getId());
        List<AbstractFightTroop> kfTroopList = Lists.newArrayList();
        for (ClientTroop clientTroop : buildClientTroop(player)) {
            kfTroopList.add(new KfPlayerTroop(gamePlayer,clientTroop));
        }
        return kfTroopList;
    }
}
