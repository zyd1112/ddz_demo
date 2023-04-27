package com.zyd.ddz.event.monitor;

import xyz.noark.core.annotation.Component;
import xyz.noark.core.thread.MonitorThreadPool;
import xyz.noark.core.thread.NamedThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@Component
public class RoomMonitor implements MonitorThreadPool {
    private final ScheduledExecutorService scheduledExecutor = new ScheduledThreadPoolExecutor(4, new NamedThreadFactory("monitor", false));


    @Override
    public ExecutorService getMonitorService() {
        return this.scheduledExecutor;
    }

    public void addService(GameTask task) {
        this.scheduledExecutor.scheduleWithFixedDelay(task, task.getInitialDelay(), task.getDelay(), task.getUnit());
    }
}
