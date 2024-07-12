package com.hm.action.http;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.hm.libcore.httpserver.handler.HttpSession;
import com.hm.action.http.model.CampOrGuildEntity;
import com.hm.model.guild.Guild;
import com.hm.servercontainer.guild.GuildContainer;
import com.hm.libcore.annotation.Action;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ClassName: CampOrGuildAction. <br/>
 * Function: 查询部落，或者阵营名称
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2020年9月17日22点15分
 *
 * @author zxj
 * @version
 */
@Service("camporguild.do")
public class CampOrGuildAction implements IHttpAction{

    private static final int MAX_SIZE = 100;

    public void getMsg(HttpSession session) {
        List<CampOrGuildEntity> result = Lists.newArrayList();
        int serverid = this.getServerId(session);
        int type = Integer.parseInt(session.getParams("type"));
        //type:1阵营；2部落
        if(serverid<0 || !(type==1 || type==2)) {
            session.Write(JSON.toJSONString(result));
            return;
        }
        List<Guild> guildList = GuildContainer.of(serverid).getAllGuild();
        int size = Math.min(guildList.size(), MAX_SIZE);
        for(int i=0; i<size; i++){
            if(null!=guildList.get(i)) {
                result.add(new CampOrGuildEntity(guildList.get(i)));
            }
        }
        String resultStr = JSON.toJSONString(result);
        session.Write(resultStr);
        return;
    }

    public void changeNotice(HttpSession session) {
        boolean result = false;
        int serverid = this.getServerId(session);
        int type = Integer.parseInt(session.getParams("type"));
        int id = Integer.parseInt(session.getParams("id"));
        //String notice = session.getParams("notice");
        //type:1阵营；2部落
        if(serverid<0 || !(type==1 || type==2)/* || StringUtil.isNullOrEmpty(notice)*/) {
            session.Write(JSON.toJSONString(result));
            return;
        }
        Guild guild = GuildContainer.of(serverid).getGuild(id);
        if(null==guild) {
            session.Write(JSON.toJSONString(result));
            return;
        }
        guild.getGuildInfo().changeNotice("");
        guild.saveDB();
        result = true;
        session.Write(JSON.toJSONString(result));
        return;
    }
}
