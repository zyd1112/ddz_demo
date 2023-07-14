package com.zyd.ddz;

import com.zyd.ddz.common.manager.AbstractRoomManager;
import com.zyd.ddz.server.event.MailCheckEvent;
import com.zyd.ddz.server.event.RoomHeartEvent;
import com.zyd.ddz.server.factory.RoomManagerFactory;
import com.zyd.zgame.GameServerBootstrap;
import com.zyd.zgame.core.thread.ThreadManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;


/**
 * @author zyd
 * @date 2022/12/26 12:37
 */
@SpringBootApplication(scanBasePackageClasses = {GameServerApplication.class, GameServerBootstrap.class})
@PropertySource(value = "classpath:config.properties", encoding = "UTF-8")
@ComponentScan(basePackageClasses = {GameServerApplication.class, GameServerBootstrap.class})
public class GameServerApplication {


    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(GameServerApplication.class, args);
        context.getBeanFactory().getBean(GameServerBootstrap.class).startUp();

        context.registerShutdownHook();

        ThreadManager.getDISPATCHER().dispatcherSchedule(new MailCheckEvent());

        for (AbstractRoomManager roomManager : RoomManagerFactory.getRooms().values()) {
            ThreadManager.getDISPATCHER().dispatcherSchedule(new RoomHeartEvent(roomManager));
        }
    }
}
