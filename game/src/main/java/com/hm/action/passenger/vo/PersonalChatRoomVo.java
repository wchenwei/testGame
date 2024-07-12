package com.hm.action.passenger.vo;

import com.google.common.collect.Lists;
import com.hm.model.personalchat.PersonalChatMsg;
import com.hm.model.player.SimplePlayerVo;
import com.hm.util.LogMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalChatRoomVo {
    private String roomId;
    private List<SimplePlayerVo> playerVos = Lists.newArrayList();
    private LogMode<PersonalChatMsg> msgs;
}
