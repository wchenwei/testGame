package com.hm.model.tank;

import com.hm.libcore.spring.SpringUtil;
import com.hm.action.tank.biz.ControlBiz;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TankControl {
    private ElementPos[] pos = new ElementPos[]{null,null,null,null,null,null,null,null,null};

    private ElementSkill[] skills = new ElementSkill[]{null,null,null};
    //战意技
    private ElementSkill bigSkill;


    public void changeElement(int index, int elementId) {
        ElementPos p =  pos[index];
        p.changeElement(elementId);
    }
    public ElementPos[] getPos(){
        if(Arrays.stream(pos).anyMatch(t ->t==null)){
            ControlBiz controlBiz = SpringUtil.getBean(ControlBiz.class);
            //如果没有初始化过则对pos进行初始化
            pos = controlBiz.initPos();
        }
        return pos;
    }

    public ElementPos getPos(int index){
        if(Arrays.stream(pos).anyMatch(t ->t==null)){
            ControlBiz controlBiz = SpringUtil.getBean(ControlBiz.class);
            //如果没有初始化过则对pos进行初始化
            pos = controlBiz.initPos();
        }
        return pos[index];
    }
    //是否装配了元件
    public boolean isUp(){
        return Arrays.stream(pos).anyMatch(t->t!=null&&t.getElementId()>0);
    }

    public void changePos(int p1, int p2) {
        ElementPos pos1 = pos[p1];
        ElementPos pos2 = pos[p2];
        pos[p1] =new ElementPos(pos2.getColor(),pos2.getElementId());
        pos[p2] =new ElementPos(pos1.getColor(),pos1.getElementId());
    }
}
