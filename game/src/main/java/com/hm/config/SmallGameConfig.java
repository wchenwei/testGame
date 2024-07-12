package com.hm.config;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.TypeReference;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.temlate.FunctionUnlockTemplate;
import com.hm.config.excel.temlate.SmallGameTypeTemplate;
import com.hm.config.excel.templaextra.SmallGameLevelTemplateImpl;
import com.hm.enums.GameType;
import com.hm.model.item.Items;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.BaseSamllGame;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.google.common.collect.*;
@Config
public class SmallGameConfig extends ExcleConfig {
    //战役类型配置
    private Map<Integer, SmallGameTypeTemplate> gameTypeMap = Maps.newConcurrentMap();
    private Map<Integer, SmallGameLevelTemplateImpl> levelTemplateMap = Maps.newConcurrentMap();
    private int maxLv = 0;

    @Override
    public void loadConfig() {
//        loadSmallGameTypeConfig();
//        loadLevel();
    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(SmallGameTypeTemplate.class,
                SmallGameLevelTemplateImpl.class);
    }

    private void loadSmallGameTypeConfig() {
        Map<Integer,SmallGameTypeTemplate> tempType = Maps.newConcurrentMap();
        for(SmallGameTypeTemplate template: JSONUtil.fromJson(getJson(SmallGameTypeTemplate.class), new TypeReference<ArrayList<SmallGameTypeTemplate>>(){})){
            tempType.put(template.getId(), template);
        }
        this.gameTypeMap = ImmutableMap.copyOf(tempType);
    }
    private void loadLevel() {
        Map<Integer,SmallGameLevelTemplateImpl> tempLevel = Maps.newConcurrentMap();
        for(SmallGameLevelTemplateImpl template: JSONUtil.fromJson(getJson(SmallGameLevelTemplateImpl.class), new TypeReference<ArrayList<SmallGameLevelTemplateImpl>>(){})){
            template.init();
            tempLevel.put(template.getLevel(), template);
            this.maxLv = Math.max(this.maxLv, template.getLevel());
        }
        this.levelTemplateMap = ImmutableMap.copyOf(tempLevel);
    }

    //检查是否需要开启小游戏
    public void checkGameOpen(BasePlayer player) {
        PlayerFunctionConfig playerFunctionConfig = SpringUtil.getBean(PlayerFunctionConfig.class);
        int lv = player.playerLevel().getLv();
        List<SmallGameTypeTemplate> list = this.gameTypeMap.values().stream().filter(t -> {
            FunctionUnlockTemplate template = playerFunctionConfig.getFunctionTemplate(t.getUnlock_level());
            return lv >= template.getLevel() && !player.playerGame().isUnlockGame(t.getId());
        }).collect(Collectors.toList());
        if(CollectionUtil.isEmpty(list)) {
            return;
        }
        for (SmallGameTypeTemplate template : list) {
            GameType type = GameType.getGameType(template.getId());
            if (type != null) {
                BaseSamllGame game = type.getPlayerGame();
                if (game != null) {
                    player.playerGame().unlockGame(game);
                }
            }
        }
    }

    public boolean checkExpScore(int lv, long exp, long score) {
        if(!levelTemplateMap.containsKey(lv)) {
            return false;
        }
        return this.levelTemplateMap.get(lv).checkExpScore(exp, score);
    }

    public List<Items> getGroundPassReward(int lv) {
        if(!this.levelTemplateMap.containsKey(lv)) {
            return null;
        }
        return this.levelTemplateMap.get(lv).getReward();
    }

    public int getLevel(int lv, long expAll) {
        return levelTemplateMap.values().stream().filter(e -> e.getLevel()>=lv && e.getExp_total() <= expAll)
                .mapToInt(SmallGameLevelTemplateImpl::getLevel).max().orElse(0);
    }

}
