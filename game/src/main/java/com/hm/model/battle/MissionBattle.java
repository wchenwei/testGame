package com.hm.model.battle;

import com.hm.config.MissionConfig;
import com.hm.config.excel.templaextra.MissionSpecailExtraTemplate;
import com.hm.enums.BattleType;
import com.hm.libcore.spring.SpringUtil;
import com.hm.model.player.Player;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Author chenwei
 * @Date 2024/5/13
 * @Description: 关卡类战斗
 */
@Getter
@Setter
@NoArgsConstructor
public class MissionBattle extends BaseBattle{
    private int curId;//当前关卡

    public MissionBattle(BattleType battleType){
        super(battleType);
    }

    @Override
    public boolean isCanFight(Player player, int id) {
        //只能攻打下一关卡
        return getNextId()==id;
    }

    //获取下一关
    public int getNextId(){
        MissionConfig missionConfig = SpringUtil.getBean(MissionConfig.class);
        if(this.curId==0){//如果是初始状态则找出玩家该战役类型对应的第一关
            return missionConfig.getFirstMissionId(this.getBattleId());
        }
        MissionSpecailExtraTemplate template = missionConfig.getBattle(this.curId);
        return template.getNext_mission();
    }

    public void clearCurId(){
        this.curId = 0;
    }
}
