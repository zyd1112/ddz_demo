package com.zyd.ddz.factory;

import com.zyd.ddz.constant.ExecutorType;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ExecutorFactory {
    private static final ScheduledThreadPoolExecutor ROOM_EXECUTOR = new ScheduledThreadPoolExecutor(4,
            new ThreadFactory() {
                final AtomicInteger count = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    int curCount = count.incrementAndGet();
                    return new Thread(r, "ROOM-SCHEDULE-" + curCount);
                }
            });


    private static final Map<ExecutorType, ScheduledThreadPoolExecutor> SCHEDULED_THREAD_POOL_EXECUTOR = new HashMap<ExecutorType, ScheduledThreadPoolExecutor>(){{
       put(ExecutorType.ROOM, ROOM_EXECUTOR);
    }};

    public static ScheduledThreadPoolExecutor getExecutor(ExecutorType type){
        return SCHEDULED_THREAD_POOL_EXECUTOR.get(type);
    }
}
