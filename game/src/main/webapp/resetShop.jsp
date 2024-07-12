<%@ page import="org.springframework.data.mongodb.core.MongoTemplate" %>
<%@ page import="com.hm.libcore.mongodb.MongoUtils" %>
<%@ page import="org.bson.Document" %>
<%@ page import="com.mongodb.client.MongoCollection" %>
<%@ page import="com.hm.enums.ShopType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%

    int serverId = Integer.parseInt(request.getParameter("serverId"));
    MongoTemplate mongoTemplate = MongoUtils.getServerMongoTemplate(serverId);
    if (mongoTemplate == null) {
        return;
    }
    MongoCollection<Document> collPlayer = mongoTemplate.getCollection("player");
    Document unsetFields = new Document();
    unsetFields.append("playerShop.shopMap."+ ShopType.KfWorldWarShop.getType(), "")
    ;
    Document update = new Document("$unset", unsetFields);
    collPlayer.updateMany(new Document(), update);
    out.print("suc");
%>
