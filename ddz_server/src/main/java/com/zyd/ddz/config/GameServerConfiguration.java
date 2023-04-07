package com.zyd.ddz.config;

import xyz.noark.core.annotation.Configuration;
import xyz.noark.core.annotation.configuration.Bean;
import xyz.noark.game.monitor.MonitorManager;
import xyz.noark.game.monitor.impl.MemoryMonitorService;

/**
 * @author zyd
 * @date 2023/3/16 16:15
 */
@Configuration
public class GameServerConfiguration {

    @Bean
    public MonitorManager monitorManager() {
        MonitorManager manager = new MonitorManager();
        manager.addMonitorService(new MemoryMonitorService());
        return manager;
    }
}
