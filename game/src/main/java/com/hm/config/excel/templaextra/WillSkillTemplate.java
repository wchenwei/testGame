package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.CentralControlSpecialSkillTemplate;
import com.hm.enums.WillSkillAddType;
import com.hm.util.StringUtil;
import lombok.Data;

import java.util.Map;

@Data
@FileConfig("central_control_special_skill")
public class WillSkillTemplate extends CentralControlSpecialSkillTemplate {
    private int[] typeAdds;

    public void init() {
        this.typeAdds = new int[WillSkillAddType.values().length];
        for (Map.Entry<Integer, Integer> entry : StringUtil.strToMap(getType(), ";", ":").entrySet()) {
            this.typeAdds[entry.getKey() - 1] = entry.getValue();
        }
    }

}
