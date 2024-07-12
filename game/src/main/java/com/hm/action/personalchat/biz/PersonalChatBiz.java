package com.hm.action.personalchat.biz;//package com.hm.action.personalchat.biz;
//
//import com.hm.libcore.annotation.Biz;
//import com.hm.libcore.msg.JsonMsg;
//import com.hm.action.passenger.vo.PersonalChatRoomVo;
//import com.hm.action.player.PlayerBiz;
//import com.hm.db.PlayerUtils;
//import com.hm.message.MessageComm;
//import com.hm.model.personalchat.PersonalChatMsg;
//import com.hm.model.personalchat.PersonalChatRoom;
//import com.hm.model.player.Player;
//import com.hm.servercontainer.personalchat.PersonalChatContainer;
//import com.hm.servercontainer.personalchat.PersonalChatItemContainer;
//
//import javax.annotation.Resource;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Biz
//public class PersonalChatBiz {
//    @Resource
//    private PersonalChatContainer personalChatContainer;
//    @Resource
//    private PlayerBiz playerBiz;
//
//    public PersonalChatRoom getChatRoom(Player player, int targetId) {
//        PersonalChatItemContainer container = personalChatContainer.getItemContainer(player.getServerId());
//        return container.getRoom(player.getId(), targetId);
//    }
//
//    public void sendPersonalChatMsg(Player player) {
//        PersonalChatItemContainer container = personalChatContainer.getItemContainer(player.getServerId());
//        List<PersonalChatRoomVo> vos = player.playerPersonalChat().getRoomIds().stream().map(t -> {
//            PersonalChatRoom room = container.getRoom(t);
//            return room.createRoomVo();
//        }).filter(t -> t != null).collect(Collectors.toList());
//        player.sendMsg(MessageComm.S2C_PersonalChat_Infos, vos);
//    }
//
//    public void boardRoomMsgChange(PersonalChatRoom room, PersonalChatMsg chatMsg) {
//        List<Player> players = room.getPlayerIds().stream().map(t -> PlayerUtils.getPlayer(t)).collect(Collectors.toList());
//        players.forEach(t -> {
//            JsonMsg msg = JsonMsg.create(MessageComm.S2C_PersonalChat_Change);
//            msg.addProperty("roomId", room.getId());
//            msg.addProperty("chatMsg", chatMsg);
//            t.sendMsg(msg);
//        });
//    }
//}
