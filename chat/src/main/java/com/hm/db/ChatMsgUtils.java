package com.hm.db;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alipay.remoting.util.StringUtils;
import com.google.common.collect.Lists;
import com.hm.config.GameConstants;
import com.hm.libcore.httpserver.handler.HttpSession;
import com.hm.libcore.mongodb.MongodDB;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.CommValueConfig;
import com.hm.libcore.chat.ChatMsgType;
import com.hm.enums.CommonValueType;
import com.hm.libcore.util.TimeUtils;
import com.hm.model.ChatMsg;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Date;
import java.util.List;

public class ChatMsgUtils {
    public static void save(ChatMsg msg, String roomId) {
        MongodDB mongo = ChatMongoUtils.getChatMongoDB();
        mongo.insert(msg, roomId);
    }

    public static List<ChatMsg> getChatMsgList(String roomId) {
        List<ChatMsg> chatMsgList = Lists.newArrayList();

        MongodDB mongo = ChatMongoUtils.getChatMongoDB();
        //当前房间里的总聊天数
        long roomNum = mongo.count(roomId);
        //获取最后的几条
        Query query = new Query();
        query.skip(roomNum - GameConstants.ChatMaxSize);
        chatMsgList.addAll(mongo.query(query, ChatMsg.class, roomId));

        // 保证固定数量的玩家喇叭发言
//        long hornCount = mongo.count(new Query()
//                .addCriteria(Criteria.where("msgType").is(ChatMsgType.Horn.getType())), roomId);
//        if (hornCount > 0) {
//            Query hornQuery = new Query()
//                    .addCriteria(Criteria.where("msgType").is(ChatMsgType.Horn.getType()))
//                    .skip(hornCount - config.getIntValue(CommonValueType.HISTORY_HORN_COUNT));
//            chatMsgList.addAll(mongo.query(hornQuery, ChatMsg.class, roomId));
//        }
        return chatMsgList;
    }

    public static ChatMsg getChatMsg(String roomId, String msgId) {
        MongodDB mongo = ChatMongoUtils.getChatMongoDB();
        return mongo.get(msgId, ChatMsg.class, roomId);
    }

    public static List<String> getRoomList() {
        MongodDB mongo = ChatMongoUtils.getChatMongoDB();
        List<String> roomNames = Lists.newArrayList();
        for (String name : mongo.getCollectionNames()) {
            if (name.startsWith("room")) {
                roomNames.add(name);
            }
        }
        return roomNames;
    }

    //查询聊天记录
    @SuppressWarnings({"deprecation", "rawtypes"})
    public static PageUtils getChatListPage(int pageSize, int currentPage, HttpSession session, String roomId, int serverId, String beginTime, String endTime) {
		MongodDB mongo = ChatMongoUtils.getChatMongoDB();
		//根据角色id，查询
    	String queryInput = session.getParams("searchInput");

    	List<Criteria> criteriaList = Lists.newArrayList();
		//角色ID
    	if(!StringUtils.isEmpty(queryInput)) {
    		criteriaList.add(Criteria.where("playerId").is(Integer.parseInt(queryInput)));
    	}
		criteriaList.add(Criteria.where("serverId").is(serverId));
		if (StrUtil.isNotBlank(beginTime)) {
			long startDate = TimeUtils.ParseTimeStampSafe(beginTime).getTime();
			criteriaList.add(Criteria.where("date").gt(startDate));
		}
		if (StrUtil.isNotBlank(endTime)) {
			long endDate = TimeUtils.ParseTimeStampSafe(endTime).getTime();
			criteriaList.add(Criteria.where("date").lt(endDate));
		}
		Criteria[] criterias = new Criteria[criteriaList.size()];
		criteriaList.toArray(criterias);
		int countNum = 0;
		countNum = new Long(mongo.count(new Query().addCriteria(new Criteria().andOperator(criterias)), roomId)).intValue();
		Page page = new Page(countNum, pageSize, currentPage);
    	Pageable pageable = PageRequest.of(page.getCurrent()-1, page.getSize());
    	Query query = new Query().with(pageable).with(Sort.by(new Sort.Order(Sort.Direction.DESC,"date")));
    	query.addCriteria(new Criteria().andOperator(criterias));
		List<ChatMsg> list = mongo.query(query, ChatMsg.class, roomId);
		//ChatMsgUtils.initChatUser(list);
		PageUtils pageUtils = new PageUtils(list, countNum, page.getSize(), page.getCurrent());
        return pageUtils;
    }

    /*//给chatMsg初始化用户的信息
    private static void initChatUser(List<ChatMsg> list) {
        Map<Long, Player> playerMap = PlayerUtils.getPlayerByIds(ChatMsgUtils.getUserIds(list));
        for(ChatMsg chatMsg :list) {
            chatMsg.setPlayerInfo(playerMap.get(chatMsg.getPlayerId()));
        }
    }*/
    //根据chatMsg获取用户id
    private static Long[] getUserIds(List<ChatMsg> list) {
        Long[] ids = new Long[list.size()];
        for (int i = 0; i < list.size(); i++) {
            ChatMsg chatMsg = list.get(i);
            ids[i] = chatMsg.getPlayerId();
        }
        return ids;
    }

    public static void clearChat() {
        MongodDB mongo = ChatMongoUtils.getChatMongoDB();
        //需要定时清除的数据库名
        List<String> collNames = Lists.newArrayList(mongo.getCollectionNames());
        collNames.forEach(collName -> mongo.deleteByQuery(Query.query(Criteria.where("date").lt(DateUtil.offsetDay(new Date(), -30))), collName));
    }
}
