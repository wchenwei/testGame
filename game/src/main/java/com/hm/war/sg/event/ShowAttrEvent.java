package com.hm.war.sg.event;


import com.hm.war.sg.unit.Unit;
import com.hm.war.sg.unit.UnitAttr;

/**
 * 测试
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/11/26 10:32
 */
public class ShowAttrEvent extends Event {
    private Unit atk;

    private UnitAttr atkAttr;
    private long hp;


    public ShowAttrEvent(Unit atk) {
        super(atk.getId(), EventType.ShowAttr);
        this.atk = atk;
        this.hp = atk.getHp();
        this.atkAttr = atk.getUnitAttr().clone();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id:" + this.atk.getId() + "=" + this.atkAttr.toString()+ " hp:"+hp);
        return sb.toString();
    }
}
