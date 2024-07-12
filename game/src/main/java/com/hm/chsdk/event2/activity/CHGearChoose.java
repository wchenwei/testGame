package com.hm.chsdk.event2.activity;

import com.hm.chsdk.annotation.EventMsg;
import com.hm.chsdk.event2.CHEventType;
import com.hm.chsdk.event2.CommonParamEvent;
import com.hm.enums.ActivityType;
import com.hm.model.player.Player;
import com.hm.model.tank.Tank;
import com.hm.model.tank.TankMagicReform;
import com.hm.model.tank.TankSpecial;
import com.hm.observer.ObservableEnum;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


/**
 * 草花活动统计  档位选择
 */
@Slf4j
@EventMsg(obserEnum = ObservableEnum.CHAct4005)
public class CHGearChoose extends CommonParamEvent {
    private int huodong_id;
    private String gear_type;
    // 期数
    private int periods_id;
    private List<ModelVo> push_list;

    @Override
    public void init(Player player, Object... argv) {
        ActivityType activityType = (ActivityType) argv[0];
        this.huodong_id = activityType.getType();
        this.gear_type = String.valueOf(argv[1]);
        List<Integer> gearTypeList;
        switch (activityType) {

        }
//        loadEventType(CHEventType.Activity, 4005, "gear_choose");
    }



    @NoArgsConstructor
    class TankModel extends ModelVo {
        private int star;
        private int evolveStar;
        private TankSpecial tankSpecial;
        private TankMagicReform tankMagicReform;

        public TankModel(Tank tank) {
            super(tank.getId(), tank.getLv());
            this.star = tank.getStar();
            this.evolveStar = tank.getEvolveStar();
            this.tankSpecial = tank.getTankSpecial();
            this.tankMagicReform = tank.getTankMagicReform();
        }
    }

    @NoArgsConstructor
    class CarModelVo extends ModelVo {
        // 碎片数量
        private long num;

        public CarModelVo(int id, int lv, long num) {
            super(id, lv);
            this.num = num;
        }
    }

    @NoArgsConstructor
    class ModelVo {
        private int id;
        private int lv;

        public ModelVo(int id, int lv) {
            this.id = id;
            this.lv = lv;
        }
    }

}
