package com.hm.war.sg.skilltrigger;

import com.hm.war.sg.ChoiceUtils;
import com.hm.war.sg.Frame;
import com.hm.war.sg.unit.Unit;

import java.util.List;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 坦克所在位置判断
 * @date 2018年10月9日 下午7:48:59
 */
public class UnitPosTrigger extends BaseTriggerSkill {
    private boolean isSelf;//是否是自己
    private boolean isRow;//是否是所在行
    private boolean isThan;//是否>=
    private int num;

    public UnitPosTrigger(SkillTriggerType type, boolean isSelf, boolean isRow, boolean isThan) {
        super(type);
        this.isSelf = isSelf;
        this.isRow = isRow;
        this.isThan = isThan;
    }

    @Override
    public boolean isCanTriggerFrame(Frame frame, Unit atk, Unit def, Object... args) {
        Unit unit = isSelf ? atk : def;
        if (isThan) {
            return getPosSize(unit) >= this.num;
        } else {
            return getPosSize(unit) < this.num;
        }
    }

    public int getPosSize(Unit unit) {
        List<Unit> lifeList = unit.getMyGroup().getLifeUnit();
        List<Integer> indexList = isRow ? ChoiceUtils.getLinesByIndex(unit.getIndex())
                : ChoiceUtils.getColsByIndex(unit.getIndex());
        return ChoiceUtils.listUnitsByIndexs(indexList, lifeList).size();
    }

    public void init(String parms, int lv) {
        this.num = Integer.parseInt(parms);
    }
}
