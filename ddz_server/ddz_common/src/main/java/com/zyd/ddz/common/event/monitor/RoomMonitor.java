package com.zyd.ddz.common.event.monitor;

import xyz.noark.core.annotation.Component;
import xyz.noark.core.thread.MonitorThreadPool;
import xyz.noark.core.thread.NamedThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@Component
public class RoomMonitor extends Monitor {
    private final ScheduledExecutorService scheduledExecutor = new ScheduledThreadPoolExecutor(4, new NamedThreadFactory("room-monitor", false));


    @Override
    public ExecutorService getMonitorService() {
        return this.scheduledExecutor;
    }

}
