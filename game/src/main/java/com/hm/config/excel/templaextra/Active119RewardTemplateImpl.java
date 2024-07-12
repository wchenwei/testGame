package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.config.excel.temlate.Active119RewardTemplate;
import com.hm.enums.Act119RewardType;
import com.hm.libcore.annotation.FileConfig;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import com.hm.util.StringUtil;
import lombok.Getter;

import java.util.List;

/**
 * @author wyp
 * @description
 * @date 2022/8/11 9:14
 */
@FileConfig("active_119_reward")
public class Active119RewardTemplateImpl extends Active119RewardTemplate {
    @Getter
    List<Items> listItems = Lists.newArrayList();
    @Getter
    List<Integer> unlock = Lists.newArrayList();

    public void init() {
        listItems = ItemUtils.str2ItemList(this.getMain_reward(), ",", ":");
        if(Act119RewardType.specialLock.getCode() == this.getType()){
            this.unlock = StringUtil.splitStr2IntegerList(this.getUnlock_Position(), ",");
        }
    }

    public boolean isUnLock(List<Integer> getList){
        return getList.containsAll(unlock);
    }

    public boolean isComm(){
        return this.getType() == Act119RewardType.Common.getCode();
    }

}
