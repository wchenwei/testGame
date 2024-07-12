package com.hm.action.http;

import com.hm.libcore.mongodb.MongoUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CTestData {
    private String id;
    private String val;

    public CTestData(String id, String val) {
        this.id = id;
        this.val = val;
    }

    public void saveDB() {
        MongoUtils.getLoginMongodDB().save(this);
    }

    public static CTestData getCTestData(String id) {
        return MongoUtils.getLoginMongodDB().get(id,CTestData.class);
    }
}
