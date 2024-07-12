package com.hm.model;

import java.util.Date;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 关键词过滤
 *
 * @author zxj
 * @date 2018-05-22 13:43:13
 */
@Document(collection = "wordfilter")
public class WordfilterEntity {

    /**
     * 主键ID
     */
    @Id
    private Integer id;
    /**
     * 过滤内容
     */
    @Field("content")
    private String content;
    /**
     * 添加日期
     */
    @Field("createTime")
    private Date createtime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}



