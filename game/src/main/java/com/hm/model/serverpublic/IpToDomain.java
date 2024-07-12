package com.hm.model.serverpublic;

import com.hm.db.IpToDomainUtil;
import com.hm.libcore.mongodb.MongoUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author wyp
 * @description
 * @date 2020/12/25 9:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "IpToDomain")
public class IpToDomain {
    /**
     * 主键ID
     */
    @Id
    private Integer id;
    /**
     * IP跟端口
     */
    @Field("ipAndPort")
    private String ipandport;
    /**
     * 域名
     */
    @Field("doMain")
    private String domain;

    public static void loadData() {
        List<IpToDomain> list = MongoUtils.getLoginMongodDB().queryAll(IpToDomain.class);
        Map<String, IpToDomain> collect = list.stream().collect(Collectors.toMap(IpToDomain::getIpandport, Function.identity()));
        IpToDomainUtil.getInstance().loadDate(collect);
    }

    public static List<IpToDomain> loadByParam(String ipandport){
        Query query = Query.query(Criteria.where("ipandport").is(ipandport));
        List<IpToDomain> list = MongoUtils.getLoginMongodDB().query(query, IpToDomain.class);
        return list;
    }
}
