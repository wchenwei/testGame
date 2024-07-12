package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.config.excel.temlate.Active2063IntegralboxTemplate;
import com.hm.libcore.annotation.FileConfig;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import lombok.Getter;

import java.util.List;

/**
 * @introduce: 类注释
 * @author: wyp
 * @DATE: 2024/2/18
 **/
@FileConfig("active_2063_integralbox")
public class Active2063IntegralboxTemplateImpl extends Active2063IntegralboxTemplate {
    @Getter
    private List<Items> integralList = Lists.newArrayList();

    public void init() {
        this.integralList = ItemUtils.str2DefaultItemImmutableList(this.getReward_integral());
    }


}
