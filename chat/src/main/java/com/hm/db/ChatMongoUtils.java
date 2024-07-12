package com.hm.db;

import com.hm.libcore.mongodb.MongoDBBuilder;
import com.hm.libcore.mongodb.MongodDB;
import com.hm.libcore.spring.SpringUtil;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;

public class ChatMongoUtils {
    private static String ChatDBName = "slg_chat_human";
    private static String mongo_login_uri;

    private static MongodDB chatMongoDB;

    private static MongoDBBuilder mongoDBBuilder = new MongoDBBuilder();
    static {
        Environment environment = SpringUtil.getBean(Environment.class);
        mongo_login_uri = environment.getProperty("mongo.mongo_login_uri");
        if (environment.containsProperty("mongo.chat_dbname")) {
            ChatDBName = environment.getProperty("mongo.chat_dbname");
        }
        chatMongoDB = new MongodDB(new MongoTemplate(mongoDBBuilder.getLoginMongoClient(mongo_login_uri), ChatDBName));

    }
    public static MongodDB getChatMongoDB() {
        return chatMongoDB;
    }
}
