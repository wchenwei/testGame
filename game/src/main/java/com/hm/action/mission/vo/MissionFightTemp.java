package com.hm.action.mission.vo;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.config.excel.templaextra.MissionExtraTemplate;
import com.hm.config.excel.templaextra.MissionWaveDrop;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.servercontainer.troop.TroopServerContainer;
import com.hm.util.ItemUtils;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class MissionFightTemp {
    private int missionId;
    private List<Integer> tankIdList;//上阵的坦克列表
    private MWaveTemp[] waveTemps;

    public MissionFightTemp(Player player,MissionExtraTemplate template) {
        this.missionId = template.getId();
        this.tankIdList = TroopServerContainer.of(player.getServerId()).getPlayerTroopTankList(player);

        this.waveTemps = new MWaveTemp[template.getEnemy_wave()];
        for (int i = 0; i < this.waveTemps.length; i++) {
            MissionWaveDrop waveDrop = template.getMissionWaveDrop(i);
            this.waveTemps[i] = new MWaveTemp(waveDrop);
        }
    }

    public List<Items> checkMaveKillItems(int waveId,List<Integer> killList) {
        if(CollUtil.isEmpty(killList)) {
            return Lists.newArrayList();
        }
        MWaveTemp waveTemp = this.waveTemps[waveId];
        List<Items> itemsList = waveTemp.killTank(killList);
        if(CollUtil.isEmpty(waveTemp.getPosList())) {
            itemsList.addAll(waveTemp.getElseList());
        }
        return itemsList;
    }


    @Getter
    public static class MWaveTemp{
        private int totalNum;
        private List<Integer> posList;

        private List<Items>[] baseOneItemArray;
        private List<Items> elseList;


        public MWaveTemp(MissionWaveDrop waveDrop) {
            this.posList = Lists.newArrayList(waveDrop.getPosList());
            this.totalNum = this.posList.size();

            this.baseOneItemArray = waveDrop.cloneOneBaseItem();
            this.elseList = ItemUtils.createCloneItems(waveDrop.getElseList());
        }

        public List<Items> killTank(List<Integer> killList) {
            List<Items> items = Lists.newArrayList();
            for (Integer posId : killList) {
                if(posList.remove(posId)) {
                    List<Items> itemList = findOneItems();
                    if(CollUtil.isNotEmpty(itemList)) {
                        items.addAll(itemList);
                    }
                }
            }
            return items;
        }

        public List<Items> findOneItems() {
            for (int i = 0; i < this.baseOneItemArray.length; i++) {
               if(this.baseOneItemArray[i] != null) {
                   List<Items> itemsList = this.baseOneItemArray[i];
                   this.baseOneItemArray[i] = null;
                   return itemsList;
               }
            }
            return null;
        }

    }
}
