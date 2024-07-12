package com.hm.action.http;

import cn.hutool.core.lang.Console;
import cn.hutool.core.thread.ConcurrencyTester;
import cn.hutool.core.thread.ThreadUtil;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import com.google.protobuf.ByteString;
import com.hm.db.PlayerUtils;
import com.hm.libcore.annotation.Action;
import com.hm.libcore.msg.Router;
import com.hm.libcore.protobuf.HMProtobuf;
import com.hm.libcore.soketserver.handler.HMSession;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.model.fight.FightProxy;
import com.hm.model.player.Player;
import com.hm.model.worldtroop.WorldTroop;
import com.hm.rpc.KFRpcType;
import com.hm.rpc.RpcManager;
import com.hm.servercontainer.troop.TroopServerContainer;
import com.hm.war.sg.Frame;
import com.hm.war.sg.WarParam;
import com.hm.war.sg.WarResult;
import com.hm.war.sg.event.Event;
import com.hm.war.sg.event.EventType;
import com.hm.war.sg.unit.Unit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * @author wyp
 * @description
 * @date 2021/5/14 11:38
 */
@Slf4j
@RestController
@RequestMapping("/test")
@Action
public class HttpTestAction {

    @GetMapping("/test")
    public String test(int count) {
        try {
            String url = "192.168.1.96:3001";
            Map<String, String> param = Maps.newHashMap();
            HMProtobuf.HMRequest msg = HMProtobuf.HMRequest.newBuilder()
                    .setMsgId(1).setPlayerId(6100058+"")
                    .setData(ByteString.copyFrom(GSONUtils.ToJSONString(param),"utf-8"))
                    .build();
            ConcurrencyTester tester = ThreadUtil.concurrencyTest(100, () -> {
                RpcManager.sendMsg(KFRpcType.KF,url,msg);
            });
            Console.log(tester.getInterval());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "1";
    }

    @GetMapping("/msg")
    public String msg(@RequestParam Map<String, String> param) {
        try {
            Player player = PlayerUtils.getPlayer(Integer.parseInt(param.get("playerId")));
            int msgId = Integer.parseInt(param.get("msgId"));

            HMProtobuf.HMRequest msg = HMProtobuf.HMRequest.newBuilder()
                    .setMsgId(msgId).setPlayerId(player.getId()+"")
                    .setData(ByteString.copyFrom(GSONUtils.ToJSONString(param),"utf-8"))
                    .build();

            Router.getInstance().process(msg,(HMSession) player.getSession());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "suc";
    }

    @GetMapping("/testwar")
    public String testwar(long playerId) {
        System.out.println("========testwar========="+playerId);
        Player player = PlayerUtils.getPlayer(playerId);
        List<WorldTroop> gameTroopList = TroopServerContainer.of(player).getWorldTroopByPlayer(player);

        StringBuilder sb = new StringBuilder();
        if (gameTroopList.size() > 0) {
            WorldTroop atk = gameTroopList.get(0);
            int defIndex = gameTroopList.size() > 1 ? 1 : 0;
            WorldTroop def = gameTroopList.get(defIndex);
            atk.getTroopArmy().changeTroopTankFullHp();
            def.getTroopArmy().changeTroopTankFullHp();

            WarParam warParam = new WarParam().setTest(true);
            WarResult warResult = new FightProxy().doFight(atk.buildPlayerTroop(), def.buildPlayerTroop(),warParam);

            sb.append("==============攻击方=========================<br>");
            for (Unit unit : warResult.getAtk().getUnits()) {
                sb.append(unit.getId() + " index:" + unit.getIndex() + "-> tankId:" + unit.getSetting().getTankId() + " maxHp:" + unit.getMaxHp() + "<br>");
            }
            sb.append("==============防守方=========================<br>");
            for (Unit unit : warResult.getDef().getUnits()) {
                sb.append(unit.getId() + " index:" + unit.getIndex() + "-> tankId:" + unit.getSetting().getTankId() + " maxHp:" + unit.getMaxHp() + "<br>");
            }
            sb.append("==============战斗帧=========================<br>");
            Multiset<Integer> eventMap = HashMultiset.create();
            for (Frame temp : warResult.getFrameList()) {
                Frame frame = (Frame) temp;
                sb.append(frame.getId() + "################################################战斗帧事件################################################" + frame.getId() + "<br>");
                for (Event event : frame.getEventList()) {
                    sb.append(event.getId() + "->" + event.toString() + "<br>");
                    eventMap.add(event.getType());
                }
            }

            for (Integer type : eventMap.elementSet()) {
                EventType eventType = EventType.getType(type);
                sb.append(eventType.getType()+":"+eventType.getDesc() + "->" + eventMap.count(type) + "<br>");
            }
        }else{
            return "没有部队";
        }
        return sb.toString();
    }
}
