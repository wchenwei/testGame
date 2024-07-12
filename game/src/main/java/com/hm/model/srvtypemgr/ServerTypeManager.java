package com.hm.model.srvtypemgr;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Document(collection = "servertypemanage")
public class ServerTypeManager {
    @Field("serverMark")
    private String serverMark;

    public List<String> getMarks() {
        return StrUtil.splitTrim(serverMark, ",");
    }
}
