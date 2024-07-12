package com.hm.http;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.util.TimeUtils;
import com.hm.config.GameConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Biz
public class HttpBiz {
    /**
     * 禁言
     *
     * @return
     * @throws Exception 使用说明
     * @author Administrator
     */
    public String gagPlayer(long playerId) {
        String url = ServerConfig.getInstance().getGmUrl() + "stat/playerOperate/sysGag";
        Map<String, Object> paramMap = Maps.newConcurrentMap();
        paramMap.put("playerId", playerId);
        paramMap.put("startTime", TimeUtils.getTimeString(System.currentTimeMillis()));
        paramMap.put("endTime", TimeUtils.getTimeString(System.currentTimeMillis() + 2 * GameConstants.DAY));
        try {
            return HttpUtil.post(url, paramMap);
        } catch (Exception e) {
            log.error("", e);
        }
        return "";
    }

    /**
     * 拉小黑屋
     *
     * @return
     * @throws Exception 使用说明
     * @author Administrator
     */
    public String blackHome(long playerId) {
        String url = ServerConfig.getInstance().getGmUrl() + "stat/playerOperate/sysBlackHome";
        Map<String, Object> paramMap = Maps.newConcurrentMap();
        paramMap.put("playerId", playerId);
        paramMap.put("startTime", TimeUtils.getTimeString(System.currentTimeMillis()));
        paramMap.put("endTime", TimeUtils.getTimeString(System.currentTimeMillis() + 7 * GameConstants.DAY));
        try {
            return HttpUtil.post(url, paramMap);
        } catch (Exception e) {
            log.error("", e);
        }
        return "";
    }

    /**
     * 拉小黑屋
     *
     * @return
     * @throws Exception 使用说明
     * @author Administrator
     */
    public String blackHome(long playerId, int seconds) {
        long startTime = System.currentTimeMillis();
        long endTime = startTime + TimeUnit.SECONDS.toMillis(seconds);

        String url = ServerConfig.getInstance().getGmUrl() + "stat/playerOperate/sysBlackHome";
        Map<String, Object> paramMap = Maps.newConcurrentMap();
        paramMap.put("playerId", playerId);
        paramMap.put("startTime", TimeUtils.getTimeString(startTime));
        paramMap.put("endTime", TimeUtils.getTimeString(endTime));
        try {
            return HttpUtil.post(url, paramMap);
        } catch (Exception e) {
            log.error("", e);
        }
        return "";
    }


    /**
     * 拉小黑屋
     *
     * @return
     * @throws Exception 使用说明
     * @author Administrator
     */
    public String getNoChatIds() {
        String url = ServerConfig.getInstance().getGmUrl() + "stat/playerOperate/getNochatPlayerIds";
        Map<String, Object> paramMap = Maps.newConcurrentMap();
        try {
            return HttpUtil.post(url, paramMap);
        } catch (Exception e) {
            log.error("", e);
        }
        return "";
    }


    //同步发送
    private String sendHttpForLoginServer(String url, Map<String, Object> paramMap) throws Exception {
        String result = HttpUtil.get(url, paramMap);
        return new JSONObject(result).getStr("data");
    }

    //异步发送post
    public static void sendAsyncPost(String url, Map<String, Object> parms) {
        //使用任务线程池处理业务逻辑
        ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) SpringUtil.getBean("serviceExecutor");
        executor.execute(new HttpTask(url, parms));
    }

}
