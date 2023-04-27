package com.zyd.ddz.config;

import com.zyd.ddz.event.monitor.RoomMonitor;
import com.zyd.ddz.event.RoomHeartEvent;
import com.zyd.ddz.factory.RoomManagerFactory;
import com.zyd.ddz.room.AbstractRoomManager;
import com.zyd.ddz.utils.IdUtils;
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

    @Bean
    public MonitorManager monitorManager() {
        MonitorManager manager = new MonitorManager();
        manager.addMonitorService(new MemoryMonitorService());
        return manager;
    }

    @Bean
    public RoomMonitor roomExecutors(){
        RoomMonitor roomMonitor = new RoomMonitor();
        for (AbstractRoomManager roomManager : RoomManagerFactory.getRooms().values()) {
            roomMonitor.addService(new RoomHeartEvent(roomManager));
        }
        return roomMonitor;
    }


    @Bean
    public EventManager eventManager(){
        IdUtils.setSid(sid);
        return new DefaultEventManager();
    }



}
