<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.hmkf.action.npc.NpcPlayer" %>
<%@ page import="com.hmkf.container.KFNpcContainer" %>
<%@ page import="com.hmkf.leaderboard.KfLeaderBiz" %>


<%
    for (NpcPlayer value : KFNpcContainer.npcMap.values()) {
        try {
            KfLeaderBiz.updatePlayerRank(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    out.print("suc");

%>
