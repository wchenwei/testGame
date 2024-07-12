package com.hm.main.spring;


import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author <a href="mailto:cliff7777@gmail.com">cliff</a>
 * @since 2018-7-27
 */
@Component
public final class GameStartListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
