package com.hm.model.strength;

import com.google.common.collect.Lists;
import com.hm.config.strength.excel.BlockPartsTemplate;
import com.hm.libcore.mongodb.PrimaryKeyWeb;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StrengthStore{
    // 模板id
    private int id;
    // 唯一id
    private String uid;
    // 等级
    private int lv = 1;
    // 当前经验
    private int exp;
    // 升华次数
    private int sublimationTimes;
    // 是否锁定
    private boolean lock;
    // 属性
    private List<Integer> attrList = Lists.newArrayList();
    // （精炼后）待确认的 属性值
    private List<Integer> list;

    public StrengthStore(BlockPartsTemplate template){
        this.id = template.getId();
    }

    public void upSublimation(){
        this.sublimationTimes++;
    }

    public void setUid(int serverId){
        uid = serverId+"_"+ PrimaryKeyWeb.getPrimaryKey("StrengthStore", serverId);
    }

}
