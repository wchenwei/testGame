package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.action.wx.WXSubsType;
import com.hm.action.wx.WXToken;
import com.hm.action.wx.WXVal;
import com.hm.config.excel.temlate.WxGameTemplate;
import com.hm.config.excel.temlate.WxSubscribeTemplateImpl;

import java.util.List;
import java.util.Map;
import com.google.common.collect.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: wx相关配置
 * @date 2023/8/31 10:18
 */
@Slf4j
@Config
public class WXConfig extends ExcleConfig {
    private Table<String, WXSubsType, WxSubscribeTemplateImpl> subTable = HashBasedTable.create();
    private Map<String, WxGameTemplate> wxGameMap = Maps.newHashMap();

    @Override
    public void loadConfig() {
        loadGame();

        Table<String, WXSubsType, WxSubscribeTemplateImpl> subTable = HashBasedTable.create();
        List<WxSubscribeTemplateImpl> tempList = JSONUtil.fromJson(getJson(WxSubscribeTemplateImpl.class), new TypeReference<List<WxSubscribeTemplateImpl>>() {
        });
        for (WxSubscribeTemplateImpl template : tempList) {
            template.init();
            if(template.getSubsType() != null) {
                subTable.put(template.getGameid(),template.getSubsType(),template);
            }
        }
        this.subTable = subTable;
        log.error("加载微信游戏订阅配置");
    }

    public void loadGame() {
        Map<String, WxGameTemplate> wxGameTemplateMap = Maps.newHashMap();
        List<WxGameTemplate> tempList = JSONUtil.fromJson(getJson(WxGameTemplate.class), new TypeReference<List<WxGameTemplate>>() {
        });
        for (WxGameTemplate wxGameTemplate : tempList) {
            wxGameTemplateMap.put(wxGameTemplate.getGameid(),wxGameTemplate);
        }
        this.wxGameMap = wxGameTemplateMap;
        log.error("加载微信游戏配置");
    }



    @Override
    public List<String> getDownloadFile() {
        return getConfigName(WxSubscribeTemplateImpl.class,WxGameTemplate.class);
    }


    public Map<String, WXToken> buildTokenMap() {
        Map<String,WXToken> tokenMap = Maps.newConcurrentMap();
        for (WxGameTemplate value : wxGameMap.values()) {
            tokenMap.put(value.getGameid(),new WXToken(value.getGameid(),value.getWx_id(),value.getWx_key()));
        }
        return tokenMap;
    }

    public WxSubscribeTemplateImpl getWxSubscribeTemplate(String wxGameId,WXSubsType wxSubsType) {
        return this.subTable.get(wxGameId,wxSubsType);
    }

//    public Map<String, WXVal> buildDataMap(String wxGameId,WXSubsType wxSubsType) {
//        WxSubscribeTemplateImpl template = this.subTable.get(wxGameId,wxSubsType);
//        if(template != null) {
//            return wxSubsType.buildData(template);
//        }
//        return Maps.newHashMap();
//    }
}
