package com.zyd.ddz.config;

import com.zyd.ddz.dao.UserDao;
import com.zyd.ddz.event.RoomHeartEvent;
import com.zyd.ddz.utils.IdUtils;
import xyz.noark.core.annotation.Autowired;
import xyz.noark.core.annotation.Configuration;
import xyz.noark.core.annotation.Value;
import xyz.noark.core.annotation.configuration.Bean;
import xyz.noark.core.event.EventManager;
import xyz.noark.game.event.DefaultEventManager;
import xyz.noark.game.monitor.MonitorManager;
import xyz.noark.game.monitor.impl.MemoryMonitorService;

/**
 * @author zyd
 * @date 2023/3/16 16:15
 */
@Configuration
public class GameServerConfiguration {

    @Value("server.id")
    int sid;

    @Autowired
    UserDao userDao;


    @Bean
    public MonitorManager monitorManager() {
        MonitorManager manager = new MonitorManager();
        manager.addMonitorService(new MemoryMonitorService());
        manager.addMonitorService(new RoomHeartEvent());
        return manager;
    }

    @Bean
    public IdUtils idUtils(){
        IdUtils idUtils = new IdUtils();
        idUtils.setSid(sid);
        return idUtils;
    }

    @Bean
    public EventManager eventManager(){
        return new DefaultEventManager();
    }



}
