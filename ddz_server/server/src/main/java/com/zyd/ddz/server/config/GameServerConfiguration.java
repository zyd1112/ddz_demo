package com.zyd.ddz.server.config;

import com.zyd.ddz.common.event.monitor.MailCheckMonitor;
import com.zyd.ddz.common.event.monitor.RoomMonitor;

import com.zyd.ddz.common.manager.AbstractRoomManager;
import com.zyd.ddz.common.utils.IdUtils;
import com.zyd.ddz.server.event.MailCheckEvent;
import com.zyd.ddz.server.event.RoomHeartEvent;
import com.zyd.ddz.server.factory.RoomManagerFactory;
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
            roomMonitor.addScheduleService(new RoomHeartEvent(roomManager));
        }
        return roomMonitor;
    }

    @Bean
    public MailCheckMonitor mailExecutors(){
        MailCheckMonitor mailCheckMonitor = new MailCheckMonitor();
        mailCheckMonitor.addScheduleService(new MailCheckEvent());
        return mailCheckMonitor;
    }


    @Bean
    public EventManager eventManager(){
        IdUtils.setSid(sid);
        return new DefaultEventManager();
    }



}
