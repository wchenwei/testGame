package com.hm.action.voucher.voucher;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.util.pro.ProUtil;
import com.hm.model.item.Items;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.MongodDB;
import com.hm.sysConstant.ItemConstant;

import java.util.List;
import java.util.Map;

/**
 * 兑换码工具类型
 * @author siyunlong
 * @version V1.0
 * @Description:
 * @date 2023/10/11 10:37
 */
public class VoucherUtils {
    public static String Voccher_DBName = null;

    public static Map<String,List<Items>> codeMap = Maps.newConcurrentMap();
    
    public static List<Items> getVoucherItem(String code) {
        List<Items> itemsList = codeMap.get(code);
        if(CollUtil.isNotEmpty(itemsList)) {
            return itemsList;
        }

        MongodDB mongodDB = MongoUtils.getMongodDB(getAdminDB());
        VoucherSet voucherSet = mongodDB.get(code, VoucherSet.class);
        if(voucherSet == null) {
            return null;
        }
        itemsList = str2HTItemList(voucherSet.getRewards(),",",":");
        ItemConstant.filterHideItems(itemsList);
        codeMap.put(code,itemsList);
        return itemsList;
    }

    public static List<Items> str2HTItemList(String source,String sp1,String sp2) {
        List<Items> items = Lists.newArrayList();
        try {
            for (String str : source.split(sp1)) {
                int id = Integer.parseInt(str.split(sp2)[0]);
                int type = Integer.parseInt(str.split(sp2)[1]);
                long count = Long.parseLong(str.split(sp2)[3]);
                items.add(new Items(id,count,type));
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return items;
    }

    public static String getAdminDB() {
        if(StrUtil.isNotEmpty(Voccher_DBName)) {
            return Voccher_DBName;
        }
        ProUtil pro = new ProUtil("/mongo.properties");
        String dbName = pro.getValue("admin_dbname");
        if(StrUtil.isNotEmpty(dbName)) {
            Voccher_DBName = dbName;
            return dbName;
        }
        Voccher_DBName = MongoUtils.getGameDBName().split("_")[0]+"_admin";
        return Voccher_DBName;
    }

}
