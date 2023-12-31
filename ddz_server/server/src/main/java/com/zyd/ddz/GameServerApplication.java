package com.zyd.ddz;

import com.zyd.ddz.common.dao.UserDao;
import com.zyd.ddz.common.domain.UserDomain;
import com.zyd.ddz.common.manager.AbstractRoomManager;
import com.zyd.ddz.server.event.MailCheckEvent;
import com.zyd.ddz.server.event.RoomHeartEvent;
import com.zyd.ddz.server.factory.RoomManagerFactory;
import com.zyd.zgame.core.thread.ThreadManager;
import com.zyd.zgame.orm.cache.CacheManager;
import com.zyd.zgame.server.annotation.EnableCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


/**
 * @author zyd
 * @date 2022/12/26 12:37
 */
@SpringBootApplication
@EnableCache
public class GameServerApplication {


    public static void main(String[] args) {
        SpringApplication.run(GameServerApplication.class, args);
    }

    @Bean
    public void scheduleTask(){
        ThreadManager.getDelayExecutor().dispatcherSchedule(new MailCheckEvent());

        for (AbstractRoomManager roomManager : RoomManagerFactory.getRooms().values()) {
            ThreadManager.getDelayExecutor().dispatcherSchedule(new RoomHeartEvent(roomManager));
        }
    }
}
