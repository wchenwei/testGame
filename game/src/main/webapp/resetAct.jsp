<%@ page import="org.springframework.data.mongodb.core.MongoTemplate" %>
<%@ page import="com.hm.libcore.mongodb.MongoUtils" %>
<%@ page import="org.bson.Document" %>
<%@ page import="com.mongodb.client.MongoCollection" %>
<%@ page import="com.hm.servercontainer.guild.GuildContainer" %>
<%@ page import="com.hm.model.guild.Guild" %>
<%@ page import="java.util.List" %>
<%@ page import="com.hm.db.PlayerUtils" %>
<%@ page import="com.hm.model.player.Player" %>
<%@ page import="com.hm.container.PlayerContainer" %>
<%@ page import="com.hm.enums.LeaveOnlineType" %>
<%@ page import="com.hm.libcore.spring.SpringUtil" %>
<%@ page import="com.hm.action.sys.SysFacade" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%

    int serverId = Integer.parseInt(request.getParameter("serverId"));
    SysFacade sysFacade = SpringUtil.getBean(SysFacade.class);
    for (Player player : PlayerContainer.getOnlinePlayers()) {// 踢下线
        sysFacade.sendLeavePlayer(player, LeaveOnlineType.SERVER);
    }

    MongoTemplate mongoTemplate = MongoUtils.getServerMongoTemplate(serverId);
    if (mongoTemplate == null) {
        return;
    }
    MongoCollection<Document> collPlayer = mongoTemplate.getCollection("player");
    Document unsetFields = new Document();
    unsetFields.append("playerActivity", "")
    ;
    Document update = new Document("$unset", unsetFields);
    collPlayer.updateMany(new Document(), update);
    out.print("suc");
%>
