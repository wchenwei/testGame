package com.hm.model.tank;

import com.hm.libcore.spring.SpringUtil;
import com.hm.config.ControlConfig;
import com.hm.config.excel.templaextra.ItemElementExtraTemplate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElementPos {
    private int color;//空位的颜色
    private int elementId;//元件id

    public ElementPos(int color) {
        this.color = color;
    }

    public void changeElement(int elementId) {
        this.elementId = elementId;
    }

    public void lvUp() {
        ControlConfig controlConfig = SpringUtil.getBean(ControlConfig.class);
        ItemElementExtraTemplate template = controlConfig.getElement(elementId);
        if(template.getNext_id()>0){
            this.elementId = template.getNext_id();
        }
    }
}
